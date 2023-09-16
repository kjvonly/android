/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kjvonly.memory.data

import kjvonly.memory.data.di.DefaultIODispatcher
import kjvonly.memory.data.models.*
import kjvonly.memory.database.MemoryDao
import kjvonly.memory.database.MemoryRangeDao
import kjvonly.memory.database.MemoryRangesDao
import kjvonly.memory.database.MemoryTreeDao
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kjvonly.memory.data.models.EmptyNode
import kjvonly.memory.data.models.File
import kjvonly.memory.data.models.Folder
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.Range
import kjvonly.memory.data.models.Ranges
import kjvonly.memory.data.models.SerializableNode
import kjvonly.memory.data.models.Tree
import kjvonly.memory.data.models.serialize


interface MemoryRepository {
    fun deleteMemoryRangesById(planId: String)
    fun deleteNodeRoot(id: String)
    fun saveTree(tree: Tree)
    fun getTrees(): List<Tree>
    fun saveRanges(r: Ranges)
    fun saveRange(ranges: List<Range>)
}

class DefaultMemoryRepository @Inject constructor(
    @DefaultIODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val memoryDao: MemoryDao,
) : MemoryRepository {
    val json = Json { encodeDefaults = true }

//    override fun addMemoryRanges(planId: String, rl: List<IMemoryRange>) {
//        rl.forEach {
//            val id = randomUUID()
//            it.id = id
//            val r = json.encodeToString(it)
//            val mrd = MemoryRangeDao(
//                id = id,
//                planId = planId,
//                memoryRange = r
//            )
//            memoryDao.insertRange(mrd)
//        }
//    }

    override fun deleteMemoryRangesById(rangesId: String) {
        memoryDao.deleteRangesById(rangesId)
    }

    override fun deleteNodeRoot(id: String) {
        memoryDao.deleteTreeRoot(id)
    }

    override fun saveTree(tree: Tree) {
        val sn = serialize(tree.rootNode)
        val s = json.encodeToString(sn)
        val m = MemoryTreeDao(id= tree.id, s)
        memoryDao.insertOrUpdate(m)
    }

    fun deserialize(sn: SerializableNode): Node {
        lateinit var d: Any
        if (sn.dataType == "folder"){
            d = json.decodeFromString<Folder>(sn.data)
        }
        if (sn.dataType == "file"){
            d = json.decodeFromString<File>(sn.data)
        }

        val pn = Node(sn.id, d)
        for (c in sn.nodes){
            val n = deserialize(c)
            n.parentNode = pn
            pn.nodes.add(n)
        }

        return pn
    }
    override fun getTrees(): List<Tree> {
        val daoTrees = memoryDao.getTrees()
        val l = mutableListOf<Tree>()
        for (d in daoTrees){
            val t = Tree()
            t.id = d.id
            val sn = json.decodeFromString<SerializableNode>(d.rootNode)
            val rootNode = deserialize(sn)
            rootNode.parentNode = EmptyNode()
            t.rootNode = rootNode
            t.currentNode.value = rootNode
            l.add(t)
        }

        return l
    }

    override fun saveRanges(r: Ranges) {
        val rd = MemoryRangesDao(r.id, r.treeId, r.nodeId, r.name);
        memoryDao.insertRanges(rd)
    }

    override fun saveRange(ranges: List<Range>) {
        var r = mutableListOf<MemoryRangeDao>()
        for (i in ranges){
            var rd = MemoryRangeDao(i.id, i.rangesId, i.range)
            r.add(rd)
        }
        memoryDao.insertRanges(r)
    }
}