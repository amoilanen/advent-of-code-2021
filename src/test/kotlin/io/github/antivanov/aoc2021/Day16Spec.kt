package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day16.parse
import io.github.antivanov.aoc2021.Day16.Literal

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
    val input = "68a"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 6, 10)
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  @Test
  fun test_parse_literal_3() {
    val input = "224"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 4, 20)
    assertEquals(parse(input), expected to lastPointerPosition)
  }

  //TODO: Read an operator which contains two numbers
}