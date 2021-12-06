package io.github.antivanov.aoc2021

import arrow.core.foldLeft
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
  private suspend fun totalFishNumber(startTimer: Int, daysLeftToSpan: Int): Int {
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

  private fun fishCountAfterNumberOfDaysElapsed(fishTimers: List<Int>, totalDays: Int): Int =
    fishTimers.fold(0) { totalFishes, fishTimer ->
      totalFishes + runBlocking { totalFishNumber(fishTimer, totalDays) }
    }

  fun part1(fishTimers: List<Int>): Int =
    fishCountAfterNumberOfDaysElapsed(fishTimers, 80)

  private const val maxDaysTillSpawning = newFishSpawnTime + newBornSpawnDelay

  private fun initFishGenerations(fishTimers: List<Int>): Array<Long> {
    val fishTimersCount = fishTimers.groupBy { it }.mapValues { it.value.size }
    val fishesByDaysTillSpawning = Array<Long>(maxDaysTillSpawning) { 0 }
    return fishTimersCount.foldLeft(fishesByDaysTillSpawning) { curr, (daysTillSpawning, fishesCount) ->
      curr[daysTillSpawning] = fishesCount.toLong()
      curr
    }
  }

  private fun simulateDay(fishGenerations: Array<Long>): Array<Long> {
    val updatedFishGenerations = Array<Long>(maxDaysTillSpawning) { 0 }
    val justSpawnedFish = fishGenerations[0]
    val fishWhichJustCreatedNewFish = fishGenerations[0]
    (1 until maxDaysTillSpawning).forEach { index ->
      updatedFishGenerations[index - 1] = fishGenerations[index]
    }
    updatedFishGenerations[newFishSpawnTime - 1] += fishWhichJustCreatedNewFish
    updatedFishGenerations[maxDaysTillSpawning - 1] += justSpawnedFish
    return updatedFishGenerations
  }

  /*
   * More efficient solution: instead of simulating individual fishes, simulate them as groups sharing common
   * spawning attribute: days before spawning
   */
  private fun fishCountAfterDaysElapsed(fishTimers: List<Int>, totalDays: Int): Long {
    val initialFishGenerations = initFishGenerations(fishTimers)
    val finalFishGenerations = (1..totalDays).fold(initialFishGenerations) { fishGenerations, _ ->
      simulateDay(fishGenerations)
    }
    return finalFishGenerations.sum()
  }

  fun part2(fishTimers: List<Int>): Long {
    return fishCountAfterDaysElapsed(fishTimers, 256)
  }
}

fun main() {
  val fishTimers = Day6.input.split(",").map { it.toInt() }
  println(fishTimers)
  println(Day6.part1(fishTimers))
  println(Day6.part2(fishTimers))
}