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

  @Test
  fun test_evaluate_operator_sum() {
    val input = "C200B40A82"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(6, 0, packets = listOf(
      Literal(version = 6, value = 1),
      Literal(version = 2, value = 2)
    )))
    assertEquals(operator.evaluate(), 3)
  }

  @Test
  fun test_evaluate_operator_product() {
    val input = "04005AC33890"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(0, 1, packets = listOf(
      Literal(version = 5, value = 6),
      Literal(version = 3, value = 9)
    )))
    assertEquals(operator.evaluate(), 54)
  }

  @Test
  fun test_evaluate_operator_min() {
    val input = "880086C3E88112"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(4, 2, packets = listOf(
      Literal(version = 5, value = 7),
      Literal(version = 6, value = 8),
      Literal(version = 0, value = 9)
    )))
    assertEquals(operator.evaluate(), 7)
  }

  @Test
  fun test_evaluate_operator_max() {
    val input = "CE00C43D881120"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(6, 3, packets = listOf(
      Literal(version = 0, value = 7),
      Literal(version = 5, value = 8),
      Literal(version = 0, value = 9)
    )))
    assertEquals(operator.evaluate(), 9)
  }

  @Test
  fun test_evaluate_operator_greater_than() {
    val input = "F600BC2D8F"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(7, 5, packets = listOf(
      Literal(version = 7, value = 5),
      Literal(version = 5, value = 15)
    )))
    assertEquals(operator.evaluate(), 0)
  }

  @Test
  fun test_evaluate_operator_less_than() {
    val input = "D8005AC2A8F0"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(6, 6, packets = listOf(
      Literal(version = 5, value = 5),
      Literal(version = 2, value = 15)
    )))
    assertEquals(operator.evaluate(), 1)
  }

  @Test
  fun test_evaluate_operator_equal_to() {
    val input = "9C0141080250320F1802104A08"
    val (operator, _) = parse(input)
    assertEquals(operator, Operator(4, 7, packets = listOf(
      Operator(2, 0, packets = listOf(
        Literal(version = 2, value = 1),
        Literal(version = 4, value = 3)
      )),
      Operator(6, 1, packets = listOf(
        Literal(version = 0, value = 2),
        Literal(version = 2, value = 2)
      ))
    )))
    assertEquals(operator.evaluate(), 1)
  }
}