package ccc35

sealed class Statement {
    abstract fun execute(ctxt: ExecutionContext)
}

class PrintStatement(val value: Expression) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.output(value.evaluate(ctxt).value)
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
        code.forEach { it.execute(ctxt) }
    }
}

class ReturnStatement(
    val value: String
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        throw FunctionReturnException(value)
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

class FunctionReturnException(val value: String) : RuntimeException()
