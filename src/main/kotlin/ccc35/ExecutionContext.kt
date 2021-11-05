package ccc35

typealias OneBasedIndex = Int

interface ExecutionContext {
    fun appendOutput(text: String)
    fun hasVariable(varName: String): Boolean
    fun getVariableValue(varName: String): Value
    fun setVariableValue(varName: String, value: Value, initial: Boolean)
    fun postpone(statements: List<Statement>)
    fun executeQueue()
    fun getFunctionByIndex(index: OneBasedIndex): Function
}

class RootExecutionContext(
    private val functions: List<Function>,
) : ExecutionContext {
    private val outBuffer = StringBuffer()

    val output: String
        get() = outBuffer.toString()

    override fun appendOutput(text: String) {
        outBuffer.append(text)
    }

    override fun hasVariable(varName: String) = false

    override fun getVariableValue(varName: String): Value {
        throw ProgramRuntimeError("No variables in the root context")
    }

    override fun setVariableValue(varName: String, value: Value, initial: Boolean) {
        throw ProgramRuntimeError("No variables in the root context")
    }

    override fun postpone(statements: List<Statement>) {
        throw ProgramRuntimeError("No postponing in the root")
    }

    override fun executeQueue() {
        throw ProgramRuntimeError("This context does not support postponing")
    }

    override fun getFunctionByIndex(index: OneBasedIndex): Function {
        try {
            return functions[index - 1]
        } catch (ex: IndexOutOfBoundsException) {
            throw ProgramRuntimeError("Function #$index is not defined", ex)
        }
    }
}

class FunctionExecutionContext(
    private val function: Function,
    private val parent: ExecutionContext,
) : ExecutionContext by (parent) {
    private val variables = mutableMapOf<String, Value>()

    override fun hasVariable(varName: String): Boolean = varName in variables

    override fun getVariableValue(varName: String): Value {
        return variables[varName] ?: throw ProgramRuntimeError("Variable $varName not declared yet")
    }

    override fun setVariableValue(varName: String, value: Value, initial: Boolean) {
        if (initial && varName in variables) {
            throw ProgramRuntimeError("Variable $varName declared multiple times")
        }

        if (!initial && varName !in variables) {
            throw ProgramRuntimeError("Variable $varName not declared yet")
        }

        variables[varName] = value
    }
}

class ExecutionQueueContext(private val parent: ExecutionContext) : ExecutionContext by (parent) {
    private val executionQueue = ArrayDeque<Statement>()

    override fun executeQueue() {
        while (executionQueue.isNotEmpty()) {
            val statement = executionQueue.removeFirst()
            statement.execute(this)
        }
    }

    override fun postpone(statements: List<Statement>) {
        statements.forEach(executionQueue::addLast)
    }
}

class ProgramRuntimeError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
