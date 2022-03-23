package io.github.antivanov.aoc2021.util

object ListUtils {

  fun <T> pairsOf(values: List<T>): List<Pair<T, T>> {
    val firstElements = values.filterIndexed { idx, _ -> idx % 2 == 0 }
    val secondElements = values.filterIndexed { idx, _ -> idx % 2 == 1 }
    return firstElements.zip(secondElements)
  }
}