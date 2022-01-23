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

  fun repeatingRemainders(): List<Int> =
    (1 until 30 step 3).map {
      3 * (it + 1) % 10
    }

  fun movesToReachScore(initialPosition: Int, repeatingReminders: List<Int>, score: Int): Int {
    //TODO: Implement to solve part 1
    return 0
  }

  fun scoreAfterNumberOfMoves(repeatingReminders: List<Int>): Int {
    //TODO: Implement to solve part 1
    return 0
  }
}

fun main() {
  val (firstPosition, secondPosition) = Day21.parseInput(Day21.input)
  println(firstPosition)
  println(secondPosition)
  val remainders = Day21.repeatingRemainders()
  println(remainders)
  val remaindersWithIndices = remainders.indices.zip(remainders)
  val (firstRemainders, secondRemainders) = remaindersWithIndices.partition {
    it.first % 2 == 0
  }.toList().map { playerRemainders ->
    playerRemainders.map {
      it.second
    }
  }
  println(firstRemainders)
  println(secondRemainders)
}