package kjvonly.memory.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.feature.ui.layout.base.ChapterDropdown
import kjvonly.feature.ui.layout.base.VersesDropdown
import kjvonly.feature.ui.layout.base.booksDropdown

enum class NodeMode {
    Create, Edit
}

@Composable
fun MemoryPlanCreateView(
    mode: NodeMode,
    id: String = "",
    viewModel: MemoryPlanCreateViewModel = hiltViewModel(),
    displayVm: MemoryCreateDisplayManagerViewModel = hiltViewModel(),
    groupCreateViewModel: MemoryGroupCreateViewModel = hiltViewModel()
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
                        modifier = m.clickable {},
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                    Text(
                        text = "Add Verses".uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                    Icon(modifier = m.clickable {
                        viewModel.save(displayVm, groupCreateViewModel)
                    }, imageVector = Icons.Filled.Check, contentDescription = "")
                }
            }
        },
        content = {
            it
            MemoryPlanContent(viewModel)
        },
        floatingActionButton = {}
    )
}

@Composable
fun MemoryPlanContent(
    viewModel: MemoryPlanCreateViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
    )
    {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            Text(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(alignment = Alignment.CenterVertically),
                text = "Name:".uppercase(),
                style = MaterialTheme.typography.h6
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.text.value,
                onValueChange = {
                    viewModel.text.value = it
                },
                textStyle = MaterialTheme.typography.h6,
                placeholder = {
                    Text(
                        text = "Book of Proverbs",
                        style = MaterialTheme.typography.h6,
                    )
                },
                maxLines = 1,
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            EntitySelect(viewModel)
        }

        ListMemoryEntities2(
            viewModel.memoryEntities,
            viewModel
        )

        Row(
            Modifier
                .height(30.dp)
                .fillMaxWidth()
        ) {}
    }
}


@Composable
fun EntitySelect(viewModel: MemoryPlanCreateViewModel) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Box {

            Column {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(1000, 150)),
                    exit = fadeOut()
                ) {


                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,

                            ) {
                            SelectionType(viewModel)
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            BookStartSelection(viewModel)
                            Spacer(modifier = Modifier.width(5.dp))
                            if (viewModel.selectedSelectionType.value == SelectionType.Book) {
                                Text("-")
                                Spacer(modifier = Modifier.width(5.dp))
                                BookEndSelection(viewModel)
                            }

                            if (viewModel.selectedSelectionType.value == SelectionType.Chapter ||
                                viewModel.selectedSelectionType.value == SelectionType.Verse
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))
                                ChapterStartSelection(viewModel)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            if (viewModel.selectedSelectionType.value == SelectionType.Chapter) {
                                Text("-")
                                Spacer(modifier = Modifier.width(5.dp))
                                ChapterEndSelection(viewModel)
                            }


                            if (viewModel.selectedSelectionType.value == SelectionType.Verse) {
                                Text(":")
                                Spacer(modifier = Modifier.width(5.dp))
                                VerseStartSelection(viewModel)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("-")
                                Spacer(modifier = Modifier.width(5.dp))
                                VerseEndSelection(viewModel)
                            }
                        }

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val mContext = LocalContext.current
                            OutlinedButtonWrapper(
                                modifier = Modifier,
                                onClick = {
                                    viewModel.addRange()
                                    Toast.makeText(
                                        mContext,
                                        "Added: ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                borderStroke = BorderStroke(
                                    2.dp,
                                    MaterialTheme.colors.primary
                                )
                            ) {
                                Text("Add")
                            }
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun SelectionType(vm: MemoryPlanCreateViewModel) {

    Column {
        Row {
            SelectWrapper(0, "Range") {
                OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onSelectionTypeExpanded) {
                    when (vm.selectedSelectionType.value) {
                        SelectionType.Book -> Text("Book")
                        SelectionType.Chapter -> Text("Chapter")
                        SelectionType.Verse -> Text("Verse")
                    }
                }
            }
        }
        Row {
            DropdownMenu(
                expanded = vm.selectionTypeExpanded.value,
                onDismissRequest = vm::onSelectionTypeDismiss,
            ) {
                for (b in listOf("Book", "Chapter", "Verse")) {
                    DropdownMenuItem(onClick = { vm.onSelectedSelectionType(b) }) {
                        Column {
                            Text(
                                modifier = Modifier.padding(
                                    start = 5.dp,
                                    bottom = 5.dp,
                                    top = 5.dp
                                ),
                                text = b,
                                fontSize = 30.sp
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(2.dp)
                                    .background(MaterialTheme.colors.onSurface)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookStartSelection(vm: MemoryPlanCreateViewModel) {

    Column {
        Row {
            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onBookStartExpanded) {
                Text(vm.selectedStartBookName.value)
            }
        }
        Row {
            booksDropdown(
                books = vm.books,
                onDismiss = vm::onBookStartDismiss,
                onClick = {
                    vm.onBookStartSelected(it)
                    vm.onBookStartDismiss()
                },
                expanded = vm.bookStartExpanded.value
            )
        }
    }
}


@Composable
fun BookEndSelection(vm: MemoryPlanCreateViewModel) {
    Column {
        Row {
            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onBookEndExpanded) {
                Text(vm.selectedEndBookName.value)
            }
        }
        Row {
            booksDropdown(
                books = vm.bookEndRange,
                onDismiss = vm::onBookEndDismiss,
                onClick = {
                    vm.onBookEndSelected(it)
                    vm.onBookEndDismiss()
                },
                expanded = vm.bookEndExpanded.value
            )
        }
    }
}


@Composable
fun ChapterStartSelection(vm: MemoryPlanCreateViewModel) {

    Column {
        Row {
            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onChapterStartExpanded) {
                Text(vm.selectedStartChapterName.value)
            }
        }
        Row {
            ChapterDropdown(
                vm.chapterStartRange,
                vm::onChapterStartDismiss,
                {
                    vm.onChapterStartSelected(it)
                    vm.onChapterStartDismiss()
                },
                vm.chapterStartExpanded.value
            )
        }
    }
}


@Composable
fun ChapterEndSelection(vm: MemoryPlanCreateViewModel) {

    Column {
        Row {
            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onChapterEndExpanded) {
                Text(vm.selectedEndChapterName.value)
            }
        }
        Row {
            ChapterDropdown(
                vm.chapterEndRange,
                vm::onChapterEndDismiss,
                {
                    vm.onChapterEndSelected(it)
                    vm.onChapterEndDismiss()
                },
                vm.chapterEndExpanded.value
            )
        }
    }
}


@Composable
fun VerseStartSelection(vm: MemoryPlanCreateViewModel) {

    Column {
        Row {

            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onVerseStartExpanded) {
                Text(vm.selectedStartVerseName.value)
            }

        }
        Row {
            VersesDropdown(
                vm.verseStartRange,
                vm::onVerseStartDismiss,
                {
                vm.onVerseStartSelected(it)
                    vm.onVerseStartDismiss()
                },
                vm.verseStartExpanded.value
            )
        }
    }
}


@Composable
fun VerseEndSelection(vm: MemoryPlanCreateViewModel) {
    Column {
        Row {
            OutlinedButtonWrapper(modifier = Modifier, onClick = vm::onVerseEndExpanded) {
                Text(vm.selectedEndVerseName.value)
            }
        }
        Row {
            VersesDropdown(
                vm.verseEndRange,
                vm::onVerseEndDismiss,
                {
                    vm.onVerseEndSelected(it)
                    vm.onVerseEndDismiss()
                },
                vm.verseEndExpanded.value
            )
        }
    }
}

@Composable
fun OutlinedButtonWrapper(
    modifier: Modifier,
    onClick: () -> Unit,
    borderStroke: BorderStroke = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
    children: @Composable () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = borderStroke,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface.copy(.1f)
        ),

        ) {
        children()
    }
}

@Composable
fun SelectWrapper(maxWidth: Int, text: String, children: @Composable () -> Unit) {
    var modifier = Modifier.offset(0.dp)
    if (maxWidth != 0) {
        modifier = Modifier
            .width(maxWidth.dp)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier, horizontalArrangement = Arrangement.Center) {
            Text(text.uppercase(), textAlign = TextAlign.Center)
        }
        Row(modifier, horizontalArrangement = Arrangement.Center) {
            children()
        }
    }
}

@Composable
fun ListMemoryEntities2(list: MutableList<String>, viewModel: MemoryPlanCreateViewModel){
    Column(Modifier.verticalScroll(rememberScrollState())){
        for (l in list.reversed()){
            Row(){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    val s = viewModel.getText(l)
                    Text(s)
                }
            }
        }
    }
}