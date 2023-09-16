package kjvonly.memory.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "ranges", foreignKeys = [ForeignKey(
        entity = MemoryTreeDao::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("treeId"),
        onDelete = CASCADE
    )]
)
data class MemoryRangesDao(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "treeId")
    val treeId: String,
    @ColumnInfo(name = "nodeId")
    val nodeId: String,
    @ColumnInfo(name = "name")
    val name: String
) {}

@Entity(
    tableName = "range", foreignKeys = [ForeignKey(
        entity = MemoryRangesDao::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("rangesId"),
        onDelete = CASCADE
    )]
)
data class MemoryRangeDao(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "rangesId")
    val rangesId: String,
    @ColumnInfo(name = "range")
    val memoryRange: String,
) {}


@Entity(
    tableName = "verses", foreignKeys = [ForeignKey(
        entity = MemoryRangeDao::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("rangeId"),
        onDelete = CASCADE
    )]
)
data class MemoryVerseDao(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "rangeId") val rangeId: String,
    @ColumnInfo(name = "versePath")
    val versePath: String, // path to get the verse e.g. 50_3_16
    val name: String,
) {}

@Entity(tableName = "tree")
data class MemoryTreeDao(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "rootNode")
    val rootNode: String,
) {}


@Dao
interface MemoryDao {
    @Query("SELECT * FROM ranges where id = :id limit 1")
    fun getMemoryPlan(id: String): MemoryRangesDao

    @Query("SELECT * FROM ranges")
    fun getMemoryPlans(): List<MemoryRangesDao>

    @Query("SELECT * FROM range where rangesId = :rangesId")
    fun getRanges(rangesId: String): List<MemoryRangeDao>

    @Query("SELECT * FROM tree")
    fun getTrees(): List<MemoryTreeDao>

    @Insert
    fun insertRanges(mp: MemoryRangesDao)

    @Insert
    fun insertRanges(mp: List<MemoryRangeDao>)

    @Insert
    fun insertRange(mr: MemoryRangeDao)
//
//    @Query("DELETE from range")
//    fun deleteAllRange()
//
//    @Query("DELETE from ranges ")
//    fun deleteAllRanges()

    @Query("DELETE from ranges where id = :id")
    fun deleteRanges(id: String)

    @Query("DELETE from range WHERE rangesId = :rangesId")
    fun deleteRangeByRangesId(rangesId: String)

    @Query("DELETE from ranges WHERE id= :id")
    fun deleteRangesById(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTree(mn: MemoryTreeDao): Long

    @Update
    fun updateTree(mn: MemoryTreeDao)

    @Transaction
    fun insertOrUpdate(mn: MemoryTreeDao): Long {
        val id = insertTree(mn)
        return if (id==-1L) {
            updateTree(mn)
            1
        } else {
            id
        }
    }

    @Query("SELECT * from tree WHERE id = :id")
    fun getTree(id: String): MemoryTreeDao

    @Query("DELETE from tree WHERE id = :id")
    fun deleteTreeRoot(id: String)

}
