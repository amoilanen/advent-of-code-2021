package io.github.antivanov.aoc2021

import arrow.core.Either

object Day10 {

  val input = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
  """.trimIndent()

  fun parse(input: String): List<List<Char>> =
    input.split("\n").map {
      it.trim().toList()
    }


  private val openingBrackets: List<Char> = "([{<".toList()
  private val closingBrackets: List<Char> = ")]}>".toList()
  private val closingToOpeningBracket: Map<Char, Char> = closingBrackets.zip(openingBrackets).toMap()
  val invalidBracketPoints = hashMapOf(
    '(' to 3,
    '[' to 57,
    '{' to 1197,
    '<' to 25137
  )

  fun isOpeningBracket(ch: Char): Boolean =
    openingBrackets.contains(ch)

  fun isClosingBracket(ch: Char): Boolean =
    closingBrackets.contains(ch)

  fun evaluateCorrectness(expression: List<Char>): Either<Char, List<Char>> {
    fun readNext(input: List<Char>, stack: List<Char>): Either<Char, List<Char>> =
      if (input.isEmpty()) {
        Either.Right(stack)
      } else {
        val nextSymbol = input.first()
        val restOfInput = input.drop(1)
        if (isOpeningBracket(nextSymbol)) {
          val updatedStack = listOf(nextSymbol) + stack
          readNext(restOfInput, updatedStack)
        } else if (isClosingBracket(nextSymbol)) { // nextBracket is a closing bracket
          if (stack.isEmpty()) {
            Either.Left(nextSymbol)
          } else {
            val stackTop = stack.first()
            val updatedStack = stack.drop(1)
            val expectedStackTop = closingToOpeningBracket[nextSymbol]
            if (stackTop != expectedStackTop) {
              Either.Left(nextSymbol)
            } else {
              readNext(restOfInput, updatedStack)
            }
          }
        } else {
          throw IllegalStateException("Not an opening or closing bracket: $nextSymbol")
        }
      }

    return readNext(expression, emptyList())
  }
}

fun main() {
  val parsedInput = Day10.parse(Day10.input)
  println(parsedInput)

  val correctness = parsedInput.map {
    Day10.evaluateCorrectness(it)
  }.filter { it.isLeft() }
  println(correctness)
}