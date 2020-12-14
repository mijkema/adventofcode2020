package main

import java.io.File

fun main() {
    val entries = mutableListOf<String>()
    File("inputs/Day5").forEachLine { entries.add(it) }
    val seats = mutableListOf<Int>()
    for (seat in entries) {
        seats.add(
                convertBinaryToDecimal(
                        seat
                                .replace('B', '1')
                                .replace('F', '0')
                                .replace('R', '1')
                                .replace('L', '0')
                                .toLong()))
    }
    var highestSeat = 0
    seats.forEach { s -> highestSeat = if (s > highestSeat) s else highestSeat }
    println("highest Seat ID: $highestSeat")

    for (i in 0..1028) {
        if (!seats.contains(i)) {
            println("Empty seat: $i")
        }
    }

}
