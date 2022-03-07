package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day22.intersectRanges

class Day22Spec {

  @Test
  fun test_intersectRanges_1() {
    val actual = intersectRanges(listOf(0..5, 2..8))
    val expected = listOf(0..1, 2..5, 6..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_2() {
    val actual = intersectRanges(listOf(0..8, 2..5, 1..10))
    val expected = listOf(0..0, 1..1, 2..5, 6..8, 9..10)
    assertEquals(expected, actual)
  }

  //TODO: Same range start and end
  //TODO: Same range start
  //TODO: Same range end
  //TODO: Same start and end occur several times in different ranges
  //TODO: Distinguish same start and end with different directions, i.e. [0, 3], [1, 2], [2, 3] vs [1, 1], [1, 1], [1, 1]
}