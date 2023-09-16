package kjvonly.bible.data.models

import kotlinx.serialization.*

@Serializable
sealed interface IBookNames {
    @SerialName("booknamesById")
    val bookNamesById: HashMap<Int, String>
    @SerialName("booknamesByName")
    val bookNamesByName: HashMap<String, Int>
    @SerialName("shortNames")
    val shortNames: HashMap<Int, String>
    @SerialName("maxChapterById")
    val maxChapterById: HashMap<Int, Int>
    @SerialName("bookchapterversecountById")
    val bookChapterVerseCountById: HashMap<Int, HashMap<Int, Int>>
}

data class EmptyBookNames(
    @SerialName("booknamesById")
    override val bookNamesById: HashMap<Int, String> = HashMap(),
    @SerialName("booknamesByName")
    override val bookNamesByName: HashMap<String, Int> = HashMap(),
    @SerialName("shortNames")
    override val shortNames: HashMap<Int, String> = HashMap(),
    @SerialName("maxChapterById")
    override val maxChapterById: HashMap<Int, Int> = HashMap(),
    @SerialName("bookchapterversecountById")
    override val bookChapterVerseCountById: HashMap<Int, HashMap<Int, Int>> = HashMap()
) : IBookNames {}

@Serializable
data class BookNames(
    @SerialName("booknamesById")
    override val bookNamesById: HashMap<Int, String>,
    @SerialName("booknamesByName")
    override val bookNamesByName: HashMap<String, Int>,
    @SerialName("shortNames")
    override val shortNames: HashMap<Int, String>,
    @SerialName("maxChapterById")
    override val maxChapterById: HashMap<Int, Int>,
    @SerialName("bookchapterversecountById")
    override val bookChapterVerseCountById: HashMap<Int, HashMap<Int, Int>>
) : IBookNames {

}

@Serializable
data class Chapter(
    val number: Int,
    val bookName: String,
    val verses: HashMap<Int, Verse>,
    val verseMap: HashMap<Int, String>,
    val footnotes: HashMap<Int, String>,
)


interface IVerse {
    val number: Int
    val words: List<Word>
    val text: String
}

@Serializable
data class Verse(
    override val number: Int,
    override val words: List<Word>,
    override val text: String,
) : IVerse {}


@Serializable
object EmptyVerse : IVerse {
    override val number: Int = 0
    override val words: List<Word> = mutableListOf()
    override val text: String = ""
}

@Serializable
data class Word(
    val text: String,
    @SerialName("class")
    val _class: List<String>?,
    val href: List<String>?,
    val emphasis: Boolean,
)

@Serializable
data class StrongDef(
    val number: String,
    val originalWord: String,
    val partsOfSpeech: String,
    val phoneticSpelling: String,
    val transliteratedWord: String,
    val usageByBook: List<Usage>?,
    val usageByWord: List<Usage>?,
    val brownDef: List<ListItem>?,
    val strongsDef: String,
    val thayersDef: ListItem?,
)

@Serializable
data class ListItem(
    val text: String,
    val children: List<ListItem>?,
)

@Serializable
data class Usage(
    val text: String,
    val href: List<String>?,
    @SerialName("class")
    val _class: List<String>?,
)