package kjvonly.feature.ui.layout.base


import androidx.lifecycle.*
import kjvonly.bible.services.FontSizeService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FontSizeViewModel @Inject constructor(
    private val fontSizeService: FontSizeService

) : ViewModel() {

    fun increaseFont(){
        fontSizeService.increaseFont()
    }
    fun decreaseFont(){
        fontSizeService.decreaseFont()
    }

}
