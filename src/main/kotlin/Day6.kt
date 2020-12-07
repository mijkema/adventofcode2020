package main

import java.io.File

fun main() {
    val groups = mutableListOf<List<String>>()
    var current = mutableListOf<String>()
    File("inputs/Day6").forEachLine {
        if (it.isBlank()) {
            groups.add(current)
            current = mutableListOf()
        } else {
            current.add(it)
        }
    }
    groups.add(current)

    // Part 1
    val sum = groups.map { g -> g.flatMap { i -> i.toList() }.toSet().size }.sum()
    println("sum (part 1): $sum")

    // Part 2
    var count = 0
    for (group in groups) {
        val answersWithCounts = mutableMapOf<Char, Int>()
        group.map { g ->
            g.toCharArray().forEach { c ->
                answersWithCounts.compute(c) { _, v ->
                    (v ?: 0) + 1
                }
            }
        }
        count += answersWithCounts.filter { e -> e.value == group.size }.size
    }
    println("count (part 2): $count")
}
