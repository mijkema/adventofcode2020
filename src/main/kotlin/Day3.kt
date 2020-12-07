package main

import java.io.File

fun main() {
    val lines = mutableListOf<String>()
    File("inputs/Day3").forEachLine { lines.add(it) }
    val result = listOf(
        getTrees(1, 1, lines),
        getTrees(3, 1, lines),
        getTrees(5, 1, lines),
        getTrees(7, 1, lines),
        getTrees(1, 2, lines)
    )
    println("individual results: $result")
    
    println("sum: ${result.reduce { acc, i -> i * acc }}")

}

fun getTrees(slopeX: Int, slopeY: Int, lines: MutableList<String>): Long {
    var x = 0
    var y = 0
    var trees = 0L
    while (y < lines.count()) {
        val char = lines[y].elementAt(x % lines[y].length)
        trees += if (char == '#') {
            1
        } else {
            0
        }
        x += slopeX
        y += slopeY
    }
    return trees
}
