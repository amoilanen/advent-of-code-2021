package io.github.antivanov.aoc2021

import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.antivanov.aoc2021.Day16.parse
import io.github.antivanov.aoc2021.Day16.versionNumberSumOf
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

  @Test
  fun test_parse_operator_3() {
    val input = "8A004A801A8002F478"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version = 4, type = 2, packets = listOf(
      Operator(version=1, type=2, packets = listOf(
        Operator(version=5, type=2, packets= listOf(
          Literal(version=6, value=15)
        ))
      ))
    ))
    val parsed = parse(input)
    assertEquals(parsed, expected to lastPointerPosition)
    assertEquals(versionNumberSumOf(parsed.first), 16)
  }

  @Test
  fun test_parse_operator_4() {
    val input = "620080001611562C8802118E34"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version = 3, type = 0, packets = listOf(
      Operator(version=0, type=0, packets=listOf(
        Literal(version=0, value=10),
        Literal(version=5, value=11)
      )),
      Operator(version=1, type=0, packets=listOf(
        Literal(version=0, value=12),
        Literal(version=3, value=13)
      ))
    ))
    val parsed = parse(input)
    assertEquals(parsed, expected to lastPointerPosition)
    assertEquals(versionNumberSumOf(parsed.first), 12)
  }

  @Test
  fun test_parse_operator_5() {
    val input = "C0015000016115A2E0802F182340"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version=6, type=0, packets=listOf(
      Operator(version=0, type=0, packets=listOf(
        Literal(version=0, value=10),
        Literal(version=6, value=11)
      )),
      Operator(version=4, type=0, packets=listOf(
        Literal(version=7, value=12),
        Literal(version=0, value=13)
      ))
    ))
    val parsed = parse(input)
    assertEquals(parsed, expected to lastPointerPosition)
    assertEquals(versionNumberSumOf(parsed.first), 23)
  }

  @Test
  fun test_parse_operator_6() {
    val input = "A0016C880162017C3686B18A3D4780"
    val lastPointerPosition = input.length * 4
    val expected = Operator(version=5, type=0, packets=listOf(
      Operator(version=1, type=0, packets=listOf(
        Operator(version=3, type=0, packets=listOf(
          Literal(version=7, value=6),
          Literal(version=6, value=6),
          Literal(version=5, value=12),
          Literal(version=2, value=15),
          Literal(version=2, value=15)))))))
    val parsed = parse(input)
    assertEquals(parsed, expected to lastPointerPosition)
    assertEquals(versionNumberSumOf(parsed.first), 31)
  }
}