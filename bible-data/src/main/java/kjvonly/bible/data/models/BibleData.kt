package kjvonly.bible.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(var name: String, var id: Int, var chapter: Int, var verse: Int)

@Serializable
data class ChapterPosition(val id: Int, val chapter: Int, val verse: Int)
