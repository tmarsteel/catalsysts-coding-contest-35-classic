package ccc35

sealed interface Expression {
    fun evaluate(ctxt: ExecutionContext): Value
    fun toSource(): String
}

class ExpressionImpl(val variableName: String) : Expression {
    override fun evaluate(ctxt: ExecutionContext): Value {
        if (ctxt.hasVariable(variableName)) {
            return ctxt.getVariableValue(variableName)
        } else {
            return Value(variableName)
        }
    }

    override fun toSource(): String = variableName
}
