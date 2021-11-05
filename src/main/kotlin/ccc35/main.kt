package ccc35

import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readLines
import kotlin.io.path.writeText

fun main(vararg args: String) {
    val inputs = Paths.get("./src/main/resources/inputs/level5")
        .listDirectoryEntries("*.in")
        .map { it.toAbsolutePath() }
        .filter { "5_3" in it.fileName.toString() }

    for (inputFile in inputs) {
        println("Processing $inputFile")

        var lines = inputFile.readLines()
        val nLines = lines[0].toInt()
        lines = lines.subList(1, nLines + 1)

        val tokens = lines.joinToString("\n").tokenize()
        val functions = tokens.parseProgram()

        val currentFileOutput = StringBuffer()

        for ((zeroIndex, function) in functions.withIndex()) {
            val ctxt = RootExecutionContext(functions)
            val functionOutput = try {
                val returnValue = function.execute(ctxt)
                println("Function #${zeroIndex + 1} returned $returnValue")
                ctxt.output
            } catch (ex: ProgramRuntimeError) {
                ex.printStackTrace(System.err)
                "ERROR"
            } catch (ex: StackOverflowError) {
                System.err.println("stack overflow")
                "ERROR"
            }

            currentFileOutput.appendLine(functionOutput)
        }

        val outFileName = inputFile.fileName.toString().substringBeforeLast('.') + ".out.my"
        val outFile = inputFile.parent.resolve(outFileName)
        outFile.writeText(currentFileOutput)
        println("Wrote $outFile")

        val fmtOut = StringBuffer()
        functions.forEachIndexed { idx, fn ->
            fmtOut.appendLine("# function ${idx + 1}")
            fmtOut.append(fn.toSource())
            fmtOut.appendLine()
        }
        val fmtOutFile = inputFile.parent.resolve(inputFile.fileName.toString() + ".fmt")
        fmtOutFile.writeText(fmtOut.toString())
        println("Wrote $fmtOutFile")
    }
}
