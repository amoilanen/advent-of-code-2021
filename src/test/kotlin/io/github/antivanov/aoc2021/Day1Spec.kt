package io.github.antivanov.aoc2021

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Spec {

  val day1TestInput = Arrays.asList(
    199,
    200,
    208,
    210,
    200,
    207,
    240,
    269,
    260,
    263
  )

  @Test
  fun test_day1_part1() {
    assertEquals(7, Day1.part1(day1TestInput))
  }

  @Test
  fun test_day1_part2() {
    assertEquals(5, Day1.part2(day1TestInput))
  }
}