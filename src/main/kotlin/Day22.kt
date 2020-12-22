package main

import java.io.File

fun main() {
    var players = File("inputs/Day22")
        .readText()
        .split("\n\n")
        .map { p -> p.split(":\n")[0] to p.split(":\n")[1] }
        .map { e -> e.second.lines().filter(String::isNotEmpty).map(String::toInt).toMutableList() }

    // play
    while (!players.any { p -> p.isEmpty() }) {
        val first = players[0].removeFirst()
        val second = players[1].removeFirst()
        if (first > second) {
            players[0].add(first)
            players[0].add(second)
        } else {
            players[1].add(second)
            players[1].add(first)
        }
    }
    var solution = players.find { it.isNotEmpty() }!!.reversed().mapIndexed { i, v -> (i + 1) * v }.sum()

    println("solution: $solution")

    // part 2
    players = File("inputs/Day22")
        .readText()
        .split("\n\n")
        .map { p -> p.split(":\n")[0] to p.split(":\n")[1] }
        .map { e -> e.second.lines().filter(String::isNotEmpty).map(String::toInt).toMutableList() }

    // play
    val cache = mutableMapOf<MutableList<Int>, Pair<Int, MutableList<Int>>>()
    val winner = playRounds(players, cache)

    solution = winner.second.reversed().mapIndexed { i, v -> (i + 1) * v }.sum()

    println("solution: $solution")
}

fun playRounds(players: List<MutableList<Int>>, known: MutableMap<MutableList<Int>, Pair<Int, MutableList<Int>>>, depth: Int = 0): Pair<Int, MutableList<Int>> {
    if (known.contains(players[0])) {
        return known[players[0]]!!
    }
    val seen = mutableListOf<Pair<List<Int>, List<Int>>>()
    var player1Wins = false
    loop@ while (!players.any { p -> p.isEmpty() }) {
        if (seen.contains(Pair(players[0].toList(), players[1].toList()))) {
            player1Wins = true
            known[players[0]] = Pair(0, players[0])
            break@loop
        }
        seen.add(Pair(players[0].toList(), players[1].toList()))
        val first = players[0].removeFirst()
        val second = players[1].removeFirst()
        if (players[0].size >= first && players[1].size >= second) {
            val newGame = players.map { l -> l.toMutableList() }.toMutableList()
            newGame[0] = newGame[0].subList(0, first)
            newGame[1] = newGame[1].subList(0, second)
            val forCache = players[0].toMutableList()
            val recursiveResult = playRounds(newGame, known, depth + 1)
            known[forCache] = recursiveResult
            if (recursiveResult.first == 0) {
                players[0].add(first)
                players[0].add(second)
            } else {
                players[1].add(second)
                players[1].add(first)
            }
        } else {
            if (first > second) {
                players[0].add(first)
                players[0].add(second)
            } else {
                players[1].add(second)
                players[1].add(first)
            }
        }
    }
    if (player1Wins) return Pair(0, players[0])
    return Pair(players.indexOfFirst { it.isNotEmpty() }, players.find { it.isNotEmpty() }!!)
}
