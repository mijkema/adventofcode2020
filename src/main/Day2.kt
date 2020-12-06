package main

import java.io.File

fun main() {
    var valid = 0
    File("inputs/Day2").forEachLine {
        val parts = it.split(':')
        val policy = parts[0].split(' ')
        val bounds = policy[0].split('-').map { it.toInt() }
        val character = policy[1].single()
        val pattern = parts[1]
        if (validPattern(bounds, character, pattern)) {
            valid++
        }
        println("bounds: $bounds, character: $character, pattern: $pattern")
    }
    print("valid patterns: $valid")
}

fun validPattern(bounds: List<Int>, character: Char, pattern: String): Boolean {
    return (pattern.elementAt(bounds[0]) == character).xor(
            pattern.elementAt(bounds[1]) == character
    )
}
