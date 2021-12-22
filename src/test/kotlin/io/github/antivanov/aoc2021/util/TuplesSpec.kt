package io.github.antivanov.aoc2021.util

import kotlin.test.Test
import kotlin.test.assertEquals

class TuplesSpec {

  @Test
  fun test_select_tuples_from_list() {
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
}