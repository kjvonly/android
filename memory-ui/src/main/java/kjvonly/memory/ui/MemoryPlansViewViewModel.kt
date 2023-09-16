package kjvonly.memory.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kjvonly.bible.services.FontSizeService
import kjvonly.core.services.NavigationService
import kjvonly.memory.data.MemoryRepository
import kjvonly.memory.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kjvonly.memory.data.models.INode
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.Tree
import javax.inject.Inject

 enum class  ViewDisplays{
     Tree, Node
 }

@HiltViewModel
class MemoryPlansViewViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val navigationService: NavigationService,
    fontSizeService: FontSizeService,
    private val memoryRepository: MemoryRepository,
) : ViewModel() {
    val trees = mutableStateListOf<Tree>()
    val fontSize = mutableStateOf(30)
    val selectedTree = mutableStateOf(Tree())
    val display = mutableStateOf(ViewDisplays.Tree)

    init {
        val t = Tree()
        t.id = "EmptyTree"
        selectedTree.value = t
        viewModelScope.launch(Dispatchers.IO) {
            val n = memoryRepository.getTrees()

            viewModelScope.launch(Dispatchers.Main) {
                trees.addAll(n)
            }
        }

        fontSizeService.subscribe {
            fontSize.value = it
        }
    }

    fun back() {
        this.navigationService.back("memory")
    }


    fun navigateBack() {
        if (selectedTree.value.id == "EmptyTree"){
            back()
        } else if (selectedTree.value.currentNode.value == selectedTree.value.rootNode){
            display.value = ViewDisplays.Tree
            val t = Tree()
            t.id = "EmptyTree"
            t.currentNode.value = Node("EmptyNode", "")
            selectedTree.value = t
        } else {
            selectedTree.value.prev()
        }
    }

    fun onCancelJob(id: String){

    }
    fun onEdit(id: String){}
    fun onClick(inode: INode){
        selectedTree.value.next(inode as Node)

    }
    fun onDelete(inode: INode){}
    fun onTreeClicked(item: Tree) {
        selectedTree.value = item
        display.value = ViewDisplays.Node
    }

}

/*

    private val rootOfRoot = RootOfRootNode("rootOfRoot", "Memory Groups", NodeType.RootOfRoot)

    lateinit var rootNode: IRootNode
    var selectedNode: MutableState<IParentNode> = mutableStateOf(EmptyParentNode())
    var selectedNodeChildNodes = mutableStateListOf<INode>()

    val jobs = HashMap<String, Job>()
    var fontSize = 30

    fun back() {
        this.navigationService.back("memory")
    }

    fun navigate(id: String) {
        navigationService.navigate("memory", "memoryPlanView/$id")
    }

    fun onEdit(id: String) {
        navigationService.navigate("memory", "memoryPlanEditView/$id")
    }

    fun removeNode(n: IParentNode, nodeId: String) {
        val i = n.nodes.iterator()
        while (i.hasNext()) {
            val it = i.next()
            if (it.id == nodeId) {
                i.remove()
                return
            }
        }
    }

    fun deleteAllPlans(childNode: INode) {
        val pn = childNode as? IParentNode
        if (pn != null) {
            for (n in pn.nodes) {
                deleteAllPlans(n)
            }
        }

        val ln = childNode as? LeafNode
        if (ln != null) {
            memoryRepository.deleteMemoryPlan(ln.planId)
        }
    }

    fun onDeleteNode(childNode: INode) {
        removeNode(selectedNode.value, childNode.id)
        deleteAllPlans(childNode)
        val rn = childNode as? RootNode
        if (rn != null) {
            memoryRepository.deleteNodeRoot(rn.id)
        }
        memoryRepository.updateNode(rootNode)
        onSelectNode(selectedNode.value)
    }

    fun onDelete(childNode: INode) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            delay(5000)

            // Only on RootOfRoot list
            if (childNode.type == NodeType.Root) {
                rootNode =
                    memoryRepository.getNode(childNode.id).getOrDefault(EmptyRootNode())
                onDeleteNode(rootNode)
            } else {
                onDeleteNode(childNode)
            }
        }
        jobs[childNode.id] = job
    }

    fun cancelJob(planId: String) {
        val j = jobs[planId] ?: return
        j.cancel()
        jobs.remove(planId)
    }

    fun updateRootNode(n: IParentNode) {
        val rn = n as? IRootNode
        if (rn != null) {
            rootNode = rn
        }
    }

    suspend fun getNode(id: String) {
        rootNode = memoryRepository.getNode(id).getOrDefault(EmptyRootNode())
        onSelectNode(rootNode)
    }

    private fun onSelectNode(flattenNode: IParentNode) {
        updateRootNode(flattenNode)
        selectedNode.value = flattenNode
        selectedNodeChildNodes.clear()
        selectedNodeChildNodes.addAll(selectedNode.value.nodes)
    }

    fun onNodeClick(childNode: INode) {
        val ln = childNode as? LeafNode
        if (ln != null) {
            navigate(ln.planId)
            return
        }

        val nn = childNode as? Node
        if (nn != null) {
            onSelectNode(nn)
            return
        }

        val rn = childNode as? RootNode
        if (rn != null) {
            viewModelScope.launch { getNode(rn.id) }
            return
        }
    }

    fun navigateBack() {
        val sn = selectedNode.value as? Node
        if (sn != null) {
            onSelectNode(sn.parentNode)
            return
        }
        val srn = selectedNode.value as? RootNode
        if (srn != null) {
            onSelectNode(rootOfRoot)
            return
        }

        val rrn = selectedNode.value as? RootOfRootNode
        if (rrn != null) {
            back()
            return
        }

    }

    init {
        // TODO push dispatcher to rep
        viewModelScope.launch(Dispatchers.IO) {
            val n = memoryRepository.getTree()
            for (i in n) {
                i.parentNode = rootOfRoot
                rootOfRoot.nodes.add(i)
            }

            viewModelScope.launch(Dispatchers.Main) {
                onSelectNode(rootOfRoot)
            }
        }

        fontSizeService.subscribe {
            fontSize = it
        }
    }


}*/