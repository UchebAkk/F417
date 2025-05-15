package org.example

fun main() {
    val n = readln().toInt()
    val t = readln().toInt()
    var timeA = 0 // начальное время в A0
    var timeB = t // начальное время в B0 (переезд A0 -> B0)

    repeat(n) {
        val (a, b) = readln().split(" ").map { it.toInt() }

        val newTimeA = timeA + a
        val newTimeB = minOf(timeB + b, timeA + b + t)

        timeA = newTimeA
        timeB = newTimeB
    }
    println(minOf(timeA, timeB))
}