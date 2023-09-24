package kjvonly.bible.services

import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.BookNames
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kjvonly.bible.data.models.ChapterPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface BookNavServiceModule {
    @Singleton
    @Binds
    fun bindsBookNavService(
        bookNavService: BookNavServiceImpl
    ): BookNavService
}

interface BookNavService {
    fun setBook(book: Book)
    fun setBook(cp: ChapterPosition)
    fun subscribe(onUpdate: (b: Book) -> Unit)
    fun publish(b: Book)
        fun init()
}

class BookNavServiceImpl @Inject constructor(
    private val bibleRepository: BibleRepository,
    bookNamesService: BookNamesService
    // TODO ADD in new BookHistoryService
) :

    BookNavService {
    var bookNames =
        BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap())


    private var book: Book = Book("John", 50, 3, 2);


    init {
      bookNamesService.getBookNames{
          bookNames  = it
      }
    }

    override fun setBook(b: Book) {
        book = b
        publish(b)
    }

    override fun setBook(cp: ChapterPosition) {
        var bn = bookNames.bookNamesById[cp.id]
        if (bn == null) {
            bn = "Genesis" // should never be empty but must have for compiler warnings
        }
        setBook(Book(bn, cp.id, cp.chapter, cp.verse))
    }

    private var subs = mutableListOf<(b: Book) -> Unit>()
    override fun subscribe(onUpdate: (b: Book) -> Unit) {
        subs.add(onUpdate)
        onUpdate(book)
    }

    override fun publish(b: Book) {
        book = b
        subs.forEach { it(b) }
    }

    override fun init() {
        val cp = bibleRepository.getLastChapterVisited()
        if (cp.id != -1){
            setBook(cp)
        }
    }
}