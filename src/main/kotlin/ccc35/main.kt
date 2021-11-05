package ccc35

import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readLines
import kotlin.io.path.writeText

fun main(vararg args: String) {
    val inputs = Paths.get("./src/main/resources/inputs/level2")
        .listDirectoryEntries("*.in")
        .map { it.toAbsolutePath() }
        //.filter { "example" in it.fileName.toString() }

    for (inputFile in inputs) {
        println("Processing $inputFile")

        var lines = inputFile.readLines()
        val nLines = lines[0].toInt()
        lines = lines.subList(1, nLines + 1)

        val tokens = lines.joinToString("\n").tokenize()
        val function = tokens.parseProgram().single()

        val ctxt = ExecutionContext()
        function.execute(ctxt)

        val outFileName = inputFile.fileName.toString().substringBeforeLast('.') + ".out"
        val outFile = inputFile.parent.resolve(outFileName)
        outFile.writeText(ctxt.output)

        println("Wrote $outFile")
    }
}
