package kjvonly.bible.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Chapter
import kjvonly.bible.data.models.Book
import kjvonly.bible.services.BookNavService
import kjvonly.bible.services.FontSizeService
import kjvonly.bible.services.TurnToService
import dagger.hilt.android.lifecycle.HiltViewModel
import kjvonly.bible.data.models.ChapterPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val bibleRepository: BibleRepository,
    private val navService: BookNavService,
    private val fontSizeService: FontSizeService,
    private val turnToService: TurnToService
) : ViewModel() {

    // NavService will update book on subscribe. This has no effect in setting the book.
    var book = mutableStateOf(Book("John", 50, 3, 16))
    // -1 number prevents LaunchEffect from running until the chapter content is
    // available to render.
    var chapter: MutableState<Chapter> = mutableStateOf<Chapter>(
        Chapter(-1, "John", HashMap(), HashMap(), HashMap())
    )

    /**
     * HashMap to keep track of rendered verse row height to
     * direct scrollState where to scroll.
     */
    var verseRowRenderedHeightMap = HashMap<Int, Float>()
    var fontSize = mutableStateOf(30)

    init {
        navService.subscribe(onUpdate = {
            book.value = it
            val path = "${book.value.id}_${book.value.chapter}"
            viewModelScope.launch(Dispatchers.IO) {
                chapter.value = bibleRepository.getChapter(path)
            }
        })

        fontSizeService.subscribe(onUpdate = {
            this.fontSize.value = it
        })
    }

    fun turnPageForward() {
        val cp = ChapterPosition(book.value.id, book.value.chapter, book.value.verse)
        val ncp = turnToService.goNext(cp)
        navService.setBook(ncp)
        saveChapterPosition(ncp)
    }

    fun turnPageBackwards() {
        val cp = ChapterPosition(book.value.id, book.value.chapter, book.value.verse)
        val ncp = turnToService.goPrev(cp)
        navService.setBook(ncp)
        saveChapterPosition(cp)
    }

    private fun saveChapterPosition(cp: ChapterPosition){
        viewModelScope.launch(Dispatchers.IO){
            bibleRepository.setLastChapterVisited(cp)
        }

    }
}