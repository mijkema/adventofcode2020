package main

import java.io.File

fun main() {
    val passports = mutableListOf<Passport>()
    var currentPassport = mutableMapOf<String, String>()
    File("inputs/Day4").forEachLine {
        if (it.isEmpty()) {
            passports.add(Passport(currentPassport))
            currentPassport = mutableMapOf()
        } else {
            it.split(' ').forEach { s -> val x = s.split(':'); currentPassport[x[0]] = x[1] }
        }
    }

    val validPassports = passports.filter { p -> p.allRequiredFields() }.filter { p -> p.isValid() }.count()
    println("valid passports: $validPassports")
}


