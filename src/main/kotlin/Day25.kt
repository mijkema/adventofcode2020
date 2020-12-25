package main

fun main() {
    val i1 = 14205034L
    val i2 = 18047856L

    // transform subject number
    val i1LoopSize = getLoopSize(i1)
    val i2LoopSize = getLoopSize(i2)
    println("loop size $i1: $i1LoopSize, $i2: $i2LoopSize")
    println("Solution: ${transform(i1, i2LoopSize)}, ${transform(i2, i1LoopSize)}")
}

fun getLoopSize(number: Long): Long {
    var value = 1L
    val subjectNumber = 7L
    var index = 1L
    while (true) {
        value *= subjectNumber
        value = value.rem(20201227)
        if (value == number) return index
        index++
    }
}

fun transform(number: Long, loopSize: Long): Long {
    var value = 1L
    var index = 0L
    while (index < loopSize) {
        value *= number
        value = value.rem(20201227)
        index++
    }
    return value
}
