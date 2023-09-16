package kjvonly.memory.ui

import kjvonly.core.services.NavigationService
import kjvonly.memory.data.models.FileSystemObject
import kjvonly.memory.data.models.Node
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kjvonly.memory.ui.MemoryGroupCreateViewModel
import kjvonly.memory.ui.getSimpleTree
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.spy
import org.junit.Assert.*


class MemoryFolderCreateViewModelTest {

    lateinit var vm: MemoryGroupCreateViewModel
    lateinit var navMock: NavigationService

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        navMock = spy()
        vm = MemoryGroupCreateViewModel(navMock)
    }


    @Test
    fun onExpandedShouldSetDropdownExpandedToTrue() {
        //arrange
        vm.dropdownExpanded.value = false

        //act
        vm.onExpanded()

        //assert
        assertEquals(true, vm.dropdownExpanded.value)
    }

    @Test
    fun onDismissShouldSetDropdownExpandedToFalse() {
        //arrange
        vm.dropdownExpanded.value = true

        //act
        vm.onDismiss()

        //assert
        assertEquals(false, vm.dropdownExpanded.value)
    }

    @Test
    fun onAddGroupShouldAddNewNodeToCurrentNodeAndUpdateCurrentNode() {
        //arrange
        vm.tree = getSimpleTree()

        // preassert
        assertEquals((vm.tree.currentNode.value.data as FileSystemObject).name, "Series A")

        //act
        vm.onAddGroup()

        //assert
        assertEquals((vm.tree.currentNode.value.data as FileSystemObject).name, "")
    }

    @Test
    fun onBackShouldSetCurrentNodeToCurrentNodesParentNode() {
        //arrange
        vm.tree = getSimpleTree()
        val expected = vm.tree.currentNode.value.parentNode
        val pn = expected as Node

        assertEquals((vm.tree.currentNode.value.data as FileSystemObject).name, "Series A")
        assertEquals(2, pn.nodes.size)

        //act
        vm.onDelete()

        //assert
        assertEquals(vm.tree.currentNode.value, expected)
        assertEquals(1, pn.nodes.size)
    }

    @Test
    fun onCheckClickedShouldAddNodeToTree() {
        //arrange
        vm.tree = getSimpleTree()
        val expected = vm.tree.currentNode.value.parentNode
        val fso = vm.tree.currentNode.value.data as FileSystemObject
        assertEquals("Series A", fso.name)

        //act
        fso.name = "Not Series A"
        vm.onCheckClicked()

        //assert
        val cn = vm.tree.currentNode.value as Node
        val sa = cn.nodes.first() as Node
        val sad = sa.data as FileSystemObject
        assertEquals("Not Series A", sad.name)
    }

    @Test
    fun onFileSystemObjectClickedShouldUpdateCurrentNode() {
        //arrange
        vm.tree = getSimpleTree()
        val n = vm.tree.currentNode.value as Node
        val expected = n.nodes.first()

        // act
        vm.onFileSystemObjectClicked(expected)

        //assert
        assertEquals(expected, vm.tree.currentNode.value)
    }
}