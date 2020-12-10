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
    numbers.windowed(2).forEach{ window ->
        if (window[1] - window[0] == 1) differencesOfOne++
        if (window[1] - window[0] == 3) differencesOfThree++
    }

    // part 1
    println("solution: ${differencesOfOne * differencesOfThree}")

    // part 2
    var solutions = 1L
    var windowLength = 1
    numbers.windowed(2).forEach{ window ->
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
}
