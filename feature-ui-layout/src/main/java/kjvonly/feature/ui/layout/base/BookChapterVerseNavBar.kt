package kjvonly.feature.ui.layout.base

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

import kjvonly.bible.data.di.FakeBibleRepository
import kjvonly.core.ui.theme.BibleTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.*
import kjvonly.bible.data.models.Book
import kjvonly.bible.services.BookNamesServiceImpl
import kjvonly.bible.services.BookNavServiceImpl


@Composable
fun BookChapterVerseNavBar(
    viewModel: BookChapterVerseNavBarViewModel = hiltViewModel(),
) {

    Column {
        Row {
            navRow(viewModel)
            dropdowns(viewModel)
        }
    }
}

@Composable
fun navRow(viewModel: BookChapterVerseNavBarViewModel) {
    Column() {

        Row(Modifier) {
            val modifier = Modifier.padding(3.dp)
            OutlinedButton(
                modifier = modifier,
                onClick = {
                    viewModel.booksExpanded.value = true
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.onSurface,
                    backgroundColor = MaterialTheme.colors.surface.copy(.1f)
                ),

                ) {
                Text(viewModel.selectedBook.value.name)
            }

            OutlinedButton(
                modifier = modifier, onClick = {
                    viewModel.chapterExpanded.value = true
                }, shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.onSurface,
                    backgroundColor = MaterialTheme.colors.surface.copy(.1f)
                )
            ) {
                Text(viewModel.selectedBook.value.chapter.toString())
            }

            OutlinedButton(
                modifier = modifier, onClick = {
                    viewModel.verseExpanded.value = true
                }, shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.onSurface,
                    backgroundColor = MaterialTheme.colors.surface.copy(.1f)
                )
            ) {
                Text(viewModel.selectedBook.value.verse.toString())
            }

        }

    }
}

@Composable
fun dropdowns(viewModel: BookChapterVerseNavBarViewModel) {

    val books by viewModel.books.observeAsState()
    val chapters by viewModel.chapters.observeAsState()
    val verses by viewModel.verses.observeAsState()

    booksDropdown(
        books = books, //listOf(Book("test", 1, 1, 1)),
        onDismiss = { viewModel.booksExpanded.value = false },
        onClick = {
            viewModel.setBook(it)
            viewModel.selectedBook.value = it
            viewModel.setChapters(it.id)
            viewModel.booksExpanded.value = false
            viewModel.chapterExpanded.value = true
        },
        expanded = viewModel.booksExpanded.value,
    )

    ChapterDropdown(
        chapters = chapters,
        onDismiss = {
            viewModel.booksExpanded.value = false
            viewModel.chapterExpanded.value = false
            viewModel.verseExpanded.value = true
        },
        onClick = {
            val b = Book(
                viewModel.selectedBook.value.name,
                viewModel.selectedBook.value.id,
                it,
                1
            )
            viewModel.setBook(b)
            viewModel.setVerses(viewModel.selectedBook.value.id, it)
            viewModel.booksExpanded.value = false
            viewModel.chapterExpanded.value = false;
            viewModel.verseExpanded.value = true

        },
        expanded = viewModel.chapterExpanded.value
    )
    VersesDropdown(
        verses = verses,
        onDismiss = {
            viewModel.booksExpanded.value = false
            viewModel.chapterExpanded.value = false
            viewModel.verseExpanded.value = false
        },
        onClick = {
            val b = Book(
                viewModel.selectedBook.value.name,
                viewModel.selectedBook.value.id,
                viewModel.selectedBook.value.chapter,
                it
            )
            viewModel.setBook(b)
            viewModel.setVerses(viewModel.selectedBook.value.id, it)
            viewModel.booksExpanded.value = false
            viewModel.chapterExpanded.value = false;
            viewModel.verseExpanded.value = false
        },
        expanded = viewModel.verseExpanded.value
    )

}

@Composable
fun PixelToDp(pixelSize: Int): Dp {
    return with(LocalDensity.current) { pixelSize.toDp() }
}

@Composable
fun booksDropdown(
    books: List<Book>?,
    onDismiss: () -> Unit,
    onClick: (Book) -> Unit,
    expanded: Boolean,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        if (books != null && books.isNotEmpty()) {
            DropdownMenuItem(onClick = { }) {
                Column {
                    Text(
                        "Book",
                        fontStyle = FontStyle.Italic
                    )

                }
            }
            for (b in books) {
                DropdownMenuItem(onClick = { onClick(b) }) {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp, top = 5.dp),
                            text = "${b.name}",
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

@Composable
fun ChapterDropdown(
    chapters: List<Int>?,
    onDismiss: () -> Unit,
    onClick: (chapter: Int) -> Unit,
    expanded: Boolean
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {


        if (chapters != null && chapters.isNotEmpty()) {
            DropdownMenuItem(onClick = { }) {
                Column {
                    Text(
                        "Chapter",
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            for (c in chapters) {
                DropdownMenuItem(onClick = { onClick(c) }) {
                    Column {


                        Text(
                            modifier = Modifier.padding(
                                start = 5.dp,
                                bottom = 5.dp,
                                top = 5.dp
                            ),
                            text = "$c",
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

@Composable
fun VersesDropdown(
    verses: List<Int>?,
    onDismiss: () -> Unit,
    onClick: (chapter: Int) -> Unit,
    expanded: Boolean
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {

        if (verses != null && verses.isNotEmpty()) {
            DropdownMenuItem(onClick = { }) {
                Column {
                    Text(
                        "Verse",
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            for (v in verses) {
                DropdownMenuItem(onClick = { onClick(v) }) {
                    Column {
                        Text(
                            modifier = Modifier.padding(
                                start = 5.dp,
                                bottom = 5.dp,
                                top = 5.dp
                            ),
                            text = "$v",
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

@Preview
@Composable
fun BookNamePreview() {
    BibleTheme() {
        val viewModel =
            BookChapterVerseNavBarViewModel(
                FakeBibleRepository(),
                BookNavServiceImpl(
                    FakeBibleRepository(),
                    BookNamesServiceImpl(FakeBibleRepository())
                ),
                BookNamesServiceImpl(FakeBibleRepository())
            )
        BookChapterVerseNavBar(viewModel)
    }
}
