package io.github.antivanov.aoc2021

object Day3 {
  val input = """
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".trimIndent()

  private fun computeBitFrequencies(measurements: List<List<Int>>): List<Int> {
    val measurementWidth = measurements[0].size
    return (0 until measurementWidth).map { bitIndex ->
      measurements.fold(0) { current, measurement ->
        current + measurement[bitIndex]
      }
    }
  }

  private fun frequenciesToMeasurement(bitFrequencies: List<Int>, bitTriggerLimit: Int, triggerLimitComparison: (Int, Int) -> Boolean): Int {
    val gammaBits = bitFrequencies.map {
      if (triggerLimitComparison(it, bitTriggerLimit))
        1
      else
        0
    }
    return gammaBits.joinToString(separator = "").toInt(2)
  }

  private fun computeGamma(bitFrequencies: List<Int>, bitTriggerLimit: Int): Int {
    return frequenciesToMeasurement(bitFrequencies, bitTriggerLimit) { frequency, limit ->
      frequency > limit
    }
  }

  private fun computeEpsilon(bitFrequencies: List<Int>, bitTriggerLimit: Int): Int {
    return frequenciesToMeasurement(bitFrequencies, bitTriggerLimit) { frequency, limit ->
      frequency <= limit
    }
  }

  fun part1(numbers: List<String>): Int {
    val measurements: List<List<Int>> = numbers.map { it.toCharArray().toList().map { it.toString().toInt() } }
    val frequencies = computeBitFrequencies(measurements)
    val bitTriggerLimit = measurements.size / 2

    val gamma = computeGamma(frequencies, bitTriggerLimit)
    val epsilon = computeEpsilon(frequencies, bitTriggerLimit)
    return gamma * epsilon
  }
}

fun main() {
  val numbers: List<String> = Day3.input.split("\n")
  println(Day3.part1(numbers))
}