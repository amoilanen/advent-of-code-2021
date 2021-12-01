package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Spec {

  @Test
  fun test_day1_part1() {
    val input = """
  199
  200
  208
  210
  200
  207
  240
  269
  260
  263
""".trimIndent()
    assertEquals(7, Day1.part1(input))
  }
}