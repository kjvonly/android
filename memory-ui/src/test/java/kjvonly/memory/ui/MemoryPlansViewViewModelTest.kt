package kjvonly.memory.ui
//
//import kjvonly.bible.services.FontSizeService
//import kjvonly.core.services.NavigationService
//import kjvonly.memory.data.MemoryRepository
//import kjvonly.memory.data.models.*
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.*
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.mockito.kotlin.*
//
//internal class MemoryPlansViewViewModelTest {
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val testDispatcher = StandardTestDispatcher()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val testScope = TestScope(testDispatcher)
//
//    lateinit var nsMock: NavigationService
//    lateinit var mrMock: MemoryRepository
//    lateinit var fssMock: FontSizeService
//
//    lateinit var vm: MemoryPlansViewViewModel
//
//    fun getRootNode(): RootNode {
//        val rootOfRoot = RootOfRootNode("ror", "ror", NodeType.RootOfRoot)
//        val root = RootNode("1", "root", NodeType.Node, rootOfRoot, "ror")
//
//        val group1 = Node("1.1", "group", NodeType.Node, root, "1")
//        val child11 = LeafNode("1.1.1", "leaf", NodeType.Leaf, group1, group1.id, "1.1.1")
//        val child12 = LeafNode("1.1.2", "leaf", NodeType.Leaf, group1, group1.id, "1.1.2")
//
//
//        group1.nodes.add(child11)
//        group1.nodes.add(child12)
//        val group2 = Node("1.2", "group", NodeType.Node, root, "1")
//        val child21 = LeafNode("1.2.1", "leaf", NodeType.Leaf, group2, group2.id, "1.2.1")
//        val child22 = LeafNode("1.2.2", "leaf", NodeType.Leaf, group2, group2.id, "1.2.2")
//
//        group2.nodes.add(child21)
//        group2.nodes.add(child22)
//
//        root.nodes.add(group1)
//        root.nodes.add(group2)
//
//        return root
//
//    }
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setUp() {
//        nsMock = spy();
//        mrMock = spy()
//        fssMock = spy()
//        vm = MemoryPlansViewViewModel(
//            testDispatcher,
//            navigationService = nsMock,
//            fontSizeService = fssMock,
//            memoryRepository = mrMock,
//        )
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun shouldDeleteAllGroup1Plans() {
//        // arrange
//        val n = getRootNode()
//
//        // act
//        vm.deleteAllPlans(n.nodes.first())
//
//        //assert
//        verify(mrMock, times(1)).deleteMemoryPlan("1.1.1")
//        verify(mrMock, times(1)).deleteMemoryPlan("1.1.1")
//
//        verify(mrMock, times(0)).deleteMemoryPlan("1.2.1")
//        verify(mrMock, times(0)).deleteMemoryPlan("1.2.1")
//    }
//
//    @Test
//    fun shouldDeleteAllGroup2Plans() {
//        // arrange
//        val n = getRootNode()
//
//        // act
//        vm.deleteAllPlans(n.nodes.last())
//
//        //assert
//        verify(mrMock, times(0)).deleteMemoryPlan("1.1.1")
//        verify(mrMock, times(0)).deleteMemoryPlan("1.1.1")
//
//        verify(mrMock, times(1)).deleteMemoryPlan("1.2.1")
//        verify(mrMock, times(1)).deleteMemoryPlan("1.2.1")
//
//    }
//
//    @Test
//    fun shouldDeleteAllPlans() {
//        // arrange
//        val n = getRootNode()
//
//        // act
//        vm.deleteAllPlans(n)
//
//        //assert
//        verify(mrMock, times(1)).deleteMemoryPlan("1.1.1")
//        verify(mrMock, times(1)).deleteMemoryPlan("1.1.1")
//
//        verify(mrMock, times(1)).deleteMemoryPlan("1.2.1")
//        verify(mrMock, times(1)).deleteMemoryPlan("1.2.1")
//    }
//
//    @Test
//    fun shouldRemoveNodeFromNodeTree() {
//        // arrange
//        val root = getRootNode()
//
//        // act
//        val g = root.nodes.first() as Node
//        vm.removeNode(g, "1.1.1") // adds in node 1.1
//
//
//        // assert
//        for (n in g.nodes) {
//            if (n.id == "1.1.1") {
//                fail()
//            }
//        }
//    }
//
//    @Test
//    fun shouldRemoveGroupFromNodeTree() {
//        // arrange
//        val root = getRootNode()
//
//        // act
//        vm.removeNode(root, "1.1") // adds in node 1.1
//
//        // assert
//        for (n in root.nodes) {
//            if (n.id == "1.1") {
//                fail()
//            }
//        }
//    }
//
//    @Test
//    fun navigateShouldCallWithId() {
//        //act
//        vm.navigate("123")
//
//        //assert
//        verify(nsMock, times(1)).navigate("memory", "memoryPlanView/123")
//    }
//
//    @Test
//    fun onEditShouldCallWithId() {
//        //act
//        vm.onEdit("123")
//
//        //assert
//        verify(nsMock, times(1)).navigate("memory", "memoryPlanEditView/123")
//    }
//
//    @Test
//    fun backShouldNavigateBack() {
//        //act
//        vm.back()
//
//        // assert
//        verify(nsMock, times(1)).back("memory")
//    }
//
//    @Test
//    fun onNodeClickShouldUpdateSelectedNode() {
//        //arrange
//        val n = getRootNode()
//        //act
//        vm.onNodeClick(n.nodes.first())
//
//        //assert
//        verify(nsMock, times(0)).navigate("memory", "memoryPlanView/1.1")
//        assertEquals(vm.selectedNode.value.id, "1.1")
//    }
//
//    @Test
//    fun onNodeClickShouldNavigateIfNodeIsLeaf() {
//        //arrange
//        val n = getRootNode()
//
//        //act
//        val g = n.nodes.first() as Node
//        vm.onNodeClick(g.nodes.first())
//
//        //assert
//        verify(nsMock, times(1)).navigate("memory", "memoryPlanView/1.1.1")
//    }
//
//    @Test
//    fun getNodeShouldReturnRootNode() = runBlocking {
//        //arrange
//        val n = getRootNode()
//        doReturn(n).`when`(mrMock).getNode("1")
//
//        //act
//        vm.getNode("1")
//
//        //assert
//        verify(mrMock, times(1)).getNode("1")
//        assertEquals(vm.rootNode, n)
//        assertEquals(vm.selectedNode.value, n)
//        assertEquals(vm.selectedNodeChildNodes.size, 2)
//        assertEquals(vm.selectedNodeChildNodes.first().id, "1.1")
//        assertEquals(vm.selectedNodeChildNodes.last().id, "1.2")
//    }
//
//
//    fun getSimpleRootNode(): kjvonly.memory.ui.models.RootNode {
//        val tree = Tree()
//        val ror = kjvonly.memory.ui.models.RootNode(Folder("rootOfRoots") as FileSystemObject)
//
//
//        val g = Folder("Topical Memory System")
//        val rn = kjvonly.memory.ui.models.RootNode(g);
//
//        val g2 = Folder("Series A")
//        val gn1 = kjvonly.memory.ui.models.Node(g2)
//
//        val t = kjvonly.memory.ui.models.File("Christ the Center", "some plan id")
//        val cn1 =  kjvonly.memory.ui.models.Node(t)
//        tree.rootNode = rn
//        tree.currentNode.value = rn
//        rn.parentNode = ror
//        ror.nodes.add(rn)
//        rn.add(gn1)
//        gn1.add(cn1)
//
//        return ror
//
//    }
//    @Test
//    fun onSelectNodeShouldUpdateSelectedNode() {
//        // arrange
//        val n = getSimpleRootNode()
//
//        // act
//        blah(n)
//
//        // assert
//    }
//
//    fun blah (node: kjvonly.memory.ui.models.Node){
//        if(node.data is Folder){
//            val g = node.data as Folder
//            println(g.name)
//        }
//
//        if (node.data is kjvonly.memory.ui.models.File){
//            val t = node.data as kjvonly.memory.ui.models.File
//            println(t.name)
//            println(t.path)
//        }
//
//        node.nodes.forEach {
//            blah(it as kjvonly.memory.ui.models.Node)
//        }
//    }
//
//
//}
//
