package kjvonly.memory.data.models

import java.util.*

fun randomUUID() = UUID.randomUUID().toString()

class Ranges (
    var id: String,
    var treeId: String,
    var nodeId: String,
    var name: String,
)

 class  Range(
    var id: String,
    var rangesId: String,
    var range: String,
 )