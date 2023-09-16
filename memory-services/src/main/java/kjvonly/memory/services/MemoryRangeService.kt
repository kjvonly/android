package kjvonly.memory.services

import androidx.compose.runtime.mutableStateOf
import kjvonly.bible.data.models.EmptyBookNames
import kjvonly.bible.data.models.IBookNames
import kjvonly.bible.services.BookNamesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface MemoryRangeServiceModule {
    @Singleton
    @Binds
    fun bindsMemoryRangeService(
        memoryRangeService: MemoryRangeServiceImpl
    ): MemoryRangeService
}

interface MemoryRangeService {
    fun getDisplayText(s :String): String
}

class MemoryRangeServiceImpl @Inject constructor(private val bookNamesService: BookNamesService) :
    MemoryRangeService {
    val bookNames = mutableStateOf<IBookNames>(EmptyBookNames())

    init {
        bookNamesService.getBookNames {
            bookNames.value = it
        }
    }

    override fun getDisplayText(id: String): String {
        val sp = id.split("_")
        var s = ""
        if (sp.size == 1) {
            s =   getBookText(sp[0])
        }
        if (sp.size == 2) {
            val bookText = getBookText(sp[0])
            s = getChapterText(sp[1], bookText)
        }
        if (sp.size == 3) {
            val bookText = getBookText(sp[0])
            val chapterText = getChapterText(sp[1], bookText)
            s = getVerseText(sp[2], chapterText)
        }
        return s
    }

    private fun getVerseText(verseId: String, chapterText: String): String {
        val vr = verseId.split("-")
        val s = if (vr.size == 2) {
            "$chapterText:${vr[0].toInt()}-${vr[1].toInt()}"
        } else {
            "$chapterText ${vr[0].toInt()}"
        }
        return s
    }

    private fun getChapterText(chapterId:String, bookText: String): String {
        val cr = chapterId.split("-")

        val s = if (cr.size == 2) {
            "$bookText ${cr[0].toInt()}-${cr[1].toInt()}"
        } else {
            "$bookText ${cr[0].toInt()}"
        }
        return s
    }

    private fun getBookText(bookId:  String) : String{
        val br = bookId.split("-")
        val s = if (br.size == 2) {
            "${bookNames.value.shortNames[br[0].toInt()]}-${bookNames.value.shortNames[br[1].toInt()]}"

        } else {
            "${bookNames.value.shortNames[br[0].toInt()]}"

        }
        return s
    }
}
