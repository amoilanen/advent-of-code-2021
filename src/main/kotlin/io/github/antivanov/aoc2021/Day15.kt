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

  data class Cavern(val riskLevels: Array<Array<Int>>) {
    private val width = riskLevels[0].size
    private val height = riskLevels.size

    override fun toString(): String =
      (0 until height).map { y ->
        (0 until width).map { x ->
          riskLevels[y][x]
        }.joinToString("")
      }.joinToString("\n")
  }

  fun parseInput(input: String): Cavern {
    val lines = input.trim().split("\n").map { it.trim() }
    val riskLevels = lines.map { line ->
      line.toList().map { symbol ->
        symbol.digitToInt()
      }.toTypedArray()
    }.toTypedArray()
    return Cavern(riskLevels)
  }
}

fun main() {
  val cavern = Day15.parseInput(Day15.input)
  println(cavern)
}