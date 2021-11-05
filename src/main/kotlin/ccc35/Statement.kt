package ccc35

import indentLinesBy

sealed interface Statement {
    fun execute(ctxt: ExecutionContext)
    fun toSource(): String
}

class PrintStatement(val value: Expression) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.appendOutput(value.evaluate(ctxt).value)
    }

    override fun toSource(): String {
        return "print ${value.toSource()}"
    }
}

class IfElseStatement(
    val condition: Expression,
    val trueBranch: List<Statement>,
    val elseBranch: List<Statement>,
) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        val conditionResult = condition.evaluate(ctxt).value
        val code = when (conditionResult) {
            "true" -> trueBranch
            "false" -> elseBranch
            else -> throw ProgramRuntimeError("If condition did not resolve to boolean, got $conditionResult")
        }

        val nestedContext = ExecutionQueueContext(ctxt)
        nestedContext.postpone(code)
        nestedContext.executeQueue()
    }

    override fun toSource(): String {
        val trueSource = trueBranch
            .joinToString("\n", transform = Statement::toSource)
            .indentLinesBy("  ")
        val elseSource = trueBranch.joinToString("\n", transform = Statement::toSource)
            .indentLinesBy("  ")
        return "if ${condition.toSource()}\n$trueSource\nend\nelse\n$elseSource\nend"
    }
}

class ReturnStatement(
    val value: Expression,
) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        throw FunctionReturnException(value.evaluate(ctxt))
    }

    override fun toSource() = "return ${value.toSource()}"
}

class VariableDeclaration(
    val variableName: String,
    val value: Expression,
) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.setVariableValue(variableName, value.evaluate(ctxt), true)
    }

    override fun toSource() = "var $variableName ${value.toSource()}"
}

class VariableAssignment(
    val variableName: String,
    val value: Expression,
) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.setVariableValue(variableName, value.evaluate(ctxt), false)
    }

    override fun toSource() = "set $variableName ${value.toSource()}"
}

class PostponeStatement(
    val statements: List<Statement>,
) : Statement {
    override fun execute(ctxt: ExecutionContext) {
        ctxt.postpone(statements)
    }

    override fun toSource(): String {
        val source = statements.joinToString("\n", transform = Statement::toSource)
        return "postpone\n${source.indentLinesBy("  ")}\nend"
    }
}

class CallStatement(
    val target: Expression,
) : Statement, Expression {
    override fun execute(ctxt: ExecutionContext) {
        evaluate(ctxt)
    }

    override fun evaluate(ctxt: ExecutionContext): Value {
        val targetIndexStr = target.evaluate(ctxt).value
        val targetIndex = targetIndexStr.toIntOrNull()
            ?: throw ProgramRuntimeError("Call target did not evaluate to an integer, got $targetIndexStr")

        val function = ctxt.getFunctionByIndex(targetIndex)
        return function.execute(ctxt)
            ?: Value("true")
    }

    override fun toSource(): String = "call ${target.toSource()}"
}

class FunctionReturnException(val value: Value) : RuntimeException()
