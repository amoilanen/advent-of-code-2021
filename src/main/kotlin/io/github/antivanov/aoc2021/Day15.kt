package io.github.antivanov.aoc2021

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

    override fun toString(): String =
      (0 until height).map { y ->
        (0 until width).map { x ->
          values[y][x]
        }.joinToString(" ")
      }.joinToString("\n")
  }

  fun computeLowestPathRisks(riskGrid: Grid): Grid {
    val lowestPathRisks = (0 until riskGrid.height).map { _ ->
      IntArray(riskGrid.width).toTypedArray()
    }.toTypedArray()
    lowestPathRisks[0][0] = riskGrid.values[0][0]
    // Compute topmost cells
    (1 until riskGrid.width).map { x ->
      lowestPathRisks[0][x] = riskGrid.values[0][x] + lowestPathRisks[0][x - 1]
    }
    // Compute leftmost cells
    (1 until riskGrid.height).map { y ->
      lowestPathRisks[y][0] = riskGrid.values[y][0] + lowestPathRisks[y - 1][0]
    }
    // Compute the rest of the cells
    (1 until riskGrid.width).forEach { x ->
      (1 until riskGrid.height).forEach { y ->
        val pathFromAboveRisk = riskGrid.values[y][x] + lowestPathRisks[y - 1][x]
        val pathFromLeftRisk = riskGrid.values[y][x] + lowestPathRisks[y][x - 1]
        val minPathRisk = minOf(pathFromAboveRisk, pathFromLeftRisk)
        lowestPathRisks[y][x] = minPathRisk
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