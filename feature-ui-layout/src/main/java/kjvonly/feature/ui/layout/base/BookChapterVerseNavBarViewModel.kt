package kjvonly.feature.ui.layout.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.BookNames
import kjvonly.bible.data.models.Book
import kjvonly.bible.services.BookNamesService
import kjvonly.bible.services.BookNavService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class BookChapterVerseNavBarViewModel @Inject constructor(
    private val bibleRepository: BibleRepository,
    private val bookNavService: BookNavService,
    private val bookNamesService: BookNamesService

) : ViewModel() {

    val selectedBook = mutableStateOf(Book("Genesis", 1, 1, 1))

    private var bookNames: BookNames =
        BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap())

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    private val _chapters = MutableLiveData<List<Int>>()
    val chapters: LiveData<List<Int>>
        get() = _chapters

    private val _verses = MutableLiveData<List<Int>>()
    val verses: LiveData<List<Int>>
        get() = _verses


    var booksExpanded = mutableStateOf(false)
    var chapterExpanded = mutableStateOf(false)
    var verseExpanded = mutableStateOf(false)


    init {
        bookNamesService.getBookNames {
            bookNames = it
            viewModelScope.launch(Dispatchers.Main) {
                setChapters(selectedBook.value.id)
                setVerses(selectedBook.value.id, selectedBook.value.chapter)
            }
        }

        bookNamesService.getBooks {
            _books.postValue(it)

        }
        bookNavService.subscribe(onUpdate = {
            selectedBook.value = it
        })
        bookNavService.setBook(selectedBook.value)
    }

    fun setVerses(bookId: Int, chapterId: Int) {
        val chapterMap = bookNames.bookChapterVerseCountById[bookId]
        if (chapterMap != null) {
            val maxVerses = chapterMap[chapterId]
            if (maxVerses != null) {
                _verses.postValue((1..maxVerses).toList())
            }
        }
    }

    fun setChapters(id: Int) {
        val number = bookNames.maxChapterById[id]
        if (number != null) {
            _chapters.postValue((1..number).toList())
        }
    }

    fun setBook(b: Book) {
        bookNavService.setBook(b)
    }
}

