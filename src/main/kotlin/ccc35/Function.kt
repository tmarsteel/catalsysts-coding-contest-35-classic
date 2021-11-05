package ccc35

class Function(
    val statements: List<Statement>
) {
    fun execute(ctxt: RootExecutionContext): String? {
        val functionContext = FunctionExecutionContext(ctxt)
        ctxt.postpone(statements)
        ctxt.execute()

        return null
    }
}
