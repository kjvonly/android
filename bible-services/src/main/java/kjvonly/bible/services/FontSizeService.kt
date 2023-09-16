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
interface FontSizeServiceModule {
    @Singleton
    @Binds
    fun bindsFontSizeService(
        bookNavService: FontSizeServiceImpl
    ): FontSizeService
}

interface FontSizeService {
    fun decreaseFont()
    fun increaseFont()
    fun subscribe(onUpdate: (Int) -> Unit)
    fun publish()

}

class FontSizeServiceImpl @Inject constructor(
    private val bibleRepository: BibleRepository
    ) :
    FontSizeService {

    private var fontSize: Int = 30
    override fun decreaseFont() {
        fontSize -= 2
        publish()
    }

    override fun increaseFont() {
        fontSize += 2
        publish()
    }

    private var subs = mutableListOf<(Int) -> Unit>()
    override fun subscribe(onUpdate: (Int) -> Unit) {
        subs.add(onUpdate)
        onUpdate(fontSize)
    }

    override fun publish() {
        subs.forEach { it(fontSize) }
    }
}