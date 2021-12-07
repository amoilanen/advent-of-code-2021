package io.github.antivanov.aoc2021

import kotlin.math.abs

object Day7 {
  val input = "16,1,2,0,4,2,7,1,2,14"

  fun parseInput(input: String): List<Int> =
    input.split(",").map { it.toInt() }

  private fun constantFuelConsumptionLaw(from: Int, to: Int): Int =
    abs(from - to)

  private fun linearFuelConsumptionLaw(from: Int, to: Int): Int {
    val delta = abs(from - to)
    return (delta * (delta + 1)) / 2
  }

  private fun fuelToMoveToPosition(crabPositions: List<Int>, position: Int, fuelConsumptionLaw: (Int, Int) -> Int): Pair<Int, Int> =
    position to crabPositions.map {
      fuelConsumptionLaw(it, position)
    }.sum()

  private fun findCommonPositionWithOptimalFuelConsumption(crabPositions: List<Int>, fuelConsumptionLaw: (Int, Int) -> Int): Int {
    val positionRange = crabPositions.minOrNull()!!..crabPositions.maxOrNull()!!
    val positionsAndFuels = positionRange.map {
      fuelToMoveToPosition(
        crabPositions,
        it,
        fuelConsumptionLaw
      )
    }.sortedBy { it.second }
    val (_, optimalFuel) = positionsAndFuels.first()
    return optimalFuel
  }

  fun part1(crabPositions: List<Int>): Int =
    findCommonPositionWithOptimalFuelConsumption(crabPositions, ::constantFuelConsumptionLaw)

  fun part2(crabPositions: List<Int>): Int =
    findCommonPositionWithOptimalFuelConsumption(crabPositions, ::linearFuelConsumptionLaw)
}

fun main() {
  val crabPositions = Day7.parseInput(Day7.input)
  println(Day7.part1(crabPositions))
  println(Day7.part2(crabPositions))
}
