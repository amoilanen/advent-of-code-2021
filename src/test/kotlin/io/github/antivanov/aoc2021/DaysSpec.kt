package io.github.antivanov.aoc2021

import arrow.core.Some
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

  val day4TestInput = Day4.parseInput("""
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
  """.trimIndent().split("\n").map { it.trimIndent() }
  )

  @Test
  fun test_day4_part1() {
    val (drawnNumbers, boards) = day4TestInput
    assertEquals(Some(4512), Day4.part1(drawnNumbers, boards))
  }
}