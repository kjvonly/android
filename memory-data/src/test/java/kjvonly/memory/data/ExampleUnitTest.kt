package kjvonly.memory.data

import kjvonly.memory.data.models.BookMemoryRange
import kjvonly.memory.data.models.IMemoryRange
import kjvonly.memory.data.models.VerseMemoryRange
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlinx.serialization.decodeFromString

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}

class MemoryRangeTest {
    @Test
    fun memory_range_serialization(){
        var b: IMemoryRange = VerseMemoryRange()
        val json = Json { encodeDefaults = true }

        var s = json.encodeToString(b)
        val v = Json.decodeFromString<IMemoryRange>(s)
        val t = v.getText()
        assertEquals("50_3_16", t)
    }
}