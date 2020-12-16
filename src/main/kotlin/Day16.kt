package main

import com.google.common.collect.Range
import com.google.common.collect.RangeSet
import com.google.common.collect.TreeRangeSet
import java.io.File

fun main() {
    val content = File("inputs/Day16").readText()
    val (ruleString, tickets) = content.split("\nyour ticket:\n")
    val ranges = TreeRangeSet.create<Int>()
    val rules = mutableMapOf<String, RangeSet<Int>>()
    ruleString.lines().filter(String::isNotEmpty).forEach {
        val (name, rangeString) = it.split(": ")
        val rangeSet = TreeRangeSet.create<Int>()
        rangeString
            .split(" or ")
            .map { s -> Range.closed(s.split('-')[0].toInt(), s.split('-')[1].toInt()) }
            .forEach(rangeSet::add)
        ranges.addAll(rangeSet)
        rules[name] = rangeSet
    }
    val (myTicketString, nearbyTicketsString) = tickets.split("\n\nnearby tickets:\n")
    val myTicket = myTicketString.split(',').map(String::toLong)
    val otherTickets = nearbyTicketsString
        .lines()
        .filter(String::isNotEmpty)
        .map{t -> t.split(',').map(String::toInt)}

    // part 1
    println("invalid: ${otherTickets.flatten().filter { i -> !ranges.contains(i) }.sum()}")

    // part 2
    val validTickets = otherTickets.filter { t -> t.all( ranges::contains) }
    val verticalLists = validTickets[0].indices.map { i -> validTickets.map { t -> t[i] } }
    val fields = mutableMapOf<String, Int>()
    while (fields.size < rules.size) {
        rules.forEach { (name, range) ->
            val indices = verticalLists
                .mapIndexedNotNull { i, l -> if (l.all(range::contains)) i else null }
                .filter { i -> !fields.containsValue(i) }
            if (indices.size == 1) {
                fields[name] = indices[0]
            }
        }
    }
    val solution = fields
        .filterKeys { k -> k.startsWith("departure") }
        .values
        .map { i -> myTicket[i] }
        .reduce(Long::times)
    println("solution: $solution")
}
