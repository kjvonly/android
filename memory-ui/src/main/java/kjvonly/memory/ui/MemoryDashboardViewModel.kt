package kjvonly.memory.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.models.*
import kjvonly.core.services.NavigationService
import kjvonly.memory.data.MemoryRepository
import kjvonly.memory.data.models.File
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

import kotlinx.coroutines.launch
import kjvonly.bible.data.models.BookNames
import kjvonly.bible.data.models.Chapter
import kjvonly.bible.data.models.EmptyVerse
import kjvonly.bible.data.models.IVerse


@HiltViewModel
class MemoryPlanDashboardViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val memoryRepository: MemoryRepository,
    private val bibleRepository: BibleRepository
) : ViewModel() {
    fun navigate(s: String) {
        navigationService.navigate("memory", s)
    }

    val leaves = mutableStateListOf<File>()
    val bookNames = mutableStateOf(BookNames(HashMap(), HashMap(), HashMap(), HashMap(), HashMap()))
    val currentVerse = mutableStateOf<IVerse>(EmptyVerse)
    var currentChapter: Chapter = Chapter(3, "John", HashMap(), HashMap(), HashMap())

    fun updateMemoryPlans(){
        viewModelScope.launch(Dispatchers.IO) {
            //val m = memoryRepository.getMemoryPlans()

            viewModelScope.launch(Dispatchers.Main) {
                leaves.clear()
             //   leaves.addAll(m)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val bn = bibleRepository.getBookNames()
            viewModelScope.launch(Dispatchers.Main) {
                bookNames.value = bn
            }

            val verse = bibleRepository.getVerse("50_3_16")
            viewModelScope.launch(Dispatchers.Main) {
                currentVerse.value = verse
            }
        }

    }
}