package io.github.antivanov.aoc2021

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

object Day6 {
  val input = "3,4,3,1,2"

  // Many values are going to be recomputed multiple times, memoizing the values here
  private val memoizedTotalFishNumber = ConcurrentHashMap<String, Int>()
  private fun memoizedTotalFishNumberKey(startTimes: Int, daysLeftToSpan: Int): String =
    startTimes.toString() + "_" + daysLeftToSpan.toString()

  private const val newFishSpawnTime = 7
  private const val newBornSpawnDelay = 2

  // Coroutine to make the recursion stack-safe
  suspend fun totalFishNumber(startTimer: Int, daysLeftToSpan: Int): Int {
    val memoizedKey = memoizedTotalFishNumberKey(startTimer, daysLeftToSpan)
    if (memoizedTotalFishNumber.contains(memoizedKey)) {
      return memoizedTotalFishNumber[memoizedKey]!!
    } else {
      val fullDaysToSpawn = daysLeftToSpan - startTimer
      val spawnedChildFish = (0..fullDaysToSpawn / newFishSpawnTime).map { fishSpawnIndex ->
        runBlocking {
          val daysElapsed = startTimer + fishSpawnIndex * newFishSpawnTime + 1
          if (daysElapsed <= fullDaysToSpawn + startTimer) {
            async {
              totalFishNumber(newFishSpawnTime + newBornSpawnDelay - 1, daysLeftToSpan - daysElapsed)
            }
          } else async { 0 }
        }
      }.awaitAll().sum()
      val result = 1 + spawnedChildFish
      memoizedTotalFishNumber[memoizedKey] = result
      return result
    }
  }

  fun part1(fishTimers: List<Int>, totalDays: Int): Int =
    fishTimers.fold(0) { totalFishes, fishTimer ->
      totalFishes + runBlocking { totalFishNumber(fishTimer, totalDays) }
    }
}

fun main() {
  val totalDays = 80
  val fishTimers = Day6.input.split(",").map { it.toInt() }
  println(fishTimers)
  println(Day6.part1(fishTimers, totalDays))
}