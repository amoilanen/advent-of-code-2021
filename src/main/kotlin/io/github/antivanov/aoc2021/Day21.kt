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

  private val BoardSize = 10

  fun repeatingRemainders(): List<Int> =
    (1 until (BoardSize * 3) step 3).map {
      3 * (it + 1) % BoardSize
    }

  fun repeatingScores(initialPosition: Int, repeatingReminders: List<Int>): List<Int> {
    // At most BoardSize different remainders possible
    val positionsAndReminders = (1..BoardSize).fold(emptyList<Pair<Int, Int>>()) { positionsSoFar, _ ->
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

  fun scoreSums(scores: List<Int>): List<Int> =
    scores.fold(listOf<Int>(0)) { acc, current ->
      listOf(acc.first() + current) + acc
    }.reversed().drop(1)

  fun movesToReachScore(initialPosition: Int, repeatingReminders: List<Int>, score: Int): Int {
    val scoresPeriod = repeatingScores(initialPosition, repeatingReminders)

    val totalScoresInsidePeriod = scoreSums(scoresPeriod)
    val periodTotalScore = scoresPeriod.sum()

    val fullPeriodsToReachScore = score / periodTotalScore
    val remainingScoreAfterLastFullPeriod = score % periodTotalScore

    val remainingScoresInsidePeriodUntilScore = totalScoresInsidePeriod.indexOfFirst {
      it >= remainingScoreAfterLastFullPeriod
    } + 1
    //FIXME: Return the full number of moves also counting the moves of the other player
    return fullPeriodsToReachScore * scoresPeriod.size + remainingScoresInsidePeriodUntilScore
  }

  @Deprecated("Old version which might be useful for debugging still")
  fun movesToReachScoreIncomplete(initialPosition: Int, repeatingReminders: List<Int>, score: Int): Int {
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

    println(repeatingScores(initialPosition, repeatingReminders))
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