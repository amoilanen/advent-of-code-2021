package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Some
import arrow.core.flattenOption

object Day15 {
  val input = """
    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
  """.trimIndent()

  data class Point(val x: Int, val y: Int)

  data class Grid(val values: Array<Array<Int>>) {
    companion object {
      const val MaxPossibleValue = 9
    }

    val width = values[0].size
    val height = values.size

    private fun incrementValue(value: Int, increment: Int): Int {
      val updated = value + increment
      return if (updated <= MaxPossibleValue)
        updated
      else
        updated % MaxPossibleValue
    }

    private fun replicateHorizontally(times: Int): Grid {
      val replicatedHorizontally = values.map { row ->
        (1 until times).fold(row) { result, increment ->
          result + row.map { incrementValue(it, increment) }
        }
      }.toTypedArray()
      return Grid(replicatedHorizontally)
    }

    private fun replicateVertically(times: Int): Grid {
      val replicatedVertically = (1 until times).fold(values) { result, increment ->
        val allValuesIncremented = (0 until height).map { y ->
          (0 until width).map { x ->
            incrementValue(values[y][x], increment)
          }.toTypedArray()
        }.toTypedArray()
        result + allValuesIncremented
      }
      return Grid(replicatedVertically)
    }

    fun replicate(times: Int): Grid  =
      replicateHorizontally(times).replicateVertically(times)

    fun getAdjacentPoints(p: Point): List<Point> {
      val top = if (p.y > 0) Some(p.copy(y = p.y - 1)) else None
      val bottom = if (p.y < height - 1) Some(p.copy(y = p.y + 1)) else None
      val left = if (p.x > 0) Some(p.copy(x = p.x - 1)) else None
      val right = if (p.x < width - 1) Some(p.copy(x = p.x + 1)) else None
      return listOf(top, bottom, left, right).flattenOption()
    }

    override fun toString(): String =
      (0 until height).map { y ->
        (0 until width).map { x ->
          values[y][x]
        }.joinToString(" ")
      }.joinToString("\n")
  }

  fun computeLowestPathRisks(riskGrid: Grid): Grid {
    val lowestPathRisks = (0 until riskGrid.height).map { _ ->
      IntArray(riskGrid.width).map { _ -> Int.MAX_VALUE - Grid.MaxPossibleValue }.toTypedArray()
    }.toTypedArray()

    lowestPathRisks[0][0] = riskGrid.values[0][0]
    var hasOptimalRiskChanged = true
    while (hasOptimalRiskChanged) {
      hasOptimalRiskChanged = false
      (0 until riskGrid.height).forEach { y ->
        (0 until riskGrid.width).forEach { x ->
          val adjacentPoints = riskGrid.getAdjacentPoints(Point(x, y))
          val currentRiskValue = lowestPathRisks[y][x]
          val possibleRiskUpdates = adjacentPoints.map {
            riskGrid.values[y][x] + lowestPathRisks[it.y][it.x]
          }
          val bestRiskUpdate = possibleRiskUpdates.minOrNull()!!
          if (bestRiskUpdate < currentRiskValue) {
            hasOptimalRiskChanged = true
            lowestPathRisks[y][x] = bestRiskUpdate
          }
        }
      }
    }
    return Grid(lowestPathRisks)
  }

  fun parseInput(input: String): Grid {
    val lines = input.trim().split("\n").map { it.trim() }
    val riskLevels = lines.map { line ->
      line.toList().map { symbol ->
        symbol.digitToInt()
      }.toTypedArray()
    }.toTypedArray()
    return Grid(riskLevels)
  }

  fun riskOfPathFromLeftTopToRightBottom(pathRisks: Grid): Int =
    pathRisks.values[pathRisks.height - 1][pathRisks.width - 1] - pathRisks.values[0][0]

  fun part1(cavern: Grid): Int {
    val lowestPathRisks = computeLowestPathRisks(cavern)
    return riskOfPathFromLeftTopToRightBottom(lowestPathRisks)
  }

  fun part2(cavern: Grid): Int {
    val replicatedCavern = cavern.replicate(5)
    val lowestPathRisks = computeLowestPathRisks(replicatedCavern)
    return riskOfPathFromLeftTopToRightBottom(lowestPathRisks)
  }
}

fun main() {
  val cavern = Day15.parseInput(Day15.input)
  println(Day15.part1(cavern))
  println(Day15.part2(cavern))
}