package io.github.antivanov.aoc2021

object Day11 {

  val input = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
  """.trimIndent()

  data class Point(val x: Int, val y: Int)

  data class Grid(val locations: Array<Array<Int>>) {
    private val width = locations[0].size
    private val height = locations.size

    private fun liesInGrid(p: Point): Boolean =
      p.x in (0 until width) &&
          p.y in  (0 until height)

    private fun getAdjacentPoints(p: Point): List<Point> {
      val subGrid = (-1..1).flatMap { xOffset ->
        (-1..1).map { yOffset ->
          p.copy(x = p.x + xOffset, y = p.y + yOffset)
        }
      }
      return subGrid.filter {
        it != p && liesInGrid(it)
      }
    }

    fun updateGrid(): Grid {
      val updatedLocations = locations.copyOf().map { it.copyOf() }.toTypedArray()
      incrementLevels(updatedLocations)
      checkIfToFlashAndPropagateIfFlashed(updatedLocations)
      return Grid(updatedLocations)
    }

    fun flashesCount(): Int =
      (0 until width).flatMap { x ->
        (0 until height).filter { y ->
          locations[y][x] == 0
        }
      }.count()

    fun areAllFlashed(): Boolean =
      flashesCount() == width * height

    private fun incrementLevels(locations: Array<Array<Int>>) {
      (0 until width).flatMap { x ->
        (0 until height).map { y ->
          locations[y][x] += 1
        }
      }
    }

    private fun checkIfToFlashAndPropagateIfFlashed(locations: Array<Array<Int>>) {
      (0 until width).flatMap { x ->
        (0 until height).map { y ->
          checkIfToFlashAndPropagateIfFlashed(Point(x, y), locations)
        }
      }
    }

    private fun checkIfToFlashAndPropagateIfFlashed(p: Point, locations: Array<Array<Int>>) {
      if (locations[p.y][p.x] > 9) {
        locations[p.y][p.x] = 0
        propagateLevelChangesFrom(p, locations)
      }
    }

    private fun propagateLevelChangesFrom(p: Point, locations: Array<Array<Int>>) {
      getAdjacentPoints(p).forEach {
        if (locations[it.y][it.x] != 0) {
          locations[it.y][it.x] += 1
          checkIfToFlashAndPropagateIfFlashed(it, locations)
        }
      }
    }

    override fun toString(): String =
      locations.joinToString("\n") {
        it.joinToString("")
      }
  }

  fun parseInput(input: String): Array<Array<Int>> =
    input.split("\n").map {
      it.trim()
    }.map {
      it.toList().map { digit ->
        digit.toString().toInt()
      }.toTypedArray()
    }.toTypedArray()

  private fun gridFrom(parsedInput: Array<Array<Int>>): Grid {
    val clonedParsedInput = parsedInput.copyOf().map { it.copyOf() }.toTypedArray()
    return Grid(clonedParsedInput)
  }

  fun part1(parsedInput: Array<Array<Int>>): Int {
    val initialGrid = gridFrom(parsedInput)
    val totalSteps = 100
    val (_, finalFlashes) = (1..totalSteps).fold(initialGrid to 0) { (grid, flashes), _ ->
      val updatedGrid = grid.updateGrid()
      val updatedFlashes = flashes + updatedGrid.flashesCount()
      updatedGrid to updatedFlashes
    }
    return finalFlashes
  }

  fun part2(parsedInput: Array<Array<Int>>): Int {
    fun firstStepWhenAllFlashed(grid: Grid, step: Int): Int {
      val updatedGrid = grid.updateGrid()
      return if (updatedGrid.areAllFlashed())
        step + 1
      else
        firstStepWhenAllFlashed(updatedGrid, step + 1)
    }
    return firstStepWhenAllFlashed(gridFrom(parsedInput), 0)
  }
}

fun main() {
  val parsedInput = Day11.parseInput(Day11.input)
  println(Day11.part1(parsedInput))
  println(Day11.part2(parsedInput))
}