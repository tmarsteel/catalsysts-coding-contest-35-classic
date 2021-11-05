package ccc35

fun String.tokenize(): TransactionalSequence<String> {
    return TransactionalSequence(split(Regex("\\s")).iterator())
}
