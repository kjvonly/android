package kjvonly.memory.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MemoryRangesDao::class, MemoryRangeDao::class, MemoryVerseDao::class, MemoryTreeDao::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myModelDao(): MemoryDao
}