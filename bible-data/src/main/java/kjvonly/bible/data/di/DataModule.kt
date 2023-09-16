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

package kjvonly.bible.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kjvonly.bible.data.BibleRepository
import kjvonly.bible.data.DefaultBibleRepository
import kjvonly.bible.data.models.BookNames
import kjvonly.bible.data.models.Chapter
import kjvonly.bible.data.models.IVerse
import kjvonly.bible.data.models.Verse
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsBibleRepository(
        bibleRepository: DefaultBibleRepository
    ): BibleRepository
}

class FakeBibleRepository @Inject constructor() : BibleRepository {
    override fun getBookNames(): BookNames {
        TODO("Not yet implemented")
    }

    override fun getChapter(path: String): Chapter {
        TODO("Not yet implemented")
    }

    override fun getVerseRange(path: String): List<IVerse> {
        TODO("Not yet implemented")
    }

    override fun getVerse(path: String): IVerse {
        TODO("Not yet implemented")
    }

}