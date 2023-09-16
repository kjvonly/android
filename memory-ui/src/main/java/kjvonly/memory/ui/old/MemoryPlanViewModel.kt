package kjvonly.memory.ui.old
//
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kjvonly.bible.data.BibleRepository
//import kjvonly.bible.data.models.Book
//import kjvonly.bible.data.models.BookNames
//import kjvonly.bible.services.BookNamesService
//import kjvonly.bible.services.FontSizeService
//import kjvonly.core.services.NavigationService
//import kjvonly.memory.data.MemoryRepository
//import kjvonly.memory.data.models.*
//import kjvonly.memory.services.MemoryRangeService
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//enum class VersesSteps {
//    Verses, Memory
//}
//
//enum class ChaptersSteps {
//    Chapters, Verses, Memory
//}
//
//enum class BookSteps {
//    Books, Chapters, Verses, Memory
//}
//
//@HiltViewModel
//class MemoryPlanViewViewModel @Inject constructor(
//    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
//    private val navigationService: NavigationService,
//    private val bookNamesService: BookNamesService,
//    private val memoryRepository: MemoryRepository,
//    private val memoryRangeService: MemoryRangeService,
//    private val bibleRepository: BibleRepository,
//    private val fontSizeService: FontSizeService,
//    ) : ViewModel() {
//
//
//    val bookNames = mutableStateOf(BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap()))
//    val books = mutableListOf<Book>()
//    val memoryPlan = mutableStateOf<IMemoryPlan>(EmptyMemoryPlan)
//    val selectedRange = mutableStateOf<IMemoryRange>(EmptyMemoryRange())
//    var planId: String = ""
//    var fontSize = 30
//
//    var versesStep = mutableStateOf(VersesSteps.Verses)
//    var selectedVerseStep = mutableStateOf(0)
//
//    var chaptersStep = mutableStateOf(ChaptersSteps.Chapters)
//    var selectedChapterStep = mutableStateOf(0)
//    var selectedChapterVerseStep = mutableStateOf(0)
//
//    var bookStep = mutableStateOf(BookSteps.Books)
//    var selectedBookStep = mutableStateOf(Book("Joh", 50, 3, 16))
//    var selectedBookChapterStep = mutableStateOf(0)
//    val selectedBookChapterVerseStep = mutableStateOf(0)
//
//    val memoryEntity = mutableStateOf<IMemoryEntity>(EmptyMemoryEntity)
//
//    fun back() {
//        this.navigationService.back("memory")
//    }
//
//
//    fun getMemoryPlan(id: String) {
//        planId = id
//        viewModelScope.launch(Dispatchers.IO) {
//            val mp = memoryRepository.getTopic(id)
//            viewModelScope.launch(Dispatchers.Main) {
//                memoryPlan.value = mp
//            }
//        }
//    }
//
//    fun getMemoryRangeText(s: String): String {
//        return memoryRangeService.getDisplayText(s)
//    }
//
//    fun getVerse(textId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val verse = bibleRepository.getVerse(textId)
//            viewModelScope.launch(Dispatchers.Main) {
//                memoryEntity.value = MemoryEntity(textId = textId, verse = verse)
//            }
//        }
//    }
//
//    init {
//        bookNamesService.getBookNames {
//            bookNames.value = it
//        }
//        bookNamesService.getBooks {
//            books.clear()
//            books.addAll(it)
//        }
//
//        fontSizeService.subscribe {
//            fontSize = it
//        }
//    }
//
//}