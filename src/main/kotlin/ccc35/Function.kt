package ccc35

import indentLinesBy

class Function(
    val selfIndex: OneBasedIndex,
    val statements: List<Statement>,
) {
    fun execute(ctxt: ExecutionContext): Value? {
        val functionContext = ExecutionQueueContext(FunctionExecutionContext(this, ctxt))
        functionContext.postpone(statements)
        try {
            functionContext.executeQueue()
        } catch (ex: FunctionReturnException) {
            return ex.value
        }

        return null
    }

    fun toSource(): String {
        val source = statements
            .joinToString("\n", transform = Statement::toSource)
            .indentLinesBy("  ")
        return "start\n$source\nend"
    }
}
