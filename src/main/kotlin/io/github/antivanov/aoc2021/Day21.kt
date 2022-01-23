package io.github.antivanov.aoc2021

object Day21 {

  val input = """
    Player 1 starting position: 4
    Player 2 starting position: 8
  """.trimIndent()

  fun parseInput(input: String): Pair<Int, Int> {
    val positions = input.split("\n").map {
      val (_, position) = it.split(":")
      position.trim().toInt()
    }
    return Pair(positions[0], positions[1])
  }
}

fun main() {
  val positions = Day21.parseInput(Day21.input)
  println(positions)
}