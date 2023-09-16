package kjvonly.memory.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kjvonly.core.services.NavigationService
import kjvonly.core.ui.theme.DarkBibleTheme
import kjvonly.memory.data.MemoryRepository
import kjvonly.memory.data.models.*
import kjvonly.memory.data.models.File
import kjvonly.memory.data.models.FileSystemObject
import kjvonly.memory.data.models.INode
import kjvonly.memory.data.models.Node
import kjvonly.memory.data.models.Range
import kjvonly.memory.data.models.Ranges
import kjvonly.memory.data.models.Tree


enum class Displays {
    Tree, Plan, TreeFromPlan
}

@Composable
fun Display(dvm: MemoryCreateDisplayManagerViewModel = hiltViewModel(), gvm: MemoryGroupCreateViewModel = hiltViewModel(), pvm: MemoryPlanCreateViewModel = hiltViewModel()) {
    if (dvm.selectedScreen.value == Displays.Tree) {
        MemoryGroupCreateView()
    } else if (dvm.selectedScreen.value == Displays.Plan) {
        MemoryPlanCreateView(NodeMode.Create, "")
    }   else if (dvm.selectedScreen.value == Displays.TreeFromPlan) {
        (gvm.tree.currentNode.value.data as? File)?.name = pvm.text.value
        (gvm.tree.currentNode.value.data as? File)?.rangesId = pvm.memoryRangesId
        pvm.clear()
        gvm.tree.prev()
        dvm.selectedScreen.value = Displays.Tree
    }
}

@Composable
fun MemoryGroupCreateViewWithName(
    name: String,
    viewModel: MemoryGroupCreateViewModel = hiltViewModel()
) {
    val a = viewModel.tree.currentNode.value.data as? FileSystemObject
    a?.name = name
    viewModel.tree.currentNode.value = viewModel.tree.currentNode.value.parentNode
    GroupCreate(viewModel)
}


@Composable
fun MemoryGroupCreateView(viewModel: MemoryGroupCreateViewModel = hiltViewModel()) {
    GroupCreate(viewModel)
}

@Composable
fun GroupCreate(
    viewModel: MemoryGroupCreateViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = rememberScaffoldState(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val m = Modifier.size(35.dp)

                    Icon(
                        modifier = Modifier.clickable { viewModel.onDelete() },
                        imageVector = Icons.Filled.Delete, contentDescription = ""
                    )

                    Text(
                        text = "Create Group".uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                    Icon(modifier = Modifier.clickable {
                        viewModel.onCheckClicked()
                    }, imageVector = Icons.Filled.Check, contentDescription = "")
                }
            }
        },
        content = {
            it
            GroupList(node = viewModel.tree.currentNode.value)
        },
        floatingActionButton = {
            GroupCreateFloatingActionButton(
                viewModel.dropdownExpanded.value,
            )
        }
    )
}

@Composable
fun GroupList(node: INode) {
    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            val g = node.data as FileSystemObject
            GroupName(g)
            val n = node as Node
            GroupListListViewModel(n.nodes)
        }
    }
}

@Composable
fun GroupListListViewModel(
    list: List<INode>,
    viewModel: MemoryGroupCreateViewModel = hiltViewModel()
) {
    GroupListList(list, viewModel)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupListList(list: List<INode>, viewModel: MemoryGroupCreateViewModel) {
    LazyColumn {
        // on below line we are setting data for each item of our listview.
        itemsIndexed(list) { index, item ->
            val context = LocalContext.current
            val data = item.data as? FileSystemObject

            // on below line we are creating a card for our list view item.
            Card(
                onClick = {
                    viewModel.onFileSystemObjectClicked(item)
                    // inside on click we are displaying the toast message.
                    Toast.makeText(
                        context,
                        "$item clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                // on below line we are adding
                // padding from our all sides.
                modifier = Modifier.padding(8.dp),

                // on below line we are adding
                // elevation for the card.
                elevation = 6.dp
            )
            {
                Box {
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(data?.name.orEmpty(), modifier = Modifier.fillMaxWidth())
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.clickable {
                                //  onDeleteNode(item.id)
                            },
                            imageVector = Icons.Filled.Delete,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GroupName(g: FileSystemObject) {
    var text by remember { mutableStateOf(g.name) }
    if (g.name != text) {
        text = g.name
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            modifier = Modifier
                .padding(end = 10.dp)
                .align(alignment = Alignment.CenterVertically),
            text = "Name:".uppercase(),
            style = MaterialTheme.typography.h6
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
                g.name = it
            },
            textStyle = MaterialTheme.typography.h6,

            placeholder = {
                Text(
                    text = "Topical Memory Course",
                    style = MaterialTheme.typography.h6,
                )
            },
            maxLines = 1,
            enabled = true,
        )
    }
}

@Composable
fun GroupCreateFloatingActionButton(
    dropdownExpanded: Boolean,
    viewModel: MemoryGroupCreateViewModel = hiltViewModel(),
) {

    FloatingActionButton(
        modifier = Modifier.padding(bottom = 25.dp),
        onClick = {
            viewModel.onExpanded()
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSurface,
    ) {
        Icon(Icons.Filled.Add, "Action Button")
        GroupCreateFloatingActionButtonDropdownMenu(
            dropdownExpanded,
            viewModel,
        )
    }
}

@Composable

fun GroupCreateFloatingActionButtonDropdownMenu(
    expanded: Boolean,
    viewModel: MemoryGroupCreateViewModel = hiltViewModel(),
    vm: MemoryCreateDisplayManagerViewModel = hiltViewModel()
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            viewModel.onDismiss()
        }
    ) {
        DropdownMenuItem(onClick = { viewModel.onAddGroup() }) {
            Text("Add Group")
        }
        DropdownMenuItem(onClick = { viewModel.onAddVerses(vm) }) {
            Text("Add Verses")
        }
    }
}


fun mutableStringState(): MutableState<String> {
    return mutableStateOf("blah")
}


/*
*
* where my preview code starts
*
* */

class mockNavSvc : NavigationService {
    override fun navigate(module: String, dest: String) {
        TODO("Not yet implemented")
    }

    override fun add(module: String, nc: NavController) {
        TODO("Not yet implemented")
    }

    override fun back(module: String) {
        TODO("Not yet implemented")
    }

}

class mockMr : MemoryRepository {
    override fun deleteMemoryRangesById(planId: String) {

    }

    override fun deleteNodeRoot(id: String) {

    }

    override fun saveTree(tree: Tree) {

    }

    override fun getTrees(): List<Tree> {
        TODO("Not yet implemented")
    }

    override fun saveRanges(r: Ranges) {

    }

    override fun saveRange(ranges: List<Range>) {

    }

}

@Preview
@Composable
fun GroupCrateRorPreview() {
    var nsMock: NavigationService = mockNavSvc()
    var mrMock: MemoryRepository = mockMr()
    val tree = getSimpleTree()
    val g = tree.currentNode.value.data as FileSystemObject
    val name = mutableStringState()
    name.value = g.name
    val vm = MemoryGroupCreateViewModel(nsMock, mrMock)
    vm.tree = tree
    DarkBibleTheme() {
        GroupCreate(vm)
    }
}
