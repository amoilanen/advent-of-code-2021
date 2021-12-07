package io.github.antivanov.aoc2021

object Day7 {
  val input = "16,1,2,0,4,2,7,1,2,14"

  private fun fuelToMoveToPosition(crabPositions: List<Int>, position: Int): Pair<Int, Int> =
    position to crabPositions.map { Math.abs(it - position) }.sum()

  fun parseInput(input: String): List<Int> =
    input.split(",").map { it.toInt() }

  fun part1(crabPositions: List<Int>): Int {
    val positionRange = crabPositions.minOrNull()!!..crabPositions.maxOrNull()!!
    val positionsAndFuels = positionRange.map { fuelToMoveToPosition(crabPositions, it) }.sortedBy { it.second }
    val (optimalPosition, optimalFuel) = positionsAndFuels.first()
    return optimalFuel
  }
}

fun main() {
  val crabPositions = Day7.parseInput(Day7.input)
  println(Day7.part1(crabPositions))
}
