package io.github.antivanov.aoc2021.util

import kotlin.test.Test
import kotlin.test.assertEquals

class TuplesSpec {

  @Test
  fun test_select_tuples_from_set_1() {
    assertEquals(listOf(
      listOf(1, 2),
      listOf(2, 1),
      listOf(2, 3),
      listOf(3, 2),
      listOf(3, 1),
      listOf(1, 3)
    ).toSet(), Tuples.tuplesOfSize(2,
      listOf(1, 2, 3)
    ).toSet())
  }

  @Test
  fun test_select_tuples_from_set_2() {
    val expectedSize = (25 * 24 * 23) / (3 * 2 * 1)
    assertEquals(expectedSize, Tuples.setsOfSize(3,
      (1..25).toSet()
    ).size)
  }
}