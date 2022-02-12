package io.github.antivanov.aoc2021

import kotlin.math.ceil

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

  fun playerScoreAfterNumberOfMoves(playerInitialPosition: Int, repeatingRemainders: List<Int>, movesNumber: Int, movesBeforeThisPlayer: Int): Int {
    val playerMovesNumber = ceil((movesNumber - movesBeforeThisPlayer).toDouble() / 2).toInt()
    val scoresCycle = repeatingScores(playerInitialPosition, repeatingRemainders)
    val cycleTotalScore = scoresCycle.sum()

    val fullCycles = playerMovesNumber / scoresCycle.size
    val fullCycleScore = fullCycles * cycleTotalScore

    val remainingMoves = playerMovesNumber % scoresCycle.size
    val remainingScore = scoresCycle.subList(0, remainingMoves).sum()

    return fullCycleScore + remainingScore
  }

  fun part1(firstPosition: Int, secondPosition: Int): Int {
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

    val loosingPlayerScore = playerScoreAfterNumberOfMoves(playerPositions[loosingPlayer]!!, playerRemainders[loosingPlayer]!!, winningMovesNumber, movesBeforePlayer[loosingPlayer]!!)
    val dieRollCount = dieRollCountForMoveCount(winningMovesNumber)

    return dieRollCount * loosingPlayerScore
  }

  fun forwardMoveFrequencies(): Map<Int, Int> {
    val diePossibleRolls = (1..3).toList()

    val forwardMoves = diePossibleRolls.flatMap { first ->
      diePossibleRolls.flatMap { second ->
        diePossibleRolls.map { third ->
          first + second + third
        }
      }
    }
    return forwardMoves.groupBy { it }.mapValues { it.value.size }
  }

  val forwardMoves = forwardMoveFrequencies()

  data class PlayerState(val position: Int, val scoreLeft: Int)

  fun winCounts(currentToMove: PlayerState, nextToMove: PlayerState): Pair<Long, Long> =
    if (nextToMove.scoreLeft <= 0)
      0L to 1L
    else {
      val winCountsPerPossibleMove = forwardMoves.map { moveAndFrequency ->
        val move = moveAndFrequency.key
        val frequency = moveAndFrequency.value
        val newPosition = (currentToMove.position + move) % BoardSize
        val scoreIncrement = if (newPosition == 0)
          BoardSize
        else
          newPosition
        val updatedScoreLeft = currentToMove.scoreLeft - scoreIncrement
        val winsGivenMove = winCounts(nextToMove, currentToMove.copy(position = newPosition, scoreLeft = updatedScoreLeft))
        (winsGivenMove.second * frequency to winsGivenMove.first * frequency)
      }
      winCountsPerPossibleMove.fold(0L to 0L) { acc, count ->
        (acc.first + count.first) to (acc.second + count.second)
      }
    }

  fun part2(firstPosition: Int, secondPosition: Int): Long {
    val scoreToReach = 21
    val firstPlayer = PlayerState(firstPosition, scoreToReach)
    val secondPlayer = PlayerState(secondPosition, scoreToReach)
    val counts = winCounts(firstPlayer, secondPlayer)
    return counts.toList().maxOrNull()!!
  }
}

fun main() {
  val (firstPosition, secondPosition) = Day21.parseInput(Day21.input)
  println(Day21.part1(firstPosition, secondPosition))
  println(Day21.part2(firstPosition, secondPosition))
}