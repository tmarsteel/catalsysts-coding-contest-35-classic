package ccc35

class Function(
    val statements: List<Statement>
) {
    fun execute() {
        statements.forEach(Statement::execute)
    }
}
