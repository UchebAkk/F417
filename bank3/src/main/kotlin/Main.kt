package org.example

import kotlin.math.abs

fun main() {
    val n = readln().toInt()
    val points = mutableListOf<Pair<Int, Int>>()
    repeat(n) {
        val (x, y) = readln().split(" ").map { it.toInt() }
        points.add(x to y)
    }

    data class QuadrantInfo(var count: Int = 0, var minR: Int = Int.MAX_VALUE, var bestPoint: Pair<Int, Int>? = null)

    val quadrants = Array(4) { QuadrantInfo() }

    for ((x, y) in points) {
        if (x == 0 || y == 0) continue

        val quadrant = when {
            x > 0 && y > 0 -> 0
            x < 0 && y > 0 -> 1
            x < 0 && y < 0 -> 2
            x > 0 && y < 0 -> 3
            else -> continue
        }

        val info = quadrants[quadrant]
        info.count++
        val r = minOf(abs(x), abs(y))
        if (r < info.minR || (r == info.minR && info.bestPoint == null)) {
            info.minR = r
            info.bestPoint = x to y
        }
    }

    val bestQuadrant = quadrants.withIndex()
        .filter { it.value.count > 0 }
        .maxWithOrNull(compareBy<IndexedValue<QuadrantInfo>> { it.value.count }
            .thenBy { -it.value.minR }
            .thenBy { it.index }
        ) ?: throw IllegalStateException("No points in any quadrant")

    val k = bestQuadrant.index + 1
    val m = bestQuadrant.value.count
    val a = bestQuadrant.value.bestPoint!!
    val r = bestQuadrant.value.minR

    println("K = $k")
    println("M = $m")
    println("A = (${a.first}, ${a.second})")
    println("R = $r")
}