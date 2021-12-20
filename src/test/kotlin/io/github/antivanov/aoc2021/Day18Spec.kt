package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.Day18.parse
import io.github.antivanov.aoc2021.Day18.parseWithoutBackLinks
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
    assertEquals(expected, parseWithoutBackLinks(input))
  }

  @Test
  fun test_parse_snailfish_number_simple_pair() {
    val input = "[1,2]"
    val expected = Pair(
      Number(1),
      Number(2)
    )
    assertEquals(expected, parseWithoutBackLinks(input))
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
    assertEquals(expected, parseWithoutBackLinks(input))
  }

  @Test
  fun test_parse_snailfish_number_multiple_digit_numbers() {
    val input = "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
    val parsedExpected = Pair(
      Pair(
        Pair(
          Pair(
            Number(0),
            Number(7)
          ),
          Number(4)
        ),
        Pair(
          Pair(
            Number(7),
            Number(8)
          ),
          Pair(
            Number(0),
            Number(13)
          )
        )),
      Pair(
        Number(1),
        Number(1)))
    assertEquals(parsedExpected, parseWithoutBackLinks(input))
  }

  private fun reduceOnce(number: String): String {
    val parsed = parse(number)
    return parsed.reduceOnce().second.toString()
  }

  private fun reduce(number: String): String {
    val parsed = parse(number)
    return parsed.reduce().toString()
  }

  @Test
  fun test_reduce_once_exploding_1() {
    val input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
    val expected = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_exploding_2() {
    val input = "[[[[[9,8],1],2],3],4]"
    val expected = "[[[[0,9],2],3],4]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_exploding_3() {
    val input = "[7,[6,[5,[4,[3,2]]]]]"
    val expected = "[7,[6,[5,[7,0]]]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_exploding_4() {
    val input = "[[6,[5,[4,[3,2]]]],1]"
    val expected = "[[6,[5,[7,0]]],3]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_exploding_5() {
    val input = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    val expected = "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_splitting_1() {
    val input = "[[[[0,7],4],[15,[0,13]]],[1,1]]"
    val expected = "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_once_splitting_2() {
    val input = "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
    val expected = "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"
    assertEquals(expected, reduceOnce(input))
  }

  @Test
  fun test_reduce_fully() {
    val input = "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"
    val expected = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    assertEquals(expected, reduce(input))
  }

  @Test
  fun test_addition() {
    val x = parse("[[[[4,3],4],4],[7,[[8,4],9]]]")
    val y = parse("[1,1]")
    val sum = x.add(y)
    val expected = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    assertEquals(expected, sum.toString())
  }
}