package ccc35

class ExecutionContext {
    private val outBuffer = StringBuffer()

    val output: String
        get() = outBuffer.toString()

    fun output(text: String) {
        outBuffer.append(text)
    }
}
