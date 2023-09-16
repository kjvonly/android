package kjvonly.memory.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.BookNames
import kjvonly.bible.services.BookNamesService
import kjvonly.core.services.NavigationService
import kjvonly.memory.data.MemoryRepository
import kjvonly.memory.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch
import kjvonly.memory.data.models.Range
import kjvonly.memory.data.models.Ranges
import kjvonly.memory.data.models.randomUUID


enum class SelectionType{
    Book, Chapter,Verse
}
@HiltViewModel
class MemoryPlanCreateViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val bookNamesService: BookNamesService,
    private val memoryRepository: MemoryRepository,
) : ViewModel() {


    val bookStartExpanded = mutableStateOf(false)

    fun onBookStartDismiss(){
        bookStartExpanded.value = false
    }
    fun onBookStartExpanded(){
        bookStartExpanded.value = true
    }

    val bookEndExpanded = mutableStateOf(false)
    fun onBookEndDismiss(){
        bookEndExpanded.value = false
    }
    fun onBookEndExpanded(){
        bookEndExpanded.value = true
    }

    val chapterStartExpanded = mutableStateOf(false)


    fun onChapterStartDismiss(){
        chapterStartExpanded.value = false
    }
    fun onChapterStartExpanded(){
        chapterStartExpanded.value = true
    }

    val chapterEndExpanded = mutableStateOf(false)
    fun onChapterEndDismiss(){
        chapterEndExpanded.value = false
    }
    fun onChapterEndExpanded(){
        chapterEndExpanded.value = true
    }

    //

    val verseStartExpanded = mutableStateOf(false)


    fun onVerseStartDismiss(){
        verseStartExpanded.value = false
    }
    fun onVerseStartExpanded(){
        verseStartExpanded.value = true
    }

    val verseEndExpanded = mutableStateOf(false)
    fun onVerseEndDismiss(){
        verseEndExpanded.value = false
    }
    fun onVerseEndExpanded(){
        verseEndExpanded.value = true
    }
    val selectedSelectionType = mutableStateOf(SelectionType.Verse)
    val selectionTypeExpanded = mutableStateOf(false)


    fun onSelectionTypeDismiss(){
        selectionTypeExpanded.value = false
    }

    fun onSelectionTypeExpanded(){
        selectionTypeExpanded.value = true
    }
    fun onSelectedSelectionType(b: String){
        if (b == "Book"){
            selectedSelectionType.value = SelectionType.Book
        }

        if (b == "Chapter"){
            selectedSelectionType.value = SelectionType.Chapter
        }

        if (b == "Verse"){
            selectedSelectionType.value = SelectionType.Verse
        }
        selectionTypeExpanded.value = false
    }

    var booksStartSelection = 1
    var booksEndSelection = 1
    var selectedStartBookName = mutableStateOf<String>("Genesis")
    var selectedEndBookName= mutableStateOf<String>("Genesis")
    var bookEndRange = mutableStateListOf<Book>()

    fun onBookStartSelected(it: Book) {
        booksStartSelection = it.id
        if (booksStartSelection > booksEndSelection){
            booksEndSelection = booksStartSelection
            updateSelectedEndBookName(booksEndSelection)
        }

        updateBooksEndRange(booksStartSelection)
        updateSelectedStartBookName(booksStartSelection)
        onChapterStartSelected(1)
        onChapterEndSelected(1)
    }

    private fun updateSelectedStartBookName(bookId :Int) {
        selectedStartBookName.value = bookNames.bookNamesById[bookId].toString()
    }

    fun onBookEndSelected(it: Book) {
        booksEndSelection = it.id
        updateSelectedEndBookName(booksEndSelection)
    }

    private fun updateSelectedEndBookName(bookId :Int) {
        selectedEndBookName.value = bookNames.bookNamesById[bookId].toString()
    }

    private fun updateBooksEndRange(startId: Int){
        var i = 0
        books.forEachIndexed { index, it ->
            if (it.id == startId) {
                i = index
            }
        }
        bookEndRange.clear()
        bookEndRange.addAll(books.subList(i, books.size))
    }

    var chapterStartSelection = 1
    var chapterEndSelection = 1
    var selectedStartChapterName = mutableStateOf<String>("1")
    var selectedEndChapterName= mutableStateOf<String>("1")
    var chapterStartRange = mutableStateListOf<Int>()
    var chapterEndRange = mutableStateListOf<Int>()

    fun onChapterStartSelected(chapterId: Int){
        selectedStartChapterName.value = chapterId.toString()
        chapterStartSelection = chapterId
        if (chapterStartSelection > chapterEndSelection){
            chapterEndSelection = chapterStartSelection
            selectedEndChapterName.value = chapterEndSelection.toString()
        }

        updateChapterStartRange(chapterId)
        onVerseStartSelected(1)
        onVerseEndSelected(1)
    }

    fun onChapterEndSelected(chapterId: Int){
        selectedEndChapterName.value = chapterId.toString()
        chapterEndSelection = chapterId
    }

    private fun updateChapterStartRange(startChapter: Int){
        chapterStartSelection = startChapter
        var maxChapter = bookNames.maxChapterById[booksStartSelection]
        if (maxChapter == null) {
            maxChapter = 1
        }
        chapterStartRange.clear()
        chapterStartRange.addAll(1..maxChapter)
        updateChapterEndRange(chapterStartSelection, maxChapter)
    }

    private fun updateChapterEndRange(chapterStartId: Int, maxChapter: Int){
        chapterEndRange.clear()
        chapterEndRange.addAll(chapterStartId..maxChapter)
    }


    var verseStartSelection = 1
    var verseEndSelection = 1
    var selectedStartVerseName = mutableStateOf<String>("1")
    var selectedEndVerseName= mutableStateOf<String>("1")
    var verseStartRange = mutableStateListOf<Int>()
    var verseEndRange = mutableStateListOf<Int>()

    fun onVerseStartSelected(verseId: Int){
        selectedStartVerseName.value = verseId.toString()
        verseStartSelection = verseId
        if (verseStartSelection > verseEndSelection){
            verseEndSelection = verseStartSelection
            selectedEndVerseName.value = verseEndSelection.toString()
        }

        updateVerseStartRange(verseId)
    }

    fun onVerseEndSelected(verseId: Int){
        selectedEndVerseName.value = verseId.toString()
        verseEndSelection = verseId
    }

    private fun updateVerseStartRange(startVerse: Int){
        verseStartSelection = startVerse
        var verseCount =
            bookNames.bookChapterVerseCountById[booksStartSelection]?.get(
                chapterStartSelection
            )
        if (verseCount == null) {
            verseCount = 1
        }

        verseStartRange.clear()
        verseStartRange.addAll(1..verseCount)
        updateVerseEndRange(verseStartSelection, verseCount)
    }

    private fun updateVerseEndRange(verseStartId: Int, maxVerse: Int){
        verseEndRange.clear()
        verseEndRange.addAll(verseStartId..maxVerse)
    }

    var memoryEntities = mutableStateListOf<String>()
    var memoryRangesId = ""


    var bookNames: BookNames = BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap())
    var text = mutableStateOf("")

    var books = mutableStateListOf<Book>()

    fun saveRanges(memoryGroupCreateViewModel: MemoryGroupCreateViewModel){
        memoryRangesId = randomUUID()
        val rs = Ranges(memoryRangesId, memoryGroupCreateViewModel.tree.id, memoryGroupCreateViewModel.tree.currentNode.value.id, text.value)
        memoryRepository.saveRanges(rs)
        val rl = mutableListOf<Range>()
        for (e in memoryEntities){
            rl.add(Range(randomUUID(),  memoryRangesId, e))
        }
        memoryRepository.saveRange(rl)
    }

    fun save(displayManagerViewModel: MemoryCreateDisplayManagerViewModel, memoryGroupCreateViewModel: MemoryGroupCreateViewModel){
        viewModelScope.launch (Dispatchers.IO){
            saveRanges(memoryGroupCreateViewModel)
            viewModelScope.launch (Dispatchers.Main){
                displayManagerViewModel.selectedScreen.value = Displays.TreeFromPlan
            }
        }
    }

    fun addRange(){
        if (selectedSelectionType.value == SelectionType.Book){
            val s = "%s-%s".format(booksStartSelection.toString(), booksEndSelection.toString())
            this.memoryEntities.add(s)
        }

        if (selectedSelectionType.value == SelectionType.Chapter){
            val s = "%s:%s-%s".format(booksStartSelection.toString(),chapterStartSelection.toString(), chapterEndSelection.toString())
            this.memoryEntities.add(s)
        }

        if (selectedSelectionType.value == SelectionType.Verse){
            val s = "%s:%s:%s-%s".format(booksStartSelection.toString(),chapterStartSelection.toString(), verseStartSelection.toString(), verseEndSelection.toString())
            this.memoryEntities.add(s)
        }
    }

    fun getText(s: String): String{
        val sl = s.split(":")
        var text = ""
        when (sl.size){
            1 -> text = bookRangeText(sl[0])
            2 -> text = chapterRangeText(sl[0], sl[1])
            3 -> text = verseRangeText(sl[0], sl[1], sl[2])
        }

        return text;

    }

    fun bookRangeText(s: String): String{
        val sl = s.split("-")

        return if (sl[0] == sl[1]) {
            "%s".format(bookNames.shortNames[sl[0].toInt()])
        } else {
            "%s-%s".format(bookNames.shortNames[sl[0].toInt()], bookNames.shortNames[sl[1].toInt()])
        }
    }

    fun chapterRangeText(book: String, chapters: String): String{
        val chaps = chapters.split("-")
        return if (chaps[0] == chaps[1]) {
             "%s %s".format(
                bookNames.shortNames[book.toInt()],
                chaps[0].toInt()
            )
        } else {
            "%s %s-%s".format(
                bookNames.shortNames[book.toInt()],
                chaps[0].toInt(),
                chaps[1].toInt()
            )
        }
    }

    fun verseRangeText(book: String, chapter: String, verses: String): String{
        val v = verses.split("-")
        return if (v[0] == v[1]){
            "%s %s:%s".format(bookNames.shortNames[book.toInt()], chapter, v[0].toInt())
        } else {
            "%s %s:%s-%s".format(bookNames.shortNames[book.toInt()], chapter, v[0].toInt(), v[1].toInt())
        }

    }

    fun clear(){
        memoryEntities.clear()
        memoryRangesId = ""
        text.value = ""
    }

    init {
        bookNamesService.getBookNames {
            bookNames = it
            onBookStartSelected(Book("Genesis", 1, 1, 1))
        }

        bookNamesService.getBooks {
            viewModelScope.launch {
                books.clear()
                books.addAll(it)

            }
        }

    }

}
