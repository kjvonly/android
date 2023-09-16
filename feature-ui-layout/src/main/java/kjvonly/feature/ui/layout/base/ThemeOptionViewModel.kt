package kjvonly.feature.ui.layout.base


import androidx.lifecycle.*
import kjvonly.bible.services.ThemeService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ThemeOptionViewModel @Inject constructor(
    private val themeService: ThemeService

) : ViewModel() {

    fun darkMode(){
        themeService.darkMode()
    }
    fun lightMode(){
        themeService.lightMode()
    }

}
