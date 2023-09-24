package kjvonly.bible.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bible::class, History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myModelDao(): BibleDao
}