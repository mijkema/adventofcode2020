package main

import java.io.File

fun main() {
    val (rs, lines) = File("inputs/Day19").readText().split("\n\n")
    val input = lines.lines().filter { l -> l.isNotEmpty() }

    val rules = rs.lines()
        .map { s -> s.split(':') }
        .map { p -> p[0].toInt() to p[1].substring(1) }
        .toMap()
        .toMutableMap()

    println("matches: ${input.filter { l -> matches(l, rules, rules[0]!!, false) }.count()}")

    rules[8] = "42 | 42 8"
    rules[11] = "42 31 | 42 11 31"
    println("matches: ${input.filter { l -> matches(l, rules, rules[0]!!, true) }.count()}")
}

fun matches(line: String, rules: Map<Int, String>, ruleToMatch: String, pt2: Boolean): Boolean {
    if (pt2) {
        // first 42 has to match at least once.
        var index = 0
        val firstMatch = matchSize(line, rules, rules[42]!!)
        if (firstMatch == 0) return false
        index += firstMatch
        var count1 = 0
        var loop = true
        while (loop) {
            val match = matchSize(line.substring(index), rules, rules[42]!!)
            if (match > 0) {
                count1++
                index += match
            } else {
                loop = false
            }
        }
        var matchedSecondHalf = false
        while (count1 > 0) {
            if (index == line.length) break
            val match = matchSize(line.substring(index), rules, rules[31]!!)
            if (match == 0) break
            matchedSecondHalf = true
            count1--
            index += match
        }
        return matchedSecondHalf && line.length == index
    } else {
        return matchSize(line, rules, ruleToMatch) == line.length
    }
}

fun matchSize(line: String, rules: Map<Int, String>, ruleToMatch: String): Int {
    if (ruleToMatch.contains("|")) {
        val (left, right) = ruleToMatch.split(" | ")
        val result = matchSize(line, rules, left)
        if (result > 0) return result
        return matchSize(line, rules, right)
    }
    var index = 0
    ruleToMatch.split(' ').forEach { r ->
        if (index == line.length) return 0
        if (r == "\"a\"" && line[index] == 'a') {
            index++
        } else if (r == "\"b\"" && line[index] == 'b') {
            index++
        } else if (r.toIntOrNull() != null) {
            val result = matchSize(line.substring(index), rules, rules[r.toInt()]!!)
            if (result == 0) return 0
            index += result
        } else {
            // no match
            return 0
        }
    }
    return index
}
