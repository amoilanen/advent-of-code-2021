package io.github.antivanov.aoc2021

object Day6 {
  val input = "3,4,3,1,2"

  //TODO: Optimize with memoization
  //TODO: Parallelize computations

  private const val newFishSpawnTime = 7
  private const val newBornSpawnDelay = 2
  fun totalFishNumber(startTimer: Int, totalDays: Int, initialDayOffset: Int): Int {
    val fullDaysToSpawn = totalDays - initialDayOffset - startTimer
    val spawnedChildFish = (0..fullDaysToSpawn / newFishSpawnTime).map { fishSpawnIndex ->
      val daysElapsed = startTimer + fishSpawnIndex * newFishSpawnTime + 1
      if (daysElapsed <= fullDaysToSpawn + startTimer) {
        println("debug: spawn on day ${daysElapsed + initialDayOffset}")
        totalFishNumber(newFishSpawnTime + newBornSpawnDelay, totalDays, daysElapsed - 1)
        //1
      } else
        0
    }.sum()
    return 1 + spawnedChildFish
  }

  fun part1(fishTimers: List<Int>, totalDays: Int): Int =
    fishTimers.fold(0) { totalFishes, fishTimer ->
      totalFishes + totalFishNumber(fishTimer, totalDays, 0)
    }
}

fun main() {
  //4, 10 - no new fish yet self spawned
  val totalDays = 18
  // 18 - new fish self spawned
  val fishTimers = Day6.input.split(",").map { it.toInt() }
  println(fishTimers)
  /*
  Day6.totalFishNumber(3, totalDays, 0)
  println("Newborn fish 1") // + 1
  Day6.totalFishNumber(9, totalDays, 1)
  println("Newborn fish 2")
  Day6.totalFishNumber(9, totalDays, 4)
  println("Newborn fish 3")
  Day6.totalFishNumber(9, totalDays, 8)
  println("Newborn fish 4")
  Day6.totalFishNumber(9, totalDays, 9)
  */

  /*
println("fish 1")
Day6.totalFishNumber(3, totalDays, 0)
println("fish 2") // + 1
Day6.totalFishNumber(4, totalDays, 0)
println("fish 3")
Day6.totalFishNumber(3, totalDays, 0)
println("fish 4")
Day6.totalFishNumber(1, totalDays, 0)
println("fish 5")
Day6.totalFishNumber(2, totalDays, 0)
  */

  println(Day6.part1(fishTimers, totalDays))
}