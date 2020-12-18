package main

import main.Operation.*
import java.io.File

fun main() {
    val lines = File("inputs/Day18").readLines().map { s -> s.replace(" ", "") }
    // part 1
    println("sum: ${lines.map(::math).map { p -> p.first }.sum()}")

    // part 2
    println("sum: ${lines.map(::addBrackets).map(::math).map { p -> p.first }.sum()}")
}

fun addBrackets(input: String): String {
    var evaluatedPlusses = 0
    var total = input.count { c -> c == '+' }
    var newString = input
    while (evaluatedPlusses < total) {
        val i = newString.mapIndexedNotNull{i, c -> if (c == '+') i else null}[evaluatedPlusses]
        // add bracket left
        var leftBracketIndex = i - 1
        while (newString[leftBracketIndex].isDigit()) {
            leftBracketIndex--
            if (leftBracketIndex < 0) break
        }
        if (leftBracketIndex == i - 1) {
            // this must be a closing bracket
            var closeCount = 1
            while (closeCount != 0) {
                leftBracketIndex--
                if (newString[leftBracketIndex] == ')') closeCount++
                if (newString[leftBracketIndex] == '(') closeCount--
            }
        }

        // add bracket right
        var rightBracketIndex = i + 1
        while (newString[rightBracketIndex].isDigit()) {
            rightBracketIndex++
            if (rightBracketIndex >= newString.length) break
        }
        if (rightBracketIndex == i + 1) {
            // this must be a closing bracket
            var openCount = 1
            while (openCount != 0) {
                rightBracketIndex++
                if (newString[rightBracketIndex] == ')') openCount--
                if (newString[rightBracketIndex] == '(') openCount++
            }
        }
        newString =
            newString.substring(0, leftBracketIndex+1) +
                    "(" + newString.substring(leftBracketIndex + 1, rightBracketIndex) + ")" +
                    newString.substring(rightBracketIndex)
        evaluatedPlusses++
    }
    return newString
}

fun math(input: String): Pair<Long, Int> {
    var index = 0
    var current = 0L
    var currentOp = ADD
    var nextPart = nextPart(input)
    while (nextPart != null) {
        index += nextPart.length()
        when (nextPart.op) {
            CLOSE -> return Pair(current, index)
            OPEN -> {
                val (subExpression, subExpressionLength) = math(input.substring(index))
                if (currentOp == ADD) {
                    current += subExpression
                } else {
                    current *= subExpression
                }
                index += subExpressionLength
            }
            ADD -> currentOp = ADD
            MULT -> currentOp = MULT
            NUM -> {
                if (currentOp == ADD) {
                    current += nextPart.num!!
                } else {
                    current *= nextPart.num!!
                }
            }
        }
        nextPart = nextPart(input.substring(index))
    }
    return Pair(current, index)
}

fun nextPart(input: String): Part? {
    var index = 0
    var number = ""
    if (input.length <= index) return null
    while (input[index].isDigit()) {
        number += input[index]
        index++
        if (input.length <= index) break
    }
    if (number.isNotEmpty()) return Part(NUM, number.toInt())
    return when (input[index]) {
        '+' -> Part(ADD)
        '*' -> Part(MULT)
        '(' -> Part(OPEN)
        ')' -> Part(op = CLOSE)
        else -> throw RuntimeException()
    }
}

enum class Operation {
    ADD,
    MULT,
    OPEN,
    CLOSE,
    NUM
}

class Part(val op: Operation, val num: Int? = null) {
    fun length(): Int {
        if (op != NUM) return 1
        return num.toString().length
    }

    override fun toString(): String {
        return "Part(op=$op, num=$num)"
    }
}
