package ccc35

sealed class Statement {
    abstract fun execute()
}
class PrintStatement(val value: String) : Statement() {
    override fun execute() {
        print(value)
    }
}
