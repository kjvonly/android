package kjvonly.bible.services


import kjvonly.bible.data.BibleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ThemeServiceModule {
    @Singleton
    @Binds
    fun bindsThemeService(
        bookNavService: ThemeServiceImpl
    ): ThemeService
}

interface ThemeService {
    fun darkMode()
    fun lightMode()
    fun subscribe(onUpdate: (Mode) -> Unit)
    fun publish()

}

enum class Mode {
    DARK,
    LIGHT
}

class ThemeServiceImpl @Inject constructor(
    private val bibleRepository: BibleRepository
    ) :
    ThemeService {

    private var mode : Mode = Mode.DARK
    private var subs = mutableListOf<(Mode) -> Unit>()
    override fun darkMode() {
        mode = Mode.DARK
        publish()
    }

    override fun lightMode() {
        mode = Mode.LIGHT
        publish()
    }

    override fun subscribe(onUpdate: (Mode) -> Unit) {
        subs.add(onUpdate)
        onUpdate(mode)
    }

    override fun publish() {
        subs.forEach { it(mode) }
    }
}