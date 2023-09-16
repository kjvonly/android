package kjvonly.bible.services

import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.BookNames
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread


@Module
@InstallIn(SingletonComponent::class)
interface BookNamesServiceModule {
    @Singleton
    @Binds
    fun bindsBookNamesService(
        bookNavService: BookNamesServiceImpl
    ): BookNamesService
}

interface BookNamesService {
    fun getBookNames(onUpdate: (BookNames) -> Unit)
    fun getBooks(onUpdate: (List<Book>) -> Unit)
}

class BookNamesServiceImpl @Inject constructor(private val bibleRepository: BibleRepository) :
    BookNamesService {

    private var bookNames: BookNames =
        BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap())
    private var books = mutableListOf<Book>()

    init {
        thread {
            setBookNames()
            publish()
            publishBooks()
        }
    }

    var sub = mutableListOf<(BookNames) -> Unit>()
    override fun getBookNames(onUpdate: (BookNames) -> Unit) {
        sub.add(onUpdate)
        if (bookNames.bookNamesById.size != 0) {
            publish()
        }
    }

    private fun publish(){
        sub.forEach{
            it(bookNames)
        }
    }

    private var bookSubs = mutableListOf<(List<Book>) -> Unit>()
    override fun getBooks(onUpdate: (List<Book>)-> Unit) {
        bookSubs.add(onUpdate)
        if (books.size != 0) {
            publishBooks()
        }
    }

    private fun publishBooks(){
        bookSubs.forEach{
            it(books.toList())
        }


    }

    private fun setBookNames() {
        bookNames = bibleRepository.getBookNames()

        val sorted = bookNames.bookNamesById.keys.toList().sorted()

        sorted?.forEach {
            books.add(
                Book(
                    bookNames.bookNamesById?.get(it).toString(),
                    it,
                    1,
                    1
                )
            )
        }
    }
}