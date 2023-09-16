package kjvonly.bible.services

import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.BookNames
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TurnToServiceModule {
    @Singleton
    @Binds
    fun bindsTurnToService(
        bookNavService: TurnToServiceImpl
    ): TurnToService
}
data class ChapterPosition(val id: Int, val chapter: Int, val verse: Int)

interface TurnToService {
    fun goNext(currentPosition: ChapterPosition): ChapterPosition
    fun goPrev(currentPosition: ChapterPosition): ChapterPosition
    fun getNextChapter(cp: ChapterPosition): ChapterPosition
    fun getPrevChapter(cp: ChapterPosition): ChapterPosition
}

class TurnToServiceImpl @Inject constructor(
    private val bibleRepository: BibleRepository,
    private val bookNamesService: BookNamesService
) :
    TurnToService {
    private var bookNames: BookNames =
        BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap())
    private var sortedBooks = listOf<Book>()
    private var sortedBooksIds = mutableListOf<Int>()

    init {
        bookNamesService.getBookNames {
            bookNames = it
        }

        bookNamesService.getBooks {
            sortedBooks = it
            sortedBooks.forEach {
                sortedBooksIds.add(it.id)
            }
        }
    }

    override fun getNextChapter(cp: ChapterPosition): ChapterPosition {
        val i = sortedBooksIds.indexOf(cp.id)
        if (i == sortedBooksIds.size - 1) {
            return ChapterPosition(sortedBooksIds[0], 1, 1)
        }
        return ChapterPosition(sortedBooksIds[i + 1], 1, 1)
    }

    override fun getPrevChapter(cp: ChapterPosition): ChapterPosition {
        val i = sortedBooksIds.indexOf(cp.id)
        if (i == 0) {
            val prevCh = sortedBooksIds.size - 1
            var last: Int? = bookNames.maxChapterById[prevCh]
            // for compile compliance - check for null. Should never happen
            if (last == null) {
                last = 1
            }

            return ChapterPosition(sortedBooksIds[prevCh], last, 1)
        }
        var last = bookNames.maxChapterById[sortedBooksIds[i - 1]]
        if (last == null) {
            last = 1
        }
        return ChapterPosition(sortedBooksIds[i - 1], last, 1)
    }

    override fun goNext(cp: ChapterPosition): ChapterPosition {
        val max = bookNames.maxChapterById[cp.id]
        if (max == cp.chapter) {
            return getNextChapter(cp)
        }
        return ChapterPosition(cp.id, cp.chapter + 1, 1)
    }

    override fun goPrev(cp: ChapterPosition): ChapterPosition {
        if (cp.chapter == 1) {
            return getPrevChapter(cp)
        }
        return ChapterPosition(cp.id, cp.chapter - 1, 1)
    }
}