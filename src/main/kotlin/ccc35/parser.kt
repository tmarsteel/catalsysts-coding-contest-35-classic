package ccc35

fun TransactionalSequence<String>.parseProgram(): List<Function> {
    val functions = mutableListOf<Function>()
    while (hasNext()) {
        functions.add(parseFunction())
    }

    return functions
}

fun TransactionalSequence<String>.parseFunction(): Function {
    mark()
    val startToken = next()
    if (startToken != "start") {
        rollback()
        throw expected("start", startToken)
    }

    val statements = mutableListOf<Statement>()

    while (hasNext()) {
        if (peek() == "end") {
            next()
            break
        }
        statements.add(parseStatement())
    }

    commit()
    return Function(statements)
}

fun TransactionalSequence<String>.parseStatement(): Statement {
    check(next() == "print")
    return PrintStatement(next())
}

fun expected(e: String, got: String) = IllegalArgumentException("Expected $e bug got $got")
