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

val originalMeasurements: List<Int> = input.split("\n").map { it.toInt() }

object Day1 {

  fun slidingWindowSamples(measurements: List<Int>, slidingWindowSize: Int): List<List<Int>> {
    val slidingWindowStartIndexes = 0..(measurements.size - slidingWindowSize)
    return slidingWindowStartIndexes.map {
      measurements.subList(it, it + slidingWindowSize)
    }
  }

  fun countIncreases(measurements: List<Int>): Int {
    return measurements.zipWithNext().filter({it.first < it.second}).count()
  }

  fun part1(measurements: List<Int>): Int {
    return countIncreases(measurements)
  }

  fun part2(measurements: List<Int>): Int {
    val slidingWindowSums = slidingWindowSamples(measurements, 3).map { it.sum() }
    return countIncreases(slidingWindowSums)
  }
}

fun main() {
  println(Day1.part1(originalMeasurements))
  println(Day1.part2(originalMeasurements))
}