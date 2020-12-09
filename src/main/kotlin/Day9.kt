package main

import java.io.File
import java.lang.RuntimeException

fun main() {
    val numbers = mutableListOf<Long>()
    File("inputs/Day9").forEachLine { numbers.add(it.toLong()) }
    val invalid = findInvalid(numbers, 25)
    println("invalid: $invalid")
    val contiguous = findContiguous(numbers, invalid)
    println("smallest + largest: ${contiguous.minOrNull()!! + contiguous.maxOrNull()!!}")
}

fun findContiguous(numbers: MutableList<Long>, invalid: Long): List<Long> {
    loop@ for (i in numbers.indices) {
        var current = 0L
        for (j in numbers.subList(i, numbers.size)) {
            current += j
            if (current > invalid) continue@loop
            if (current == invalid) {
                return numbers.subList(i, numbers.indexOf(j) + 1)
            }
        }
    }
    throw RuntimeException("No contiguous set for $numbers")
}

fun findInvalid(numbers: List<Long>, preamble: Int): Long {
    loop@ for (i in numbers.indices) {
        if (i < preamble) continue@loop
        for (p in getPairs(numbers.subList(i - preamble, i))) {
            if (numbers[i] == p.first + p.second) continue@loop
        }
        return numbers[i]
    }
    throw RuntimeException("No invalid for $numbers")
}

fun getPairs(list: List<Long>): Sequence<Pair<Long, Long>> {
    return sequence {
        for (i in list.indices) {
            for (j in list.subList(i + 1, list.size)) {
                yield(Pair(list[i],j))
            }
        }
    }
}
