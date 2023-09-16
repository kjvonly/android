package kjvonly.bible.database

import androidx.room.Dao
import androidx.room.Entity
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

@Dao
interface BibleDao {
    @Query("SELECT * FROM bible where id = :id limit 1")
    fun getDataById(id: String): Bible

}
