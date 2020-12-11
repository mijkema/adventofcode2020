package main

import main.LocationType.*
import java.io.File
import java.lang.RuntimeException

fun main() {
    var locations = mutableListOf<List<LocationType>>()
    File("inputs/Day11").forEachLine {
        locations.add(it.map { c -> LocationType.fromChar(c) })
    }

    // part 1
    var samePart1 = false
    while (!samePart1) {
        val newList = getNewList(locations, 4, ::getAdjacent)
        samePart1 = newList == locations
        locations = newList
    }
    println("occupied1: ${locations.flatMap { r -> r }.count { l -> l == OCCUPIED }}")


    locations = mutableListOf()
    File("inputs/Day11").forEachLine {
        locations.add(it.map { c -> LocationType.fromChar(c) })
    }
    // part 2
    var samePart2 = false
    while (!samePart2) {
        val newList = getNewList(locations, 5, ::getAdjacent2)
        samePart2 = newList == locations
        locations = newList
    }
    println("occupied2: ${locations.flatMap { r -> r }.count { l -> l == OCCUPIED }}")
}

fun getNewList(
        locations: MutableList<List<LocationType>>,
        occupiedCount: Int,
        method: (MutableList<List<LocationType>>, Int, Int) -> List<LocationType>): MutableList<List<LocationType>> {
    val result = mutableListOf<List<LocationType>>()
    locations.forEachIndexed { row, cols ->
        val newRow = mutableListOf<LocationType>()
        cols.forEachIndexed loop@{ i, col ->
            if (col == FLOOR) {
                newRow.add(FLOOR)
                return@loop
            }
            val adjacent = method(locations, row, i)
            if (col == EMPTY) {
                newRow.add(if (adjacent.count { l -> l == OCCUPIED } == 0) OCCUPIED else EMPTY)
                return@loop
            }
            newRow.add(if (adjacent.count { l -> l == OCCUPIED } >= occupiedCount) EMPTY else OCCUPIED)
        }
        result.add(newRow)
    }
    return result
}

fun getAdjacent(locations: MutableList<List<LocationType>>, row: Int, col: Int): List<LocationType> {
    val result = mutableListOf<LocationType>()
    (maxOf(row - 1, 0)..minOf(locations.size - 1, row + 1)).forEach { r ->
        (maxOf(col - 1, 0)..minOf(locations[0].size - 1, col + 1)).forEach { c ->
            if (c != col || r != row) result.add(locations[r][c])
        }
    }
    return result
}

fun getAdjacent2(locations: MutableList<List<LocationType>>, row: Int, col: Int): List<LocationType> {
    val result = mutableListOf<LocationType>()
    (-1..1).forEach{ rDir ->
        (-1..1).forEach loop@{ cDir ->
            if (rDir == 0 && cDir == 0) return@loop
            var distance = 1
            var loc = locations.getOrElse(row + (distance * rDir)) { emptyList() }.getOrNull(col + (distance * cDir))
            while (loc == FLOOR) {
                distance += 1
                loc = locations.getOrElse(row + (distance * rDir)) { emptyList() }.getOrNull(col + (distance * cDir))
                if (loc == null) return@loop
            }
            if (loc != null) result.add(loc)
        }
    }
    return result
}

enum class LocationType(private val symbol: String) {
    FLOOR("."),
    EMPTY("L"),
    OCCUPIED("#");

    override fun toString(): String {
        return symbol
    }

    companion object {
        fun fromChar(char: Char) = when (char) {
            '.' -> FLOOR
            'L' -> EMPTY
            '#' -> OCCUPIED
            else -> throw RuntimeException()
        }
    }

}
