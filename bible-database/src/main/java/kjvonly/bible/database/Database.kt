package kjvonly.bible.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bible::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myModelDao(): BibleDao
}