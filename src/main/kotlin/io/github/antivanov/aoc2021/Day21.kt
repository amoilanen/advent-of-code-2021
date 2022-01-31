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

  private const val BoardSize = 10
  private const val DieCastsPerMove = 3

  // For a deterministic repeating die: 1, 2, 3, 4, 5, ..., 99, 100, 101, 102, ...
  fun repeatingRemainders(): List<Int> =
    (1 until (BoardSize * DieCastsPerMove) step DieCastsPerMove).map {
      DieCastsPerMove * (it + 1) % BoardSize
    }

  private fun repeatingScores(initialPosition: Int, repeatingReminders: List<Int>): List<Int> {
    // At most BoardSize different positions possible in the beginning of the repeating reminders series
    val positionsAndReminders = (0..BoardSize).fold(emptyList<Pair<Int, Int>>()) { positionsSoFar, _ ->
      repeatingReminders.fold(positionsSoFar) { positions, remainder ->
        val lastPosition = if (positions.isEmpty())
          initialPosition
        else
          positions.first().first
        val newPosition = (lastPosition + remainder) % BoardSize // position 0 is same as BoardSize - it is "scored" differently
        listOf(newPosition to remainder) + positions
      }
    }.reversed()

    val firstPositionAndRemainder = positionsAndReminders.first()
    val firstRepetitionIndex = positionsAndReminders.subList(1, positionsAndReminders.size).indexOfFirst {
      it == firstPositionAndRemainder
    } + 1
    val allPossibleScores = positionsAndReminders.subList(0, firstRepetitionIndex)
    val repeatingScores = allPossibleScores.map {
      val position = it.first
      if (position == 0)
        BoardSize
      else
        position
    }
    return repeatingScores
  }

  private fun scoreSums(scores: List<Int>): List<Int> =
    scores.fold(listOf(0)) { sums, current ->
      listOf(sums.first() + current) + sums
    }.reversed().drop(1)

  fun movesToReachScore(playerInitialPosition: Int, repeatingReminders: List<Int>, score: Int, movesBeforeThisPlayer: Int): Int {
    val scoresCycle = repeatingScores(playerInitialPosition, repeatingReminders)
    val totalScoresInsideCycle = scoreSums(scoresCycle)
    val cycleTotalScore = scoresCycle.sum()

    val fullCyclesToReachScore = score / cycleTotalScore
    val stillRemainingScore = score % cycleTotalScore

    val remainingScoresInsidePeriodUntilScore = totalScoresInsideCycle.indexOfFirst {
      it >= stillRemainingScore
    } + 1
    val playerMoves = fullCyclesToReachScore * scoresCycle.size + remainingScoresInsidePeriodUntilScore
    val otherPlayerMoves = playerMoves - 1 + movesBeforeThisPlayer
    return playerMoves + otherPlayerMoves
  }

  fun dieRollCountForMoveCount(movesCount: Int): Int =
    movesCount * DieCastsPerMove

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

  val firstMoves = Day21.movesToReachScore(firstPosition, firstRemainders, 1000, movesBeforeThisPlayer = 0)
  println(firstMoves)
  println(Day21.dieRollCountForMoveCount(firstMoves))

  val secondMoves = Day21.movesToReachScore(secondPosition, secondRemainders, 1000, movesBeforeThisPlayer = 1)
  println(secondMoves)
  println(Day21.dieRollCountForMoveCount(secondMoves))
}