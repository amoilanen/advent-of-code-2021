package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day16.parsePacket
import io.github.antivanov.aoc2021.Day16.Literal

class Day16Spec {

  @Test
  fun test_parse_literal() {
    val input = "D2FE28"
    val lastPointerPosition = input.length * 4
    val expected = Literal(version = 6, 2021)
    assertEquals(parsePacket(input), expected to lastPointerPosition)
  }

  //TODO: Read an operator which contains two numbers
}