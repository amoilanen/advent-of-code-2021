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

  // For a deterministic repeating die: 1, 2, 3, 4, 5, ..., 99, 100, 1, 2, ...
  fun repeatingRemainders(): List<Int> =
    (1 until (BoardSize * DieCastsPerMove) step DieCastsPerMove).map {
      DieCastsPerMove * (it + 1) % BoardSize
    }

  fun repeatingRemindersPerPlayer(): Pair<List<Int>, List<Int>> {
    val remainders = repeatingRemainders()
    val remaindersWithIndices = remainders.indices.zip(remainders)
    val (firstRemainders, secondRemainders) = remaindersWithIndices.partition {
      it.first % 2 == 0
    }.toList().map { playerRemainders ->
      playerRemainders.map {
        it.second
      }
    }
    return firstRemainders to secondRemainders
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
    val repeatingPositionsAndRemainders = positionsAndReminders.subList(0, firstRepetitionIndex)
    val repeatingScores = repeatingPositionsAndRemainders.map {
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

  fun movesNumberToReachScore(playerInitialPosition: Int, repeatingRemainders: List<Int>, score: Int, movesBeforeThisPlayer: Int): Int {
    val scoresCycle = repeatingScores(playerInitialPosition, repeatingRemainders)
    val cycleTotalScore = scoresCycle.sum()

    val fullCyclesToReachScore = score / cycleTotalScore
    val stillRemainingScore = score % cycleTotalScore

    val totalScoresInsideCycle = scoreSums(scoresCycle)
    val remainingMoves = totalScoresInsideCycle.indexOfFirst {
      it >= stillRemainingScore
    } + 1
    val playerMoves = fullCyclesToReachScore * scoresCycle.size + remainingMoves
    val otherPlayerMoves = playerMoves - 1 + movesBeforeThisPlayer
    return playerMoves + otherPlayerMoves
  }

  fun dieRollCountForMoveCount(movesCount: Int): Int =
    movesCount * DieCastsPerMove

  fun playerScoreAfterNumberOfMoves(playerInitialPosition: Int, repeatingRemainders: List<Int>, movesNumber: Int): Int {
    //FIXME: Fix the computation of the score after a given number of moves for a given player
    println("playerScoreAfterNumberOfMoves:begin")
    val scoresCycle = repeatingScores(playerInitialPosition, repeatingRemainders)
    println(scoresCycle)
    val cycleTotalScore = scoresCycle.sum()
    println(cycleTotalScore)

    val fullCycles = movesNumber / scoresCycle.size
    println(fullCycles)
    val fullCycleScore = fullCycles * cycleTotalScore

    val remainingMoves = movesNumber % scoresCycle.size
    val remainingScore = scoresCycle.subList(0, remainingMoves).sum()

    println("playerScoreAfterNumberOfMoves:end")
    return fullCycleScore + remainingScore
  }

  fun part1(input: String): Int {
    val (firstPosition, secondPosition) = parseInput(input)
    val (firstRemainders, secondRemainders) = repeatingRemindersPerPlayer()

    val firstMovesNumber = movesNumberToReachScore(firstPosition, firstRemainders, 1000, movesBeforeThisPlayer = 0)
    val secondMovesNumber = movesNumberToReachScore(secondPosition, secondRemainders, 1000, movesBeforeThisPlayer = 1)

    val playerRemainders = mapOf(
      0 to firstRemainders,
      1 to secondRemainders
    )
    val playerMoves = mapOf(
      0 to firstMovesNumber,
      1 to secondMovesNumber
    )
    val playerPositions = mapOf(
      0 to firstPosition,
      1 to secondPosition
    )
    val movesBeforePlayer = mapOf(
      0 to 0,
      1 to 1
    )
    val winningPlayer = if (firstMovesNumber > secondMovesNumber) 1 else 0
    val loosingPlayer = (winningPlayer + 1) % 2
    val winningMovesNumber = playerMoves.values.minOrNull()!!

    val loosingPlayerMovesNumber = winningMovesNumber - movesBeforePlayer[loosingPlayer]!!
    val loosingPlayerScore = playerScoreAfterNumberOfMoves(playerPositions[loosingPlayer]!!, playerRemainders[loosingPlayer]!!, loosingPlayerMovesNumber)

    println(loosingPlayer)
    println(loosingPlayerMovesNumber)
    println(winningMovesNumber)
    println(loosingPlayerScore)

    val dieRollCount = dieRollCountForMoveCount(secondMovesNumber)
    return dieRollCount * loosingPlayerScore
  }
}

fun main() {
  println(Day21.part1(Day21.input))
}