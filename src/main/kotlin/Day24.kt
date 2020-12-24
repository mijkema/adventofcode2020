package main

import java.io.File

fun main() {
    val tileDirections = File("inputs/Day24").readLines()
    var blackTiles = mutableSetOf<Pair<Int, Int>>()
    tileDirections.forEach { s ->
        var current = Pair(0, 0)
        val inst = "((?:se|ne|e|w|nw|sw)),?".toRegex().findAll(s).map { m -> m.groupValues[1] }.toList()
        inst.forEach { dir ->
            current = when (dir) {
                "e" -> Pair(current.first + 2, current.second)
                "se" -> Pair(current.first + 1, current.second - 1)
                "sw" -> Pair(current.first - 1, current.second - 1)
                "w" -> Pair(current.first - 2, current.second)
                "nw" -> Pair(current.first - 1, current.second + 1)
                "ne" -> Pair(current.first + 1, current.second + 1)
                else -> throw RuntimeException()
            }
        }
        if (blackTiles.contains(current)) {
            blackTiles.remove(current)
        } else {
            blackTiles.add(current)
        }
    }

    println("Solution: ${blackTiles.size}")

    // part 2
    (1..100).forEach {
        val newSet = mutableSetOf<Pair<Int, Int>>()
        blackTiles.flatMap { t -> getNeighbourTiles(t) }.distinct()
            .forEach { t ->
                val blackNeighbours = getNeighbourTiles(t).count { blackTiles.contains(it) }
                if (blackTiles.contains(t) && (blackNeighbours == 1 || blackNeighbours == 2)) {
                    newSet.add(t)
                } else if (!blackTiles.contains(t) && blackNeighbours == 2) {
                    newSet.add(t)
                }
            }
        blackTiles = newSet
    }
    println("Solution: ${blackTiles.size}")
}

fun getNeighbourTiles(t: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(
        Pair(t.first + 2, t.second),
        Pair(t.first + 1, t.second - 1),
        Pair(t.first - 1, t.second - 1),
        Pair(t.first - 2, t.second),
        Pair(t.first - 1, t.second + 1),
        Pair(t.first + 1, t.second + 1)
    )
}
