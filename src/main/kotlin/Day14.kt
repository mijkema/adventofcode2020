package main

import java.io.File

fun main() {
    val pattern = "mem\\[(\\d+)\\] = (\\d+)".toRegex()
    val lines = File("inputs/Day14").readLines()
    var mask = ""

    // part 1
    val memoryMap = mutableMapOf<Int, String>()
    lines.forEach {
        if (it.startsWith("mask =")) {
            mask = it.split(" = ")[1]
            return@forEach
        }
        val (address, value) = pattern.matchEntire(it)!!.destructured
        memoryMap[address.toInt()] = applyMask(value, mask)
    }
    println("part1: ${memoryMap.mapValues { e -> toDecimal(e.value) }.values.sum()}")

    // part 2
    val memoryMap2 = mutableMapOf<Long, Long>()
    lines.forEach {
        if (it.startsWith("mask =")) {
            mask = it.split(" = ")[1]
            return@forEach
        }
        val (address, value) = pattern.matchEntire(it)!!.destructured
        val result = applyMask2(address, mask)
        result.forEach { a -> memoryMap2[toDecimal(a)] = value.toLong() }
    }
    println("part2: ${memoryMap2.values.sum()}")
}

fun applyMask(value: String, mask: String): String {
    val bin = Integer.toBinaryString(value.toInt()).padStart(36, '0')
    return bin
            .mapIndexed { i, c -> if (mask[i] == 'X') c.toString() else mask[i].toString() }
            .joinToString("")
}

fun applyMask2(value: String, mask: String): List<String> {
    val bin = Integer.toBinaryString(value.toInt()).padStart(36, '0')
    val result = bin
            .mapIndexed { i, c -> if (mask[i] == '0') c.toString() else mask[i].toString() }
            .joinToString("")
    return getAllAddresses(result)
}

fun getAllAddresses(address: String): List<String> {
    val result = mutableListOf(address.toCharArray())
    address.forEachIndexed { i, c ->
        if (c != 'X') return@forEachIndexed
        val newList = result.map { ca -> ca.clone() }
        result.forEach { ca -> ca[i] = '0' }
        newList.forEach { ca -> ca[i] = '1' }
        result.addAll(newList)
    }
    return result.map { ca -> ca.concatToString() }
}
