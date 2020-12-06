package main

import java.io.File

fun main() {
    val entries = mutableListOf<Int>()
    File("inputs/Day1").forEachLine { entries.add(it.toInt()) }
    for (i in entries) {
        for (j in entries) {
            if (i + j == 2020) {
                println("$i + $j = 2020")
                println("$i * $j = ${i*j}")
            }
            for (k in entries) {
                if (i + j + k == 2020) {
                    println("$i + $j + $k = 2020")
                    println("$i * $j * $k = ${i*j*k}")
                }
            }
        }
    }
}
