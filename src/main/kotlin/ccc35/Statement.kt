package ccc35

sealed class Statement {
    abstract fun execute(ctxt: ExecutionContext)
}

class PrintStatement(val value: String) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.output(value)
    }
}

class IfElseStatement(
    val condition: String,
    val trueBranch: List<Statement>,
    val elseBranch: List<Statement>
) : Statement() {
    override fun execute(ctxt: ExecutionContext) {
        val code = if (condition == "true") trueBranch else elseBranch
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

class FunctionReturnException(val value: String) : RuntimeException()
