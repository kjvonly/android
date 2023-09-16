package kjvonly.feature.ui.layout.base

import androidx.lifecycle.ViewModel
import kjvonly.core.services.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class KJVonlyDrawerViewModel @Inject constructor(
    val navigationService: NavigationService,
) : ViewModel() {
    fun navigate(module: String, clickedItem: String) {
        navigationService.navigate(module, clickedItem)
    }

}