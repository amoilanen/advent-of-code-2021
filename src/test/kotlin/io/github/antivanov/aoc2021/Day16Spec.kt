package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day16.parse
import io.github.antivanov.aoc2021.Day16.Literal
import io.github.antivanov.aoc2021.Day16.Operator

class Day16Spec {

  @Test
  fun test_parse_literal_1() {
    val input = "D2FE28"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 6, 2021)
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  @Test
  fun test_parse_literal_2() {
    val input = "D14"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 6, 10)
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  @Test
  fun test_parse_literal_3() {
    val input = "5224"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 2, 20)
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  @Test
  fun test_parse_operator_1() {
    val input = "38006F45291200"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version = 1, type = 6, packets = listOf(
      Literal(version=6, value=10),
      Literal(version=2, value=20)
    ))
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  @Test
  fun test_parse_operator_2() {
    val input = "EE00D40C823060"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version = 7, type = 3, packets = listOf(
      Literal(version=2, value=1),
      Literal(version=4, value=2),
      Literal(version=1, value=3),
    ))
    assertEquals(parse(input), expected to lastPointerPosition)
  }
}