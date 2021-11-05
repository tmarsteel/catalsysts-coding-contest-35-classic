package ccc35

import java.nio.file.Paths
import kotlin.io.path.readLines

fun main(vararg args: String) {
    var lines = Paths.get(args[0]).readLines()
    val nLines = lines[0].toInt()
    lines = lines.subList(0, nLines)

    val tokens = lines.joinToString("\n").tokenize()
}
