package ccc35

fun main(vararg args: String) {
    var lines = Statement::class.java.getResourceAsStream("/inputs/level1/level1_5.in")!!.bufferedReader().readLines()
    val nLines = lines[0].toInt()
    lines = lines.subList(1, nLines + 1)

    val tokens = lines.joinToString("\n").tokenize()
    val function = tokens.parseProgram().single()
    function.execute()
}
