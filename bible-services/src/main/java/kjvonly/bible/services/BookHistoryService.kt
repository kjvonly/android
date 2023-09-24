package kjvonly.bible.services
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Book
import javax.inject.Inject

interface BookHistoryService {

    fun getLastBookLocation(): Book
}

class BookHistoryServiceImpl @Inject constructor(
    private val bookNavService: BookNamesService,
    private val bibleRepository: BibleRepository,
): BookHistoryService{
    override fun getLastBookLocation(): Book {
        return bibleRepository.getLastChapterVisited();
    }

}