package ccc35

interface ExecutionContext {
    fun output(text: String)
    fun hasVariable(varName: String): Boolean
    fun getVariableValue(varName: String): Value
    fun setVariableValue(varName: String, value: Value, initial: Boolean)
}

class RootExecutionContext : ExecutionContext {
    private val outBuffer = StringBuffer()

    val output: String
        get() = outBuffer.toString()

    override fun output(text: String) {
        outBuffer.append(text)
    }

    override fun hasVariable(varName: String): Boolean = false

    override fun getVariableValue(varName: String): Value {
        throw ProgramRuntimeError("No variables in the root context")
    }

    override fun setVariableValue(varName: String, value: Value, initial: Boolean) {
        throw ProgramRuntimeError("No variables in the root context")
    }
}

class FunctionExecutionContext(private val root: RootExecutionContext) : ExecutionContext by (root) {
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

class ProgramRuntimeError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
