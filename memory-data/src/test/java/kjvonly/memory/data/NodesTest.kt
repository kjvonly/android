package kjvonly.memory.ui

import kjvonly.memory.data.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kjvonly.memory.data.models.EmptyNode
import kjvonly.memory.data.models.File
import kjvonly.memory.data.models.Folder
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.RootTree
import kjvonly.memory.data.models.Tree
import kjvonly.memory.data.models.serialize
import org.junit.Assert.*
import org.junit.Test
class TreeTest{

    @Test
    fun treeNext(){
        //arrange
        val tree = Tree()
        val rn = RootTree("root")

        tree.rootNode = rn
        tree.currentNode.value = tree.rootNode

        val cn = Node("cn")
        rn.add(cn)

        //act
        tree.next(cn)

        //assert
        assertEquals(cn, tree.currentNode)
    }

    @Test
    fun treePrev(){
        //arrange
        val tree = Tree()
        val rn = RootTree("root")
        tree.rootNode = rn
        tree.currentNode.value = tree.rootNode


        val cn = Node("cn")
        rn.add(cn)

        val ccn = Node("ccn")
        cn.add(ccn)

        tree.next(cn)
        tree.next(ccn)

        //act
        tree.prev()

        //assert
        assertEquals(cn, tree.currentNode)

        //act
        tree.prev()

        //assert
        assertEquals(rn, tree.currentNode)

        //act
        tree.prev()

        //assert
        assertEquals(rn, tree.currentNode)
    }
}
class NodesTest {
    @Test
    fun addNodeAddsNodeToNodes(){
        //arrange
        val n = Node("n")

        //act
        n.add(Node("n2"))

        //assert
        assertEquals(1, n.nodes.size)
    }

    @Test
    fun deleteNodeRemovesNodeFromNodes(){
        //arrange
        val n = Node("n")
        val cn = Node("cn")
        val cn2 = Node("cn2")

        //act
        n.add(cn)
        n.add(cn2)
        n.delete(cn)

       //assert
        assertEquals(1, n.nodes.size)
        assertEquals(cn2, n.nodes.first())
    }

    @Test
    fun nextShouldMoveCurrentNode(){
        //arrange
        val root = Node("root")
        val cn  = Node("cn")

    }
}



class MemoryTest{

    @Test
    fun test(){
        //arrange
        val tree = Tree()
        val rn = RootTree("root")

        val gn1 = Node("gn1")
        val gn2 = Node("gn2")

        val cn1 = Node("cn1")
        val cn2 = Node("cn2")

        tree.rootNode = rn
        tree.currentNode.value = rn

        rn.add(gn1)
        rn.add(gn2)

        gn1.add(cn1)
        gn2.add(cn2)
        blah(rn)
    }

    fun blah (node: Node){
        println(node.data)
        node.nodes.forEach {
            blah(it as Node)
        }
    }

    @Test
    fun testSerializable(){
        val tree = getSimpleTree()
        val rn = tree.rootNode
        val sn = serialize(rn)
        val json = Json{encodeDefaults = true}
        val s = json.encodeToString(sn)

        assertTrue(s.isNotEmpty())
        // go from serializableNode To RootNode

    }

}

fun getSimpleTree(): Tree {
    val tree = Tree()

    val g = Folder("Topical Memory System")
    val rn = RootTree(g);

    val g1 = Folder("Series A")
    val n11 = Node(g1)
    val g2 = Folder("Series B")
    val n12 = Node(g2)
    val t = File("Christ the Center", "some plan id")
    val cn1 = Node(t)

    tree.rootNode = rn
    tree.currentNode.value = n11
    rn.parentNode = EmptyNode()
    rn.add(n11)
    rn.add(n12)
    n11.add(cn1)

    return tree
}



