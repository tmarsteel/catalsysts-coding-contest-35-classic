package ccc35

fun String.tokenize(): TransactionSequence<String> {
    return TransactionSequence(split(Regex("\\s+")).iterator())
}
