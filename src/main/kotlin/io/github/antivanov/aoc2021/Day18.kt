package io.github.antivanov.aoc2021

import java.util.ArrayDeque
import kotlin.Pair as Tuple

object Day18 {

  val input = """
    [1,1]
    [2,2]
    [3,3]
    [4,4]
  """.trimIndent()

  fun updateChildTo(child: Element, newChild: Element): Unit {
    val parent = child.parent
    newChild.parent = parent
    if (parent?.left == child)
      parent?.left = newChild
    else
      parent?.right = newChild
  }

  abstract class Element(var children: List<Element>, open var parent: Pair?) {

    fun add(other: Element): Element {
      val result = Pair(this, other)
      this.parent = result
      other.parent = result
      return result.reduce()
    }

    fun nestednessLevel(): Int {
      var nestedness = 0
      var node = parent
      while (node != null) {
        node = node.parent
        nestedness += 1
      }
      return nestedness
    }

    abstract fun addLinksBack(): Element

    abstract fun allNumbers(): List<Number>

    val NumberValueToSplitAfter = 10
    val NestednessLevelToExplode = 5

    private fun tryExplode(numbers: List<Number>): Tuple<Boolean, Element> {

      fun explodePair(pair: Pair) =
        updateChildTo(pair, Number(0))

      fun updateAdjacentNumbers(explodingLeftIndex: Int, explodingRightIndex: Int) {
        val firstRegularNumberToLeft = if (explodingLeftIndex > 0)
          numbers[explodingLeftIndex - 1]
        else
          null
        val firstRegularNumberToRight = if (explodingRightIndex < numbers.size - 1)
          numbers[explodingRightIndex + 1]
        else
          null
        val explodingPairLeft = numbers[explodingLeftIndex]
        val explodingPairRight = numbers[explodingRightIndex]
        if (firstRegularNumberToLeft != null)
          firstRegularNumberToLeft.value += explodingPairLeft.value
        if (firstRegularNumberToRight != null)
          firstRegularNumberToRight.value += explodingPairRight.value
      }

      val nestednessLevels = numbers.map { it.nestednessLevel() }.zip(0 until numbers.size)
      val indicesOfDeepPairs = nestednessLevels.filter {
        it.first == NestednessLevelToExplode
      }.map {
        it.second
      }

      if (indicesOfDeepPairs.isNotEmpty()) {
        val explodingLeftIndex = indicesOfDeepPairs[0]
        val explodingRightIndex = indicesOfDeepPairs[1]
        explodePair(numbers[explodingLeftIndex].parent!!)
        updateAdjacentNumbers(explodingLeftIndex, explodingRightIndex)
      }
      return indicesOfDeepPairs.isNotEmpty() to this
    }

    private fun trySplitting(numbers: List<Number>): Tuple<Boolean, Element> {
      val numbersToSplit = numbers.zip(0 until numbers.size).filter {
        it.first.value >= NumberValueToSplitAfter
      }
      if (numbersToSplit.isNotEmpty()) {
        val firstNumberToSplit = numbersToSplit.first()
        val numberBeingSplit = numbers[firstNumberToSplit.second]
        val left = numberBeingSplit.value / 2
        val right = numberBeingSplit.value - left
        val splitPair = Pair(
          Number(left),
          Number(right)
        ).addLinksBack()
        updateChildTo(numberBeingSplit, splitPair)
      }
      return numbersToSplit.isNotEmpty() to this
    }

    fun reduceOnce(): Tuple<Boolean, Element> {
      val numbers = allNumbers()
      val (hasExploded, elementAfterTryingToExplode) = tryExplode(numbers)

      return if (!hasExploded) {
        val (hasSplitted, elementAfterTryingToSplit) = trySplitting(numbers)
        hasSplitted to elementAfterTryingToSplit
      } else
        hasExploded to elementAfterTryingToExplode
    }

    fun reduce(): Element {
      var (hasBeenReduced, reduced) = this.reduceOnce()
      while (hasBeenReduced) {
        val reductionResult = this.reduceOnce()
        hasBeenReduced = reductionResult.first
        reduced = reductionResult.second
      }
      return reduced
    }
  }

  data class Pair(var left: Element, var right: Element, override var parent: Pair? = null): Element(listOf(left, right), parent) {

    override fun addLinksBack(): Element {
      children.forEach {
        it.parent = this
        it.addLinksBack()
      }
      return this
    }

    override fun allNumbers(): List<Number> =
      left.allNumbers() + right.allNumbers()

    override fun toString(): String =
      "[$left,$right]"
  }

  data class Number(var value: Int, override var parent: Pair? = null): Element(emptyList(), parent) {

    override fun addLinksBack(): Element =
      this

    override fun allNumbers(): List<Number> =
      listOf(this)

    override fun toString(): String =
      value.toString()
  }

  private fun isDigit(symbol: Char): Boolean =
    "0123456789".contains(symbol)

  fun parseElementWithoutBackLinks(input: String): Element {
    var pointer = 0
    var stack = ArrayDeque<Element>()
    while (pointer != input.length) {
      var nextSymbol = input[pointer]
      if (nextSymbol == '[' || nextSymbol == ',') {
        pointer += 1
      } else if (nextSymbol == ']') {
        val right = stack.pop()
        val left = stack.pop()
        stack.push(Pair(left, right))
        pointer += 1
      } else {
        var digitsRead = emptyList<Char>()
        while (isDigit(nextSymbol)) {
          digitsRead += nextSymbol
          pointer += 1
          nextSymbol = input[pointer]
        }
        val readNumber = digitsRead.joinToString("").toInt()
        stack.push(Number(readNumber))
      }
    }
    return stack.pop()
  }

  fun parseElement(input: String): Element =
    parseElementWithoutBackLinks(input).addLinksBack()

  fun sum(numbers: List<Element>): Element {
    val first = numbers.first()
    val rest = numbers.subList(1, numbers.size)
    return rest.fold(first) { acc, next ->
      acc.add(next)
    }
  }

  fun parse(input: String): List<Element> {
    val lines = input.split("\n").map {
      it.trim()
    }
    return lines.map {
      parseElement(it)
    }
  }
}

fun main() {
  val input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
  val parsed = Day18.parseElement(input).addLinksBack()
  println(parsed)
  val allElements = parsed.allNumbers()
  println(allElements)

  val reducedOnce = parsed.reduceOnce().toString()
  println(reducedOnce)
}