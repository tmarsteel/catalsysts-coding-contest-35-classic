package ccc35

class Function(
    val statements: List<Statement>
) {
    fun execute(ctxt: RootExecutionContext): String? {
        val functionContext = FunctionExecutionContext(ctxt)

        for (statement in statements) {
            try {
                statement.execute(functionContext)
            }
            catch (ex: FunctionReturnException) {
                return ex.value
            }
        }

        return null
    }
}
