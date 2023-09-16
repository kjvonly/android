package kjvonly.memory.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kjvonly.core.services.NavigationService
import kjvonly.memory.data.MemoryRepository
import kjvonly.memory.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kjvonly.memory.data.models.EmptyNode
import kjvonly.memory.data.models.File
import kjvonly.memory.data.models.Folder
import kjvonly.memory.data.models.INode
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.RootTree
import kjvonly.memory.data.models.Tree
import kjvonly.memory.data.models.randomUUID
import javax.inject.Inject


@HiltViewModel
class MemoryGroupCreateViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val memoryRepository: MemoryRepository

    ) : ViewModel() {

    var tree: Tree = getSimpleTree()

    val dropdownExpanded = mutableStateOf(false)

    fun onExpanded() {
        dropdownExpanded.value = true
    }

    fun onDismiss() {
        dropdownExpanded.value = false
    }

    fun onDeleteChild() {

    }


    fun onDelete() {
        val rn = tree.currentNode.value as? RootTree
        if (rn != null) {
            navigationService.back("memory")
        } else {
            val cn = tree.currentNode.value
            val n = tree.currentNode.value.parentNode as? Node
            if (n != null) {
                val iterator = n.nodes.iterator()
                while (iterator.hasNext()) {
                    val node = iterator.next()
                    if (cn == node){
                        iterator.remove()
                        break
                    }
                }
            }
            tree.currentNode.value = cn.parentNode
        }
    }

    fun onAddGroup() {
        val newNode = Node(randomUUID(),  Folder(""))
        newNode.parentNode = tree.currentNode.value
        val cn = tree.currentNode.value as Node
        cn.add(newNode)
        tree.next(newNode)
    }

    fun onAddVerses(displayVm : MemoryCreateDisplayManagerViewModel) {
        val data = File("", "")
        val n = Node(randomUUID(), data);
        n.parentNode = tree.currentNode.value
        val pn = tree.currentNode.value as? Node
        if (pn != null){
            pn.add(n)
            tree.next(n)
            viewModelScope.launch (Dispatchers.IO){
                memoryRepository.saveTree(tree)
            }
            displayVm.selectedScreen.value = Displays.Plan
        }
    }

    fun onCheckClicked() {
        val rn = tree.currentNode.value as? RootTree
        if (rn != null) {
            viewModelScope.launch(Dispatchers.IO) {
                memoryRepository.saveTree(tree)
                viewModelScope.launch(Dispatchers.Main) {
                navigationService.back("memory")
                }
            }
        } else {
            tree.currentNode.value = tree.currentNode.value.parentNode
        }
    }

    fun onFileSystemObjectClicked(item: INode) {
        tree.currentNode.value = item
    }

    fun onCancel() {
        navigationService.back("memory")
    }
}

fun getSimpleTree(): Tree {
    val tree = Tree()
    tree.id = randomUUID()

    val g = Folder("Topical Memory System")
    val rn = RootTree(randomUUID(),  g);

    val g1 = Folder("Series A")
    val n11 = Node(randomUUID(),  g1)
    val g2 = Folder("Series B")
    val n12 = Node(randomUUID(),  g2)
    val t = File("Christ the Center", "")
    val cn1 = Node(randomUUID(),  t)

    tree.rootNode = rn
    tree.currentNode.value = n11
    rn.parentNode = EmptyNode()
    rn.add(n11)
    rn.add(n12)
    n11.add(cn1)

    return tree
}



@HiltViewModel
class MemoryCreateDisplayManagerViewModel @Inject constructor(): ViewModel(){
    var selectedScreen: MutableState<Displays> = mutableStateOf(Displays.Tree)
}