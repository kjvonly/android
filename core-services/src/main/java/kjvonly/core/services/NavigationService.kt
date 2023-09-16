package kjvonly.core.services


import androidx.navigation.NavController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationServiceModule {
    @Singleton
    @Binds
    fun bindsNavigationService(
        bookNavService: NavigationServiceImpl
    ): NavigationService
}

interface NavigationService {
    fun navigate(module: String, dest: String)
    fun add(module: String, nc: NavController)
    fun back(module: String)
}

class NavigationServiceImpl @Inject constructor(
) :
    NavigationService {
    val navControlMap: HashMap<String, NavController> = HashMap();

    override fun navigate(module: String, dest: String) {
        navControlMap[module]?.navigate(dest)
    }

    override fun add(module: String, nc: NavController) {
        navControlMap[module] = nc
    }

    override fun back (module: String){
        navControlMap[module]?.popBackStack()
    }


}