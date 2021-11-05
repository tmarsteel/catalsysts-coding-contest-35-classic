package ccc35

sealed class Expression {
    abstract fun evaluate(ctxt: ExecutionContext): Value
}

class ExpressionImpl(val variableName: String) : Expression() {
    override fun evaluate(ctxt: ExecutionContext): Value {
        if (ctxt.hasVariable(variableName)) {
            return ctxt.getVariableValue(variableName)
        } else {
            return Value(variableName)
        }
    }
}
