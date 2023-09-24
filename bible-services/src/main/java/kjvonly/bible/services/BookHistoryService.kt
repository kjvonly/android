package kjvonly.bible.services
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.Chapter
import kjvonly.bible.data.models.ChapterPosition
import javax.inject.Inject

interface BookHistoryService {

    fun getLastBookLocation(): ChapterPosition
}

class BookHistoryServiceImpl @Inject constructor(
    private val bookNavService: BookNamesService,
    private val bibleRepository: BibleRepository,
): BookHistoryService{
    override fun getLastBookLocation(): ChapterPosition {
        return bibleRepository.getLastChapterVisited();
    }

}