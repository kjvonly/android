package kjvonly.bible.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kjvonly.bible.services.BookNavService
import javax.inject.Inject

@HiltViewModel
class BibleLandingScreenViewModel @Inject constructor(
    private val navService: BookNavService,

    ) : ViewModel(){


}