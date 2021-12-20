package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.Day18.parse
import io.github.antivanov.aoc2021.Day18.Pair
import io.github.antivanov.aoc2021.Day18.Number
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Spec {

  @Test
  fun test_parse_snailfish_number_nested_pairs() {
    val input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
    val expected = Pair(
      Pair(
        Number(3),
        Pair(
          Number(2),
          Pair(
            Number(1),
              Pair(
                Number(value=7),
                Number(value=3))))),
      Pair(
        Number(value=6),
        Pair(
          Number(value=5),
          Pair(
            Number(value=4),
            Pair(
              Number(value=3),
              Number(value=2))))))
    assertEquals(expected, parse(input))
  }

  @Test
  fun test_parse_snailfish_number_simple_pair() {
    val input = "[1,2]"
    val expected = Pair(
      Number(1),
      Number(2)
    )
    assertEquals(expected, parse(input))
  }

  @Test
  fun test_parse_snailfish_number_one_level_nested() {
    val input = "[9,[8,7]]"
    val expected = Pair(
      Number(9),
      Pair(
        Number(8),
        Number(7)
      )
    )
    assertEquals(expected, parse(input))
  }

  fun reduceOnce(number: String): String {
    val parsed = parse(number).addLinksBack()
    return parsed.reduceOnce().second.toString()
  }

  @Test
  fun test_reduce_once_1() {
    val input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
    val expected = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_2() {
    val input = "[[[[[9,8],1],2],3],4]"
    val expected = "[[[[0,9],2],3],4]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_3() {
    val input = "[7,[6,[5,[4,[3,2]]]]]"
    val expected = "[7,[6,[5,[7,0]]]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_4() {
    val input = "[[6,[5,[4,[3,2]]]],1]"
    val expected = "[[6,[5,[7,0]]],3]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_5() {
    val input = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    val expected = "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    assertEquals(expected, reduceOnce(input))
  }
}