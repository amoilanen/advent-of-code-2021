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

  data class Grid(val locations: List<List<Int>>) {

    override fun toString(): String =
      locations.map {
        it.joinToString("")
      }.joinToString("\n")
  }

  fun parseInput(input: String): List<List<Int>> =
    input.split("\n").map {
      it.trim()
    }.map {
      it.toList().map { digit ->
        digit.toString().toInt()
      }
    }

}

fun main() {
  val parsedInput = Day11.parseInput(Day11.input)
  val initialGrid = Day11.Grid(parsedInput)
  println(initialGrid.toString())
}