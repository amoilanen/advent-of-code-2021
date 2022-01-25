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

  //TODO: Extract 10 as a constant so that different sizes of the game board might be used

  fun repeatingRemainders(): List<Int> =
    (1 until 30 step 3).map {
      3 * (it + 1) % 10
    }

  fun movesToReachScore(initialPosition: Int, repeatingReminders: List<Int>, score: Int): Int {
    // At most 10 different remainders possible
    val positionsAndReminders = (1..10).fold(emptyList<Pair<Int, Int>>()) { positionsSoFar, _ ->
      repeatingReminders.fold(positionsSoFar) { positions, remainder ->
        val lastPosition = if (positions.isEmpty())
          initialPosition
        else
          positions.first().first
        val newPosition = (lastPosition + remainder) % 10 // position 0 is same as 10 - it is "scored" differently
        listOf(newPosition to remainder) + positions
      }
    }.reversed()

    println(positionsAndReminders)
    println(positionsAndReminders.size)

    val firstPositionAndRemainder = positionsAndReminders.first()
    val firstRepetitionIndex = positionsAndReminders.subList(1, positionsAndReminders.size).indexOfFirst {
      it == firstPositionAndRemainder
    } + 1
    println(firstRepetitionIndex)
    val allPossiblePositions = positionsAndReminders.subList(0, firstRepetitionIndex)
    println(allPossiblePositions)

    // TODO: Compute the different scores for positions
    // TODO: Re-factor the utility function from the current function to compute repeating scores

    //TODO: Implement to solve part 1
    return 0
  }

  fun scoreAfterNumberOfMoves(repeatingReminders: List<Int>, movesNumber: Int): Int {
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

  val firstMoves = Day21.movesToReachScore(firstPosition, firstRemainders, 1000)
  println(firstMoves)

  val secondMoves = Day21.movesToReachScore(secondPosition, secondRemainders, 1000)
  println(secondMoves)
}