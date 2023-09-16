package kjvonly.feature.ui.layout.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.services.BookNavService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookSettingsViewModel @Inject constructor(
    private val bibleRepository: BibleRepository,
    private val bookNavService: BookNavService
) : ViewModel() {

    val onDismiss: () -> Unit = {
        expandMenu.value = false
    }
    val expandMenu = mutableStateOf(false)
    val onSettingsClicked: () -> Unit = {
        expandMenu.value = true
    }
}