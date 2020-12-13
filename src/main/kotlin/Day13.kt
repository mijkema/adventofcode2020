package main

import java.io.File

fun main() {
    val lines = File("inputs/Day13").readLines()
    val startingTime = lines[0].toInt()
    val busDepartureTimes = lines[1].split(',')

    var waitingTime = Int.MAX_VALUE
    var busTime = 0
    busDepartureTimes.forEach {
        if (it == "x") return@forEach
        val schedule = it.toInt()
        var current = schedule - startingTime.rem(schedule)
        if (current < waitingTime) {
            waitingTime = current
            busTime = schedule
        }
    }
    println("shortest: $busTime ($waitingTime), result: ${busTime * waitingTime}")

    // part 2
    var busTimes = mutableListOf<Pair<Int, Int>>()
    busDepartureTimes.forEachIndexed{ i, s ->
        if (s == "x") return@forEachIndexed
        val schedule = s.toInt()
        busTimes.add(Pair(i, schedule))
    }

    var position = 0L
    var offset = 1L
    busTimes.forEach {
        while ((position + it.first).rem(it.second) != 0L) {
            position += offset
        }
        offset *= it.second
    }
    println("solution part 2: $position")
}
