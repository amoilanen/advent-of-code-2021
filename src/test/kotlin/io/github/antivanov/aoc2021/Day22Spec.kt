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

  @Test
  fun test_intersectRanges_3_same_start_end() {
    val actual = intersectRanges(listOf(0..2, 0..4, 2..4))
    val expected = listOf(0..1, 2..2, 3..4)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_4_non_intersecting_ranges() {
    val actual = intersectRanges(listOf(0..1, 2..4, 5..8))
    val expected = listOf(0..1, 2..4, 5..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_5_same_range_start() {
    val actual = intersectRanges(listOf(0..2, 0..4, 0..8))
    val expected = listOf(0..2, 3..4, 5..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_6_same_range_end() {
    val actual = intersectRanges(listOf(2..8, 4..8, 0..8))
    val expected = listOf(0..1, 2..3, 4..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_7_same_range_end() {
    val actual = intersectRanges(listOf(2..8, 4..8, 0..8))
    val expected = listOf(0..1, 2..3, 4..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersectRanges_8_same_range_start_and_end_in_different_ranges() {
    val actual = intersectRanges(listOf(0..4, 4..6, 6..8, 0..8, 0..6))
    val expected = listOf(0..3, 4..4, 5..5, 6..6, 7..8)
    assertEquals(expected, actual)
  }

  //TODO: Distinguish same start and end with different directions, i.e. [0, 3], [1, 2], [2, 3] vs [1, 1], [1, 1], [1, 1]
  //TODO: There are gaps between the ranges not filled with other ranges
}