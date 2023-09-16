package kjvonly.core.ui.theme


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kjvonly.bible.services.Mode
import kjvonly.bible.services.ThemeService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BibleThemeViewModel @Inject constructor(
    private val themeService: ThemeService

) : ViewModel() {

    var mode = mutableStateOf(Mode.DARK)

    init {
        themeService.subscribe {
            this.mode.value = it
        }
    }

    fun darkMode(){
        themeService.darkMode()
    }
    fun lightMode(){
        themeService.lightMode()
    }

}
