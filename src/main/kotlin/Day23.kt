package main

fun main() {
    val input = "942387615".chunked(1).map{c -> c.toInt()}.toMutableList()
    var map = play(input, 100)

    var solution = "" + map[1]
    var next = map[map[1]]
    while (next != 1) {
        solution += next
        next = map[next]
    }
    println("solution: $solution")

    (10..1000000).forEach(input::add)
    map = play(input, 10000000)
    println("solution: ${map[1]!!.toLong()*map[map[1]]!!.toLong()}")
}

fun play(input: MutableList<Int>, rounds: Int): Map<Int, Int> {
    val highest = input.maxOrNull()!!
    val map = input.mapIndexed{ i, v -> v to input[(i + 1) % highest]  }.toMap().toMutableMap()

    var current = input.first()
    (1..rounds).forEach{
        val x = map[current]!!
        val y = map[x]!!
        val z = map[y]!!
        var dest = if (current - 1 < 1) highest else current - 1
        while (dest == x || dest == y || dest == z) {
            dest = if (dest - 1 < 1) highest else dest - 1
        }
        val afterDest = map[dest]!!
        map[current] = map[z]!!
        map[dest] = x
        map[z] = afterDest
        current = map[current]!!
    }
    return map
}
