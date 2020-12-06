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

fun convertBinaryToDecimal(binary: Long): Int {
    var num = binary
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}
