package main

fun main() {
    val input = "942387615".chunked(1).map{c -> c.toInt()}.toMutableList()
    var highest = input.maxOrNull()!!
    var map = input.mapIndexed{ i, v -> v to input[(i + 1) % highest]  }.toMap().toMutableMap()

    var current = input.first()
    (1..100).forEach{
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
    var solution = "" + map[1]
    var next = map[map[1]]
    while (next != 1) {
        solution += next
        next = map[next]
    }
    println("solution: $solution")

    (10..1000000).forEach(input::add)
    highest = input.maxOrNull()!!
    map = input.mapIndexed{ i, v -> v to input[(i + 1) % highest]  }.toMap().toMutableMap()

    println(map.size)
    current = input.first()
    (1..10000000).forEach{
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
        if (it % 1000000 == 0) {
            println(it)
        }
    }
    val i1 = map[1]!!
    val i2 = map[i1]!!
    println("solution: $i1 * $i2 = ${i1.toLong()*i2.toLong()}")

}
