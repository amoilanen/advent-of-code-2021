package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.Day18.parse
import io.github.antivanov.aoc2021.Day18.sum
import io.github.antivanov.aoc2021.Day18.parseElement
import io.github.antivanov.aoc2021.Day18.parseElementWithoutBackLinks
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
                Number(7),
                Number(3))))),
      Pair(
        Number(6),
        Pair(
          Number(5),
          Pair(
            Number(4),
            Pair(
              Number(3),
              Number(2))))))
    assertEquals(expected, parseElementWithoutBackLinks(input))
  }

  @Test
  fun test_parse_snailfish_number_simple_pair() {
    val input = "[1,2]"
    val expected = Pair(
      Number(1),
      Number(2)
    )
    assertEquals(expected, parseElementWithoutBackLinks(input))
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
    assertEquals(expected, parseElementWithoutBackLinks(input))
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
    assertEquals(parsedExpected, parseElementWithoutBackLinks(input))
  }

  private fun reduceOnce(number: String): String {
    val parsed = parseElement(number)
    return parsed.reduceOnce().second.toString()
  }

  private fun reduce(number: String): String {
    val parsed = parseElement(number)
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
    val x = parseElement("[[[[4,3],4],4],[7,[[8,4],9]]]")
    val y = parseElement("[1,1]")
    val sum = x.add(y)
    val expected = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    assertEquals(expected, sum.toString())
  }

  @Test
  fun test_sum_1() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]
    """.trimIndent()
    val numbers = parse(input)
    val expected = "[[[[1,1],[2,2]],[3,3]],[4,4]]"
    assertEquals(expected, sum(numbers).toString())
  }

  @Test
  fun test_sum_2() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]
      [5,5]
    """.trimIndent()
    val numbers = parse(input)
    val expected = "[[[[3,0],[5,3]],[4,4]],[5,5]]"
    assertEquals(expected, sum(numbers).toString())
  }

  @Test
  fun test_sum_3() {
    val input = """
      [1,1]
      [2,2]
      [3,3]
      [4,4]
      [5,5]
      [6,6]
    """.trimIndent()
    val numbers = parse(input)
    val expected = "[[[[5,0],[7,4]],[5,5]],[6,6]]"
    assertEquals(expected, sum(numbers).toString())
  }

  @Test
  fun test_sum_4() {
    val input = """
[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
[7,[5,[[3,8],[1,4]]]]
[[2,[2,2]],[8,[8,1]]]
[2,9]
[1,[[[9,3],9],[[9,0],[0,7]]]]
[[[5,[7,4]],7],1]
[[[[4,2],2],6],[8,7]]
    """.trimIndent()
    val numbers = parse(input)
    val expected = "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    assertEquals(expected, sum(numbers).toString())
  }

  @Test
  fun test_magnitude_1() {
    val number = parseElement("[[1,2],[[3,4],5]]")
    assertEquals(143, number.magnitude())
  }

  @Test
  fun test_magnitude_2() {
    val number = parseElement("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
    assertEquals(1384, number.magnitude())
  }

  @Test
  fun test_magnitude_3() {
    val number = parseElement("[[[[1,1],[2,2]],[3,3]],[4,4]]")
    assertEquals(445, number.magnitude())
  }

  @Test
  fun test_magnitude_4() {
    val number = parseElement("[[[[3,0],[5,3]],[4,4]],[5,5]]")
    assertEquals(791, number.magnitude())
  }

  @Test
  fun test_magnitude_5() {
    val number = parseElement("[[[[5,0],[7,4]],[5,5]],[6,6]]")
    assertEquals(1137, number.magnitude())
  }

  @Test
  fun test_magnitude_6() {
    val number = parseElement("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    assertEquals(3488, number.magnitude())
  }
}