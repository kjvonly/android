package kjvonly.bible.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Bible(
    @PrimaryKey val id: String,
    val data: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bible

        if (id != other.id) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}

@Entity
data class History(
    @PrimaryKey val id: Int,
    val path: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as History

        if (id != other.id) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + path.hashCode()
        return result
    }

}

@Dao
interface BibleDao {
    @Query("SELECT * FROM bible where id = :id limit 1")
    fun getDataById(id: String): Bible

    @Query("INSERT into history (id, path) values (0, :path)")
    fun insertLastChapterVisited(path: String)

    @Query("update history SET path = :path where id = 0")
    fun updateLastChapterVisited(path: String)

    @Query("SELECT path from history where id = 0")
    fun getLastChapterVisited():String

}
