package ccc35

import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readLines
import kotlin.io.path.writeText

fun main(vararg args: String) {
    val inputs = Paths.get("./src/main/resources/inputs/level4")
        .listDirectoryEntries("*.in")
        .map { it.toAbsolutePath() }
        // .filter { "example" in it.fileName.toString() }

    for (inputFile in inputs) {
        println("Processing $inputFile")

        var lines = inputFile.readLines()
        val nLines = lines[0].toInt()
        lines = lines.subList(1, nLines + 1)

        val tokens = lines.joinToString("\n").tokenize()
        val functions = tokens.parseProgram()

        val currentFileOutput = StringBuffer()

        for (function in functions) {
            val ctxt = RootExecutionContext()
            val functionOutput = try {
                function.execute(ctxt)
                ctxt.output
            }
            catch (ex: ProgramRuntimeError) {
                ex.printStackTrace(System.err)
                "ERROR"
            }

            currentFileOutput.appendLine(functionOutput)
        }

        val outFileName = inputFile.fileName.toString().substringBeforeLast('.') + ".out.my"
        val outFile = inputFile.parent.resolve(outFileName)
        outFile.writeText(currentFileOutput)

        println("Wrote $outFile")
    }
}
