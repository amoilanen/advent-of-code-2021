package io.github.antivanov.aoc2021.util

import arrow.core.None
import arrow.core.Some
import arrow.core.flattenOption
import java.util.*

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
      list.subList(left, right).filter { it != e }
    }
  }
}