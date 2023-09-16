package kjvonly.memory.data

import kjvonly.memory.data.models.LeafNode
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.NodeType
import kjvonly.memory.database.MemoryDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kjvonly.memory.data.DefaultMemoryRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.spy


internal class DefaultMemoryRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    lateinit var mr: DefaultMemoryRepository
    lateinit var mrdMock: MemoryDao

    @Before
    fun setUp() {
        mrdMock = spy()
        mr = DefaultMemoryRepository(testDispatcher, mrdMock)
    }

    @Test
    fun getNode() {
        //val json =
            "{\"id\":\"9c019b8d-83cd-4503-acc0-54a07644e4e6\",\"name\":\"aaa\",\"type\":\"Root\",\"planId\":\"\",\"parentNodeId\":null,\"nodes\":[{\"id\":\"3cf2af99-5d63-4e55-9022-5618303ae66b\",\"name\":\"bbbb\",\"type\":\"Node\",\"planId\":\"\",\"parentNodeId\":\"9c019b8d-83cd-4503-acc0-54a07644e4e6\",\"nodes\":[{\"id\":\"f8ee3f8d-53ca-4ebd-aaeb-543880ba7acd\",\"name\":\"ccc\",\"type\":\"Leaf\",\"planId\":\"7c172eca-e785-4048-b2b5-d4370fbf932a\",\"parentNodeId\":\"3cf2af99-5d63-4e55-9022-5618303ae66b\",\"nodes\":[]}]}]}"
        val json = "{\"id\":\"632db4c2-6710-405f-a45a-633921854b7d\",\"name\":\"aaa\",\"type\":\"Root\",\"planId\":\"\",\"parentNodeId\":null,\"nodes\":[{\"id\":\"c8373d60-dccb-4dfe-8805-49a5d64fc754\",\"name\":\"bbbb\",\"type\":\"Node\",\"planId\":\"\",\"parentNodeId\":\"632db4c2-6710-405f-a45a-633921854b7d\",\"nodes\":[{\"id\":\"7c4c5c39-6f7d-4a42-84a5-e5f819f94b72\",\"name\":\"ccc\",\"type\":\"Leaf\",\"planId\":\"32ac092f-efb1-49ea-b8f6-8fbe704b9817\",\"parentNodeId\":\"c8373d60-dccb-4dfe-8805-49a5d64fc754\",\"nodes\":[]}]}]}"
        val r = mr.unflattenedNodes(json)

        assertEquals("aaa", r.name)
        assertEquals( 1, r.nodes.size)
        val rChild = r.nodes.first()
        assertEquals("bbbb", rChild.name)
        assertEquals(NodeType.Node, rChild.type)
        val rChildNode = r.nodes.first() as Node
        val rChildChild = rChildNode.nodes.first()
        assertEquals(NodeType.Leaf, rChildChild.type)
        val rChildChildLeaf = rChildChild as LeafNode
        assertEquals("32ac092f-efb1-49ea-b8f6-8fbe704b9817", rChildChildLeaf.planId)
        assertEquals("c8373d60-dccb-4dfe-8805-49a5d64fc754", rChildChildLeaf.parentNode.id)

    }

    @Test
    fun testFlattenForInsert(){
        val json =
            "{\"id\":\"9c019b8d-83cd-4503-acc0-54a07644e4e6\",\"name\":\"aaa\",\"type\":\"Root\",\"planId\":\"\",\"parentNodeId\":null,\"nodes\":[{\"id\":\"3cf2af99-5d63-4e55-9022-5618303ae66b\",\"name\":\"bbbb\",\"type\":\"Node\",\"planId\":\"\",\"parentNodeId\":\"9c019b8d-83cd-4503-acc0-54a07644e4e6\",\"nodes\":[{\"id\":\"f8ee3f8d-53ca-4ebd-aaeb-543880ba7acd\",\"name\":\"ccc\",\"type\":\"Leaf\",\"planId\":\"7c172eca-e785-4048-b2b5-d4370fbf932a\",\"parentNodeId\":\"3cf2af99-5d63-4e55-9022-5618303ae66b\",\"nodes\":[]}]}]}"
        val r = mr.unflattenedNodes(json)
        val jsonNew = mr.flatten(r)

        assertEquals(json ,jsonNew)
    }
}