package io.github.antivanov.aoc2021

val input = """
199
200
208
210
200
207
240
269
260
263
""".trimIndent()

object Day1 {
  fun countIncreases(measurements: List<Int>): Int {
    return measurements.zipWithNext().filter({it.first < it.second}).count()
  }

  fun part1(input: String): Int {
    val measurements: List<Int> = input.split("\n").map { it.toInt() }
    return countIncreases(measurements)
  }
}

fun main() {
  println(Day1.part1(input))
}