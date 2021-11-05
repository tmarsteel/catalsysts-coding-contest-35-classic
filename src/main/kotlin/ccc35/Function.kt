package ccc35

class Function(
    val statements: List<Statement>
) {
    fun execute(ctxt: ExecutionContext): String? {
        for (statement in statements) {
            try {
                statement.execute(ctxt)
            }
            catch (ex: FunctionReturnException) {
                return ex.value
            }
        }

        return null
    }
}
