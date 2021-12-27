package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.Day19.PossibleTransformations
import io.github.antivanov.aoc2021.Day19.Point

import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Spec {

  @Test
  fun test_transformations_are_exhaustive() {
    val point = Point(1, 2, 3)
    val transformed = PossibleTransformations.map { t ->
      t.apply(point)
    }.toSet()
    val expected = hashSetOf(
      Point(1, 2, 3),
      Point(-1, 2, 3),
      Point(1, -2, 3),
      Point(1, 2, -3),
      Point(-1, -2, 3),
      Point(1, -2, -3),
      Point(-1, 2, -3),
      Point(-1, -2, -3),

      Point(3, 1, 2),
      Point(-3, 1, 2),
      Point(3, -1, 2),
      Point(3, 1, -2),
      Point(-3, -1, 2),
      Point(3, -1, -2),
      Point(-3, 1, -2),
      Point(-3, -1, -2),

      Point(2, 3, 1),
      Point(-2, 3, 1),
      Point(2, -3, 1),
      Point(2, 3, -1),
      Point(-2, -3, 1),
      Point(2, -3, -1),
      Point(-2, 3, -1),
      Point(-2, -3, -1),
    ).toSet()
    assertEquals(expected, transformed)
  }
}