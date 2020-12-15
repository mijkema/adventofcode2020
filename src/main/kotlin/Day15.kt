package main

import java.io.File

fun main() {
    val input = File("inputs/Day15").readLines()[0].split(',').map(String::toLong)
    val list = mutableListOf<Long>()
    val occurrencesMap = mutableMapOf<Long, Int>()
    list.addAll(input)
    list.subList(0, list.size - 1).forEachIndexed { index, i -> occurrencesMap[i] = index }
    var index = input.size
    while (index < 30000000) {
        val previous = occurrencesMap.getOrDefault(list.last(), -1)
        occurrencesMap[list.last()] = list.size - 1
        val newNumber = if (previous == -1) 0 else list.size.toLong() - previous - 1
        list.add(newNumber)
        index++
    }
    println("2020th number: ${list[2019]}")
    println("30000000th number: ${list.last()}")
}
