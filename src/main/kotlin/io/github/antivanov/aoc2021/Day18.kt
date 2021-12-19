package io.github.antivanov.aoc2021

import java.util.ArrayDeque

object Day18 {

  interface Expression
  data class Pair(val left: Expression, val right: Expression): Expression
  data class Number(val value: Int): Expression

  fun parse(input: String): Expression {
    var pointer = 0
    var stack = ArrayDeque<Expression>()
    while (pointer != input.length) {
      val nextSymbol = input[pointer]
      if (nextSymbol == '[' || nextSymbol == ',') {
        pointer += 1
      } else if (nextSymbol == ']') {
        val right = stack.pop()
        val left = stack.pop()
        stack.push(Pair(left, right))
        pointer += 1
      } else {
        stack.push(Number(nextSymbol.toString().toInt()))
        pointer += 1
      }
    }
    return stack.pop()
  }

}

fun main() {
  val input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
  val parsed = Day18.parse(input)
  println(parsed)
}