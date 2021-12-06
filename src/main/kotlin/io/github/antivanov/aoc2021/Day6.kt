package io.github.antivanov.aoc2021

object Day6 {
  val input = "3,4,3,1,2"

  //TODO: Optimize with memoization
  //TODO: Parallelize computations

  private const val newFishSpawnTime = 7
  private const val newBornSpawnDelay = 2
  fun totalFishNumber(startTimer: Int, daysLeftToSpan: Int): Int {
    val fullDaysToSpawn = daysLeftToSpan - startTimer
    val spawnedChildFish = (0..fullDaysToSpawn / newFishSpawnTime).map { fishSpawnIndex ->
      val daysElapsed = startTimer + fishSpawnIndex * newFishSpawnTime + 1
      if (daysElapsed <= fullDaysToSpawn + startTimer) {
        totalFishNumber(newFishSpawnTime + newBornSpawnDelay - 1, daysLeftToSpan - daysElapsed)
      } else
        0
    }.sum()
    return 1 + spawnedChildFish
  }

  fun part1(fishTimers: List<Int>, totalDays: Int): Int =
    fishTimers.fold(0) { totalFishes, fishTimer ->
      totalFishes + totalFishNumber(fishTimer, totalDays)
    }
}

fun main() {
  val totalDays = 18
  val fishTimers = Day6.input.split(",").map { it.toInt() }
  println(fishTimers)
  println(Day6.part1(fishTimers, totalDays))
}