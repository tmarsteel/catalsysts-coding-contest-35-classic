package ccc35

fun String.tokenize(): Sequence<String> {
    return split(Regex("\\s")).asSequence()
}
