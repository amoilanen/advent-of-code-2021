package io.github.antivanov.aoc2021.util

import arrow.core.None
import arrow.core.Some
import arrow.core.flattenOption

object ParsingUtils {

  fun <T> splitByElement(list: List<T>, e: T): List<List<T>> {
    val elementPositions = list.mapIndexed { index, x ->
      if (x == e)
        Some(index)
      else
        None
    }.flattenOption()
    val splitIndices = listOf(0) + elementPositions + listOf(list.size)
    return splitIndices.zipWithNext().map { (left, right) ->
      val from = if (list[left] == e && right > left)
        left + 1
      else
        left
      val to = right
      list.subList(from, to)
    }
  }
}