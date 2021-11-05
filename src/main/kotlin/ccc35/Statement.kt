package ccc35

sealed class Statement {
    abstract fun execute(ctxt: ExecutionContext)
}

class PrintStatement(val value: Expression) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.appendOutput(value.evaluate(ctxt).value)
    }
}

class IfElseStatement(
    val condition: Expression,
    val trueBranch: List<Statement>,
    val elseBranch: List<Statement>
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        val conditionResult = condition.evaluate(ctxt).value
        val code = when (conditionResult) {
            "true" -> trueBranch
            "false" -> elseBranch
            else -> throw ProgramRuntimeError("If condition did not resolve to boolean, got $conditionResult")
        }

        val nestedContext = ExecutionQueueContext(ctxt)
        nestedContext.postpone(code)
        nestedContext.execute()
    }
}

class ReturnStatement(
    val value: Expression
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        throw FunctionReturnException(value.evaluate(ctxt))
    }
}

class VariableDeclaration(
    val variableName: String,
    val value: Expression
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.setVariableValue(variableName, value.evaluate(ctxt), true)
    }
}

class VariableAssignment(
    val variableName: String,
    val value: Expression
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.setVariableValue(variableName, value.evaluate(ctxt), false)
    }
}

class PostponeStatement(
    val statements: List<Statement>
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.postpone(statements)
    }
}

class FunctionReturnException(val value: Value) : RuntimeException()
