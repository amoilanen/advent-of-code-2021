package io.github.antivanov.aoc2021

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DaysSpec {

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

  val day2TestInput = Arrays.asList(
    Day2.MovementInstruction.Forward(5),
    Day2.MovementInstruction.Down(5),
    Day2.MovementInstruction.Forward(8),
    Day2.MovementInstruction.Up(3),
    Day2.MovementInstruction.Down(8),
    Day2.MovementInstruction.Forward(2)
  )

  @Test
  fun test_day2_part1() {
    assertEquals(150, Day2.part1(day2TestInput))
  }

  @Test
  fun test_day2_part2() {
    assertEquals(900, Day2.part2(day2TestInput))
  }

  val day3TestInput: List<List<Int>> = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
  """.trimIndent().split("\n").map { it.toCharArray().toList().map { it.toString().toInt() } }

  @Test
  fun test_day3_part1() {
    assertEquals(198, Day3.part1(day3TestInput))
  }

  @Test
  fun test_day3_part2() {
    assertEquals(230, Day3.part2(day3TestInput))
  }
}