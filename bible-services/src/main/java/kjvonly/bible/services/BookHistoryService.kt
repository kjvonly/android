package kjvonly.bible.services
import kjvonly.bible.data.models.Book
import javax.inject.Inject

interface BookHistoryService {

    fun getLastBookLocation(): Book
}

class BookHistoryServiceImpl @Inject constructor(
    private val bookNavService: BookNamesService
): BookHistoryService{
    override fun getLastBookLocation(): Book {
        TODO("Not yet implemented")
    }

}