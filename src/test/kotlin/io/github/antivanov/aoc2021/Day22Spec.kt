package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day22.sequentialRangesFrom
import io.github.antivanov.aoc2021.Day22.Segment
import io.github.antivanov.aoc2021.Day22.Cube

class Day22Spec {

  @Test
  fun test_sequentialRangesFrom_1() {
    val actual = sequentialRangesFrom(listOf(0..5, 2..8))
    val expected = listOf(0..1, 2..5, 6..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_2() {
    val actual = sequentialRangesFrom(listOf(0..8, 2..5, 1..10))
    val expected = listOf(0..0, 1..1, 2..5, 6..8, 9..10)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_3_same_start_end() {
    val actual = sequentialRangesFrom(listOf(0..2, 0..4, 2..4))
    val expected = listOf(0..1, 2..2, 3..4)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_4_non_intersecting_ranges() {
    val actual = sequentialRangesFrom(listOf(0..1, 2..4, 5..8))
    val expected = listOf(0..1, 2..4, 5..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_5_same_range_start() {
    val actual = sequentialRangesFrom(listOf(0..2, 0..4, 0..8))
    val expected = listOf(0..2, 3..4, 5..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_6_same_range_end() {
    val actual = sequentialRangesFrom(listOf(2..8, 4..8, 0..8))
    val expected = listOf(0..1, 2..3, 4..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_7_same_range_end() {
    val actual = sequentialRangesFrom(listOf(2..8, 4..8, 0..8))
    val expected = listOf(0..1, 2..3, 4..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_8_same_range_start_and_end_in_different_ranges() {
    val actual = sequentialRangesFrom(listOf(0..4, 4..6, 6..8, 0..8, 0..6))
    val expected = listOf(0..3, 4..4, 5..5, 6..6, 7..8)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_9_same_range_ends_different_directions() {
    val actual = sequentialRangesFrom(listOf(0..0, 1..3, 2..2, 2..3))
    val expected = listOf(0..0, 1..1, 2..2, 3..3)
    assertEquals(expected, actual)
  }

  @Test
  fun test_sequentialRangesFrom_10_gaps_between_ranges() {
    val actual = sequentialRangesFrom(listOf(0..2, 5..7, 9..10))
    val expected = listOf(0..2, 3..4, 5..7, 8..8, 9..10)
    assertEquals(expected, actual)
  }

  @Test
  fun test_intersect_segments_non_empty_intersection() {
    val s1 = Segment(1..8)
    val s2 = Segment(0..5)
    assertEquals(Segment(1..5), s1.intersectWith(s2))
  }

  @Test
  fun test_intersect_segments_empty_intersection() {
    val s1 = Segment(1..4)
    val s2 = Segment(6..8)
    assertEquals(Segment(IntRange.EMPTY), s1.intersectWith(s2))
  }

  @Test
  fun test_intersect_cubes_non_empty_intersection() {
    val cube1 = Cube(2..4, 6..8, 10..12)
    val cube2 = Cube(3..5, 7..9, 11..13)
    val expected = Cube(3..4, 7..8, 11..12)
    assertEquals(expected, cube1.intersectWith(cube2))
  }

  @Test
  fun test_intersect_cubes_empty_intersection() {
    val cube1 = Cube(2..4, 6..8, 10..12)
    val cube2 = Cube(5..7, 9..11, 13..15)
    assertEquals(Cube(IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY), cube1.intersectWith(cube2))
  }
}