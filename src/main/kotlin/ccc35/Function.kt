package ccc35

class Function(
    val statements: List<Statement>
) {
    fun execute(ctxt: RootExecutionContext): Value? {
        val functionContext = ExecutionQueueContext(FunctionExecutionContext(ctxt))
        functionContext.postpone(statements)
        try {
            functionContext.execute()
        }
        catch (ex: FunctionReturnException) {
            return ex.value
        }

        return null
    }
}
