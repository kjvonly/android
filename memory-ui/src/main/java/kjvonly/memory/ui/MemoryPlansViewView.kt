package kjvonly.memory.ui

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.memory.data.models.EmptyNode
import kjvonly.memory.data.models.FileSystemObject
import kjvonly.memory.data.models.INode
import kjvonly.memory.data.models.Node
import kotlin.math.roundToInt
import kotlinx.coroutines.launch


@Composable
fun MemoryPlansView(viewModel: MemoryPlansViewViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
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
                    Box {

                        Icon(
                            modifier = m.clickable {
                                viewModel.navigateBack()
                            }, imageVector = Icons.Filled.ArrowBack, contentDescription = ""
                        )

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                            Text(
                                text = "",
                                textAlign = TextAlign.Center,
                                fontSize = 25.sp
                            )
                        }
                    }
                }
            }
        },
        content = {
            it
            when (viewModel.display.value) {
                ViewDisplays.Tree -> MemoryTree(viewModel = viewModel)
                ViewDisplays.Node -> MemoryNodeList(viewModel = viewModel)
            }

        },
        floatingActionButton = {}
    )
}


@Composable
fun MemoryTree(viewModel: MemoryPlansViewViewModel) {

    Surface {
        Column(
            Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            LazyColumn {
                // on below line we are setting data for each item of our listview.
                itemsIndexed(
                    items = viewModel.trees,
                    key = { index, item -> item.id }
                ) { index, item ->
                    Box {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { viewModel.onTreeClicked(item) },
                        ) {
                            Row(Modifier.padding(8.dp)) {
                                Text(
                                    (item.rootNode.data as FileSystemObject).name,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemoryNodeList(
    viewModel: MemoryPlansViewViewModel
) {
    val context = LocalContext.current
    Surface {
        Column(
            Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            LazyColumn {
                // on below line we are setting data for each item of our listview.
                itemsIndexed(
                    items = (viewModel.selectedTree.value.currentNode.value as Node).nodes,
                    key = { index, item -> item.id }
                ) { index, item ->
                    var fso = item.data as FileSystemObject

                    val swipeableState = rememberSwipeableState(0)
                    val sizePx = with(LocalDensity.current) { screenWidth.dp.toPx() }
                    val anchors = mapOf(0f to 0, -sizePx to 1)

                    Box(
                        modifier = Modifier
                            .swipeable(
                                state = swipeableState,
                                anchors = anchors,
                                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                                orientation = Orientation.Horizontal
                            )
                    ) {
                        Box() {
                            if (swipeableState.isAnimationRunning) {
                                DisposableEffect(Unit) {
                                    onDispose {
                                        if (swipeableState.currentValue == 1) {
                                            Toast.makeText(
                                                context,
                                                "Deleting ${fso.name}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.onDelete(item)
                                        }
                                    }
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .offset {
                                        var offset = 0
                                        if (swipeableState.offset.value.roundToInt() >= -screenWidth) {
                                            offset =
                                                screenWidth + swipeableState.offset.value.roundToInt()
                                        }

                                        IntOffset(offset, 0)
                                    }
                                    .padding(12.dp)
                                    .background(MaterialTheme.colors.error),
                                elevation = 6.dp
                            ) {
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.error)
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val scope = rememberCoroutineScope()
                                        Icon(
                                            modifier = Modifier.clickable {
                                            },
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = ""
                                        )
                                        Text(modifier = Modifier.clickable {
                                            Toast.makeText(
                                                context,
                                                "Restoring ${fso.name} ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.onCancelJob(item.id)
                                            scope.launch {
                                                swipeableState.animateTo(0, tween(2000))
                                            }
                                        }, text = "UNDO")
                                    }
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(swipeableState.offset.value.roundToInt(), 0)
                                    }
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                onClick = {
                                    viewModel.onClick(item)
                                },
                            ) {
                                Box {
                                    Box {
                                        Row(
                                            Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = fso.name,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }

                                    if (item.parentNode is EmptyNode) {
                                        Box {
                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    modifier = Modifier.clickable {
                                                        viewModel.onEdit(item.id)
                                                    },
                                                    imageVector = Icons.Filled.Edit,
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
