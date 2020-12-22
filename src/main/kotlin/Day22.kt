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
    val winner = playRounds(players)

    solution = winner.second.reversed().mapIndexed { i, v -> (i + 1) * v }.sum()

    println("solution: $solution")
}

fun playRounds(players: List<MutableList<Int>>): Pair<Int, MutableList<Int>> {
    val seen = mutableListOf<List<Int>>()
    var player1Wins = false
    loop@ while (!players.any { p -> p.isEmpty() }) {
        if (seen.contains(players[0].toList())) {
            player1Wins = true
            break@loop
        }
        seen.add(players[0].toList())
        val first = players[0].removeFirst()
        val second = players[1].removeFirst()
        if (players[0].size >= first && players[1].size >= second) {
            val newGame = players.map { l -> l.toMutableList() }.toMutableList()
            newGame[0] = newGame[0].subList(0, first)
            newGame[1] = newGame[1].subList(0, second)
            val recursiveResult = playRounds(newGame)
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
