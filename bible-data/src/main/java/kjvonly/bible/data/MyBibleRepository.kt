/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kjvonly.bible.data

import android.util.Log
import kjvonly.bible.data.models.BookNames
import kjvonly.bible.data.models.*
import kjvonly.bible.database.BibleDao
import java.util.zip.GZIPInputStream
import javax.inject.Inject
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import kjvonly.bible.data.models.Chapter
import kjvonly.bible.data.models.EmptyVerse
import kjvonly.bible.data.models.IVerse
import kjvonly.bible.data.models.Verse
import java.text.ParseException


interface BibleRepository {
    fun getBookNames(): BookNames
    fun getChapter(path: String): Chapter
    fun getVerseRange(path: String): List<IVerse>
    fun getVerse(path: String): IVerse

}

class DefaultBibleRepository @Inject constructor(
    private val bibleDao: BibleDao
) : BibleRepository {

    override fun getBookNames(): BookNames {
        val b = bibleDao.getDataById("booknames.json.gz")
        val json = GZIPInputStream(b?.data?.inputStream())
            .bufferedReader()
            .use { it.readText() }

        // parsing data back
        val bn = Json.decodeFromString<BookNames>(json)


        return bn
    }

    override fun getChapter(path: String): Chapter {
        Log.d("KJVonly", "getting chapter: $path")
        val b = bibleDao.getDataById("${path}.json.gz")
        val json = GZIPInputStream(b?.data?.inputStream())
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(json)
    }

    private fun getRange(s: String): Pair<Int, Int> {
        var p = Pair(1, 1)
        var start = ""
        var end = ""

        val sp = s.split("_")

        if (sp.size > 1) {
            start = sp[0]
            end = sp[1]
        } else if (sp.isNotEmpty()) {
            start = sp[0]
            end = sp[0]
        }
        try {
            p = Pair(start.toInt(), end.toInt())
        } catch (ex: ParseException) {
            Log.e("KJVonly", "Error Parsing range: $s")
        }
        return p
    }

    override fun getVerseRange(path: String): List<IVerse> {
        val verses = mutableListOf<Verse>()
        val s = path.split("_")

        if (s.size != 3) {
            return verses.toList()
        }

        val bookRange = getRange(s[0])
        val chapterRange = getRange(s[1])
        val verseRange = getRange(s[2])

        val chapter = getChapter("${bookRange.first}_${chapterRange.first}")

        for (v in verseRange.first..verseRange.second) {
            val verse = chapter.verses[v] ?: continue
            verses.add(verse)
        }
        return verses.toList()
    }

    override fun getVerse(path: String): IVerse {
        val vl = getVerseRange(path)

        if (vl.isNotEmpty()){
            return vl[0]
        }

        return EmptyVerse
    }
}


