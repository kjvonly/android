package kjvonly.memory.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Tree {
    lateinit var  id: String
    lateinit var rootNode: Node
    var currentNode: MutableState<INode> = mutableStateOf(EmptyNode())

    fun next(nextNode: Node) {
        if (nextNode.parentNode == currentNode.value) {
            currentNode.value = nextNode
        }
    }

    fun prev() {
        if (currentNode.value.parentNode !is EmptyNode) {
            currentNode.value = currentNode.value.parentNode
        }
    }
}

sealed interface INode {
    var id: String
    var parentNode: INode
    var data: Any
}

class EmptyNode(override var id: String = randomUUID(), override var data: Any = "") : INode {
    override lateinit var parentNode: INode
}

class RootTree(override var id: String = randomUUID(), override var data: Any) : Node(id, data) {
    override var parentNode: INode = EmptyNode()
}

open class Node(override var id: String = randomUUID(), override var data: Any) : INode {
    override lateinit var parentNode: INode
    val nodes: MutableList<INode> = mutableListOf()
    fun add(n: Node) {
        n.parentNode = this
        nodes.add(n)
    }

    fun delete(n: Node) {
        val i = nodes.iterator()
        while (i.hasNext()) {
            val it = i.next()
            if (it == n) {
                i.remove()
                return
            }
        }
    }
}


val json = Json { encodeDefaults = true }
fun serialize(node: Node): SerializableNode {
    val sn = SerializableNode()
    sn.id = node.id

    if (node.data is Folder) {
        val g = node.data as Folder
        sn.data = json.encodeToString(g)
        sn.dataType = "folder"
    }

    if (node.data is File) {
        val t = node.data as File
        sn.data = json.encodeToString(t)
        sn.dataType = "file"
    }

    node.nodes.forEach {
        val csn = serialize(it as Node)
        sn.nodes.add(csn)
    }
    return sn
}

@Serializable
class SerializableNode() {
    var id: String = ""
    var data: String = ""
    var dataType: String = ""
    var nodes = mutableListOf<SerializableNode>()
}

