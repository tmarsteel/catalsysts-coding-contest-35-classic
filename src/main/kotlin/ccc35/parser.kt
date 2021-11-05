package ccc35

fun TransactionSequence<String>.parseProgram(): List<Function> {
    val functions = mutableListOf<Function>()
    while (hasNext()) {
        functions.add(parseFunction())
    }

    return functions
}

fun TransactionSequence<String>.parseFunction(): Function {
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

fun TransactionSequence<String>.parseStatement(): Statement {
    return when(val token = peek()!!) {
        "print" -> parsePrint()
        "if" -> parseIfElse()
        "return" -> parseReturn()
        else -> throw expected("print, if", token)
    }
}

fun TransactionSequence<String>.parsePrint(): PrintStatement {
    check(next() == "print")
    return PrintStatement(next())
}

fun TransactionSequence<String>.parseIfElse(): IfElseStatement {
    check(next() == "if")

    val condition = next()
    val trueBranch = mutableListOf<Statement>()
    val elseBranch = mutableListOf<Statement>()

    while (peek() != "end") {
        trueBranch.add(parseStatement())
    }
    next()

    check(next() == "else")

    while (peek() != "end") {
        elseBranch.add(parseStatement())
    }
    next()

    return IfElseStatement(condition, trueBranch, elseBranch)
}

fun TransactionSequence<String>.parseReturn(): ReturnStatement {
    check(next() == "return")

    return ReturnStatement(next())
}

fun expected(e: String, got: String) = ParseException("Expected $e bug got $got")

class ParseException(message: String, cause: Throwable? = null): RuntimeException(message, cause)
