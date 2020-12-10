package main

import java.io.File

fun main() {
    val numbers = mutableListOf<Int>()
    numbers.add(0)
    File("inputs/Day10").forEachLine { numbers.add(it.toInt()) }
    numbers.sort()
    numbers.add(numbers.last() + 3)
    var differencesOfOne = 0
    var differencesOfThree = 0
    numbers.windowed(2).forEach { window ->
        if (window[1] - window[0] == 1) differencesOfOne++
        if (window[1] - window[0] == 3) differencesOfThree++
    }

    // part 1
    println("solution: ${differencesOfOne * differencesOfThree}")

    // part 2
    var solutions = 1L
    var windowLength = 1
    numbers.windowed(2).forEach { window ->
        if (window[1] - window[0] == 1) {
            windowLength++
        } else {
            if (windowLength == 3) solutions *= 2
            if (windowLength == 4) solutions *= 4
            if (windowLength == 5) solutions *= 7
            windowLength = 1
        }
    }
    println("solutions: $solutions")

    // part 2, less hacky
    val countMap = mutableMapOf<Int, Long>()
    numbers.forEachIndexed loop@{ i, n ->
        if (i < 2) {
            countMap[i] = 1L
            return@loop
        }
        countMap[i] = countMap[i  - 1]!!
        numbers
                .subList(0, i - 1)
                .reversed()
                .takeWhile { j -> n - j <= 3 }
                .forEach {
                    countMap[i] = countMap[i]!! + countMap[numbers.indexOf(it)]!!
                }
    }
    println("solutions: ${countMap[numbers.size - 1]}")
}
