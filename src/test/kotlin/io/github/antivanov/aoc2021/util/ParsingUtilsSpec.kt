package io.github.antivanov.aoc2021.util

import kotlin.test.Test
import kotlin.test.assertEquals

class ParsingUtilsSpec {

  @Test
  fun test_split_list_by_repeating_element() {
    assertEquals(listOf(
      listOf(),
      listOf(2),
      listOf(2, 3),
      listOf(2, 3, 4)
    ), ParsingUtils.splitByElement(
      listOf(1, 2, 1, 2, 3, 1, 2, 3, 4),
      1
    ))
  }

  @Test
  fun test_split_list_which_does_not_start_with_element() {
    assertEquals(listOf(
      listOf(0),
      listOf(2, 3)
    ), ParsingUtils.splitByElement(
      listOf(0, 1, 2, 3),
      1
    ))
  }

  @Test
  fun test_split_list_which_contains_adjacent_separators() {
    assertEquals(listOf(
      listOf(),
      listOf(2),
      listOf(),
      listOf(2),
      listOf()
    ), ParsingUtils.splitByElement(
      listOf(1, 2, 1, 1, 2, 1),
      1
    ))
  }
}