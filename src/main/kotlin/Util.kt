package main

import kotlin.math.pow

fun convertBinaryToDecimal(binary: Long): Int {
    var num = binary
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * 2.0.pow(i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}

fun toDecimal(binaryNumber : String) : Long {
    var sum = 0L
    binaryNumber.reversed().forEachIndexed {
        k, v -> sum += v.toString().toInt() * 2.0.pow(k.toDouble()).toLong()
    }
    return sum
}
