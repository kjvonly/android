package kjvonly.memory.data.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface  FileSystemObject {
    var name: String
}

@Serializable
class Folder(override var name: String) : FileSystemObject {}

@Serializable
class File (override var name: String, var rangesId: String): FileSystemObject {}
