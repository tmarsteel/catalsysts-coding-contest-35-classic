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
        "var" -> parseVariableDeclaration()
        "set" -> parseVariableAssignment()
        "postpone" -> parsePostpone()
        else -> throw expected("print, if", token)
    }
}

fun TransactionSequence<String>.parsePrint(): PrintStatement {
    check(next() == "print")
    return PrintStatement(parseExpression())
}

fun TransactionSequence<String>.parseIfElse(): IfElseStatement {
    check(next() == "if")

    val condition = parseExpression()
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

    return ReturnStatement(parseExpression())
}

fun TransactionSequence<String>.parseExpression(): Expression {
    return ExpressionImpl(next())
}

fun TransactionSequence<String>.parseVariableDeclaration(): VariableDeclaration {
    check(next() == "var")
    return VariableDeclaration(next(), parseExpression())
}

fun TransactionSequence<String>.parseVariableAssignment(): VariableAssignment {
    check(next() == "set")
    return VariableAssignment(next(), parseExpression())
}

fun TransactionSequence<String>.parsePostpone(): PostponeStatement {
    check(next() == "postpone")

    val statements = mutableListOf<Statement>()

    while (peek() != "end") {
        statements.add(parseStatement())
    }
    next()

    return PostponeStatement(statements)
}

fun expected(e: String, got: String) = ParseException("Expected $e bug got $got")

class ParseException(message: String, cause: Throwable? = null): RuntimeException(message, cause)
