package kjvonly.memory.ui.old
//
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import kjvonly.bible.data.models.EmptyVerse
//import kjvonly.bible.data.models.Word
//import kjvonly.memory.data.models.*
//
//enum class MemoryPlanViewScreen {
//    Home, Book, Chapter, Verse
//}
//
//@Composable
//fun MemoryPlanView(planId: String, viewModel: MemoryPlanViewViewModel = hiltViewModel()) {
//    val context = LocalContext.current
//    Toast.makeText(
//        context,
//        "$planId passed",
//        Toast.LENGTH_SHORT
//    ).show()
//
//    viewModel.getMemoryPlan(planId)
//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    var selectedScreen by remember {
//        mutableStateOf(MemoryPlanViewScreen.Home)
//    }
//
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .statusBarsPadding()
//            .navigationBarsPadding(),
//        scaffoldState = rememberScaffoldState(),
//        topBar = {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//
//
//                ) {
//                    val m = Modifier.size(35.dp)
//                    Box {
//
//                        Icon(
//                            modifier = m.clickable {
//                                when (viewModel.selectedRange.value.memoryRangeType) {
//                                    MemoryRangeTypes.Book -> {
//                                        when (viewModel.bookStep.value) {
//                                            BookSteps.Books -> viewModel.selectedRange.value =
//                                                EmptyMemoryRange()
//                                            BookSteps.Chapters -> {
//                                                viewModel.bookStep.value = BookSteps.Books
//                                            }
//                                            BookSteps.Verses -> {
//                                                viewModel.bookStep.value = BookSteps.Chapters
//                                            }
//                                            BookSteps.Memory -> {
//                                                viewModel.memoryEntity.value = EmptyMemoryEntity
//                                                viewModel.bookStep.value = BookSteps.Verses
//                                            }
//                                        }
//                                    }
//                                    MemoryRangeTypes.Chapter -> {
//                                        when (viewModel.chaptersStep.value) {
//                                            ChaptersSteps.Chapters -> {
//                                                viewModel.selectedRange.value =
//                                                    EmptyMemoryRange()
//                                            }
//                                            ChaptersSteps.Verses -> {
//                                                viewModel.chaptersStep.value =
//                                                    ChaptersSteps.Chapters
//                                            }
//                                            ChaptersSteps.Memory -> {
//                                                viewModel.memoryEntity.value = EmptyMemoryEntity
//                                                viewModel.chaptersStep.value =
//                                                    ChaptersSteps.Verses
//                                            }
//                                        }
//                                    }
//
//                                    MemoryRangeTypes.Verse -> {
//                                        when (viewModel.versesStep.value) {
//                                            VersesSteps.Verses -> {
//                                                viewModel.selectedRange.value =
//                                                    EmptyMemoryRange()
//                                            }
//                                            VersesSteps.Memory -> {
//                                                viewModel.memoryEntity.value = EmptyMemoryEntity
//                                                viewModel.versesStep.value =
//                                                    VersesSteps.Verses
//                                            }
//                                        }
//                                    }
//                                    MemoryRangeTypes.Empty -> viewModel.back()
//                                }
//
//                            }, imageVector = Icons.Filled.ArrowBack, contentDescription = ""
//                        )
//
//                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//                            when (viewModel.selectedRange.value.memoryRangeType) {
//                                MemoryRangeTypes.Empty -> {
//                                    if (viewModel.memoryPlan.value.id != "0") {
//                                        Text(
//                                            text = viewModel.memoryPlan.value.name.uppercase(),
//                                            textAlign = TextAlign.Center,
//                                            fontSize = 25.sp
//                                        )
//                                    }
//                                }
//                                else -> {
//
//                                    Text(
//                                        text = viewModel.getMemoryRangeText(viewModel.selectedRange.value.getText()),
//                                        textAlign = TextAlign.Center,
//                                        fontSize = 25.sp
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        },
//        content = {
//            it
//            when (viewModel.selectedRange.value.memoryRangeType) {
//                MemoryRangeTypes.Empty -> HomeContent(viewModel = viewModel)
//                MemoryRangeTypes.Book -> BookContent(viewModel)
//                MemoryRangeTypes.Chapter -> {
//
//                    ChapterContent(viewModel)
//                }
//                MemoryRangeTypes.Verse -> {
//                    VerseContent(viewModel = viewModel)
//                }
//
//            }
//        },
//        floatingActionButton = {}
//    )
//}
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun HomeContent(viewModel: MemoryPlanViewViewModel) {
//    val context = LocalContext.current
//    Surface {
//        Column() {
//            LazyColumn {
//                // on below line we are setting data for each item of our listview.
//                itemsIndexed(viewModel.memoryPlan.value.entities) { index, item ->
//                    // on below line we are creating a card for our list view item.
//                    Card(
//                        // inside our grid view on below line
//                        // we are adding on click for each item of our grid view.
//                        onClick = {
//                            // inside on click we are displaying the toast message.
//                            Toast.makeText(
//                                context,
//                                "${viewModel.getMemoryRangeText(item.getText())} clicked",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            viewModel.selectedRange.value = item
//                        },
//                        // on below line we are adding
//                        // padding from our all sides.
//                        modifier = Modifier.padding(8.dp),
//
//                        // on below line we are adding
//                        // elevation for the card.
//                        elevation = 6.dp
//                    )
//                    {
//                        // on below line we are creating
//                        // a row for our list view item.
//                        Row(
//                            // for our row we are adding modifier
//                            // to set padding from all sides.
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//                            // on below line inside row we are adding spacer
//                            Spacer(modifier = Modifier.width(5.dp))
//
//                            // on below line we are adding image to display the image.
//                            // on the below line we are creating a text.
//                            var t = ""
//                            when (item.memoryRangeType) {
//                                MemoryRangeTypes.Empty -> {
//                                    t = "Empty"
//                                }
//                                MemoryRangeTypes.Book -> {
//                                    t = "Book"
//                                }
//                                MemoryRangeTypes.Chapter -> {
//                                    t = "Chapter"
//                                }
//                                MemoryRangeTypes.Verse -> {
//                                    t = "Verse"
//                                }
//                            }
//
//
//                            Text(
//                                // inside the text on below line we are
//                                // setting text as the language name
//                                // from our modal class.
//                                text = t,
//
//                                // on below line we are adding padding
//                                // for our text from all sides.
//                                modifier = Modifier.padding(4.dp),
//
//                                // on below line we are adding color for our text
//                                color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center
//                            )
//
//                            // on below line we are adding spacer between image and a text
//                            Spacer(modifier = Modifier.width(5.dp))
//                            val t2 = viewModel.getMemoryRangeText(item.getText())
//                            // on the below line we are creating a text.
//                            Text(
//                                // inside the text on below line we are
//                                // setting text as the language name
//                                // from our modal class.
//                                text = t2,
//
//                                // on below line we are adding padding
//                                // for our text from all sides.
//                                modifier = Modifier.padding(4.dp),
//
//                                // on below line we are adding color for our text
//                                color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun RangeList(
//    viewModel: MemoryPlanViewViewModel,
//    range: List<String>,
//    onClick: (Int) -> Unit
//) {
//    val context = LocalContext.current
//    Surface {
//        Column() {
//            LazyColumn {
//                // on below line we are setting data for each item of our listview.
//                itemsIndexed(range) { index, item ->
//                    // on below line we are creating a card for our list view item.
//                    Card(
//                        // inside our grid view on below line
//                        // we are adding on click for each item of our grid view.
//                        onClick = {
//                            onClick(index)
//                            // inside on click we are displaying the toast message.
//                            Toast.makeText(
//                                context,
//                                "$item clicked",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        },
//                        // on below line we are adding
//                        // padding from our all sides.
//                        modifier = Modifier.padding(8.dp),
//
//                        // on below line we are adding
//                        // elevation for the card.
//                        elevation = 6.dp
//                    )
//                    {
//                        // on below line we are creating
//                        // a row for our list view item.
//                        Row(
//                            // for our row we are adding modifier
//                            // to set padding from all sides.
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//                            // on below line inside row we are adding spacer
//                            Spacer(modifier = Modifier.width(5.dp))
//
//                            // on below line we are adding image to display the image.
//                            // on the below line we are creating a text.
//                            var t = "$item"
////                            when (item.memoryRangeType) {
////                                MemoryRangeTypes.Empty -> {
////                                    t = "Empty"
////                                }
////                                MemoryRangeTypes.Book -> {
////                                    t = "Book"
////                                }
////                                MemoryRangeTypes.Chapter -> {
////                                    t = "Chapter"
////                                }
////                                MemoryRangeTypes.Verse -> {
////                                    t = "Verse"
////                                }
////                            }
//
//
//                            Text(
//                                // inside the text on below line we are
//                                // setting text as the language name
//                                // from our modal class.
//                                text = t,
//
//                                // on below line we are adding padding
//                                // for our text from all sides.
//                                modifier = Modifier.padding(4.dp),
//
//                                // on below line we are adding color for our text
//                                color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center
//                            )
//
//                            // on below line we are adding spacer between image and a text
//                            Spacer(modifier = Modifier.width(5.dp))
//                            val t2 = "$item"
//                            // on the below line we are creating a text.
//                            Text(
//                                // inside the text on below line we are
//                                // setting text as the language name
//                                // from our modal class.
//                                text = t2,
//
//                                // on below line we are adding padding
//                                // for our text from all sides.
//                                modifier = Modifier.padding(4.dp),
//
//                                // on below line we are adding color for our text
//                                color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun VerseContent(viewModel: MemoryPlanViewViewModel) {
//
//
//    when (viewModel.versesStep.value) {
//
//        VersesSteps.Verses -> {
//            val bookId = viewModel.selectedRange.value.getBooks().start
//            val bookName =
//                viewModel.bookNames.value.shortNames[bookId]
//            val chapterId = viewModel.selectedRange.value.getChapters().start
//            val text = "$bookName $chapterId"
//
//
//            val r = mutableListOf<String>()
//            (viewModel.selectedRange.value.start..viewModel.selectedRange.value.end).forEach {
//                r.add("$text $it")
//            }
//
//            RangeList(viewModel, r, onClick = {
//                viewModel.selectedVerseStep.value =
//                    (viewModel.selectedRange.value.start..viewModel.selectedRange.value.end).toList()[it]
//                viewModel.versesStep.value = VersesSteps.Memory
//            })
//
//        }
//        VersesSteps.Memory -> {
//            val textId =
//                "${viewModel.selectedRange.value.getBooks().start}_${viewModel.selectedRange.value.getChapters().start}_${viewModel.selectedVerseStep.value}"
//            if (viewModel.memoryEntity.value.verse.words.isEmpty()) {
//                Log.i("KJVonly", "${viewModel.memoryEntity.value.verse.words.size}")
//                viewModel.getVerse(textId)
//            }
//            MemoryEntityContent(viewModel = viewModel)
//        }
//    }
//}
//
//
//@Composable
//fun ChapterContent(viewModel: MemoryPlanViewViewModel) {
//
//
//    when (viewModel.chaptersStep.value) {
//        ChaptersSteps.Chapters -> {
//            val text = viewModel.getMemoryRangeText(
//                viewModel.selectedRange.value.getBooks().getText()
//            )
//
//            val r = mutableListOf<String>()
//            (viewModel.selectedRange.value.start..viewModel.selectedRange.value.end).forEach {
//                r.add("$text $it")
//            }
//
//            RangeList(viewModel, r, onClick = {
//                val list =
//                    (viewModel.selectedRange.value.start..viewModel.selectedRange.value.end).toList()
//                viewModel.selectedChapterStep.value = list[it]
//                viewModel.chaptersStep.value = ChaptersSteps.Verses
//            })
//        }
//        ChaptersSteps.Verses -> {
//            val bookName =
//                viewModel.bookNames.value.shortNames[viewModel.selectedRange.value.getBooks().start]
//            val text = "$bookName ${viewModel.selectedChapterStep.value}"
//            val bookId = viewModel.selectedRange.value.getBooks().start
//            val chapterId = viewModel.selectedRange.value.getChapters().start
//            val verseMax =
//                viewModel.bookNames.value.bookChapterVerseCountById[bookId]?.get(chapterId)
//            if (verseMax != null) {
//                val r = mutableListOf<String>()
//                (1..verseMax).forEach {
//                    r.add("$text $it")
//                }
//
//                RangeList(viewModel, r, onClick = {
//                    viewModel.selectedChapterVerseStep.value = it + 1
//                    viewModel.chaptersStep.value = ChaptersSteps.Memory
//                })
//            }
//        }
//        ChaptersSteps.Memory -> {
//            val textId =
//                "${viewModel.selectedRange.value.getBooks().start}_${viewModel.selectedChapterStep.value}_${viewModel.selectedChapterVerseStep.value}"
//            if (viewModel.memoryEntity.value.verse.words.isEmpty()) {
//                viewModel.getVerse(textId)
//            }
//            MemoryEntityContent(viewModel = viewModel)
//        }
//    }
//
//}
//
//@Composable
//fun BookContent(viewModel: MemoryPlanViewViewModel) {
//
//    when (viewModel.bookStep.value) {
//        BookSteps.Books -> {
//            var indexStart = 0
//            var indexEnd = 0
//            viewModel.books.forEachIndexed { index, book ->
//                if (book.id == viewModel.selectedRange.value.start) {
//                    indexStart = index
//                }
//                if (book.id == viewModel.selectedRange.value.end) {
//                    indexEnd = index
//                }
//            }
//
//            val r = mutableListOf<String>()
//            (indexStart..indexEnd).forEach {
//                var book = viewModel.books[it]
//                val bookName = viewModel.bookNames.value.shortNames[book.id]
//                if (bookName != null) {
//                    r.add(bookName)
//                }
//            }
//
//            RangeList(viewModel, r, onClick = { indexSelected ->
//                val bookIndex = (indexStart..indexEnd).toList()[indexSelected]
//                val book = viewModel.books[bookIndex]
//
//                viewModel.selectedBookStep.value = book
//                viewModel.bookStep.value = BookSteps.Chapters
//            })
//        }
//        BookSteps.Chapters -> {
//            val bookName = viewModel.bookNames.value.shortNames[viewModel.selectedBookStep.value.id]
//            val text = bookName
//
//            val chapterList =
//                viewModel.bookNames.value.bookChapterVerseCountById[viewModel.selectedBookStep.value.id]
//            var maxChapter = chapterList?.keys?.size
//            if (maxChapter == null) {
//                maxChapter = 1
//            }
//
//            val r = mutableListOf<String>()
//            (1..maxChapter).forEach {
//                r.add("$text $it")
//            }
//
//            RangeList(viewModel, r, onClick = {
//                val list =
//                    (1..maxChapter).toList()
//                viewModel.selectedBookChapterStep.value = list[it]
//                viewModel.bookStep.value = BookSteps.Verses
//            })
//        }
//        BookSteps.Verses -> {
//            val bookName =
//                viewModel.bookNames.value.shortNames[viewModel.selectedRange.value.getBooks().start]
//            val text = "$bookName ${viewModel.selectedBookChapterStep.value}"
//            val bookId = viewModel.selectedRange.value.getBooks().start
//            val chapterId = viewModel.selectedRange.value.getChapters().start
//            val verseMax =
//                viewModel.bookNames.value.bookChapterVerseCountById[bookId]?.get(chapterId)
//            if (verseMax != null) {
//                val r = mutableListOf<String>()
//                (1..verseMax).forEach {
//                    r.add("$text $it")
//                }
//
//                RangeList(viewModel, r, onClick = {
//                    viewModel.selectedBookChapterVerseStep.value = it + 1
//                    viewModel.bookStep.value = BookSteps.Memory
//
//                })
//            }
//        }
//        BookSteps.Memory -> {
//            val textId =
//                "${viewModel.selectedRange.value.getBooks().start}_${viewModel.selectedBookChapterStep.value}_${viewModel.selectedBookChapterVerseStep.value}"
//
//            if (viewModel.memoryEntity.value.verse.words.isEmpty()) {
//                viewModel.getVerse(textId)
//            }
//            MemoryEntityContent(viewModel = viewModel)
//        }
//    }
//}
//
//@Composable
//fun MemoryEntityContent(viewModel: MemoryPlanViewViewModel) {
//    Surface(Modifier.statusBarsPadding()) {
//        MemoryVerseView(viewModel = viewModel, memoryEntity = viewModel.memoryEntity.value)
//    }
//}
//
//@Composable
//fun MemoryVerseView(viewModel: MemoryPlanViewViewModel, memoryEntity: IMemoryEntity) {
//    Card (Modifier.padding(10.dp)){
//        Column(Modifier.fillMaxWidth()) {
//            if (viewModel.memoryEntity.value.verse.words.isNotEmpty()) {
//                val bookName =
//                    viewModel.bookNames.value.shortNames[viewModel.selectedRange.value.getBooks().start]
//                val chapterId = viewModel.selectedRange.value.getChapters().start
//                Row {
//                    Text(text ="$bookName $chapterId", style = MaterialTheme.typography.h6, fontSize = viewModel.fontSize.sp)
//                }
//                Spacer(Modifier.height(10.dp))
//                Row {
//                    Text(viewModel.memoryEntity.value.verse.text, fontSize = viewModel.fontSize.sp)
//                }
//            }
//        }
//    }
//}