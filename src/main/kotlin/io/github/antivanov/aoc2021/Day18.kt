package io.github.antivanov.aoc2021

import java.util.ArrayDeque
import kotlin.Pair as Tuple

object Day18 {

  abstract class Element(var children: List<Element>, open var parent: Pair?) {

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

    val NestednessLevelToExplode = 5

    private fun tryExplode(numbers: List<Number>): Tuple<Boolean, Element> {

      fun explodePair(pair: Pair) {
        val explodingPairParent = pair.parent
        if (explodingPairParent?.left == pair)
          explodingPairParent?.left = Number(0, explodingPairParent)
        else
          explodingPairParent?.right = Number(0, explodingPairParent)
      }

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

      val numbers = allNumbers()
      val nestednessLevels = numbers.map { it.nestednessLevel() }.zip(0 until numbers.size)
      val indicesOfDeepPairs = nestednessLevels.filter {
        it.first == NestednessLevelToExplode
      }.map {
        it.second
      }

      var hasChanged = false
      if (indicesOfDeepPairs.isNotEmpty()) {
        hasChanged = true
        val explodingLeftIndex = indicesOfDeepPairs[0]
        val explodingRightIndex = indicesOfDeepPairs[1]
        explodePair(numbers[explodingLeftIndex].parent!!)
        updateAdjacentNumbers(explodingLeftIndex, explodingRightIndex)
      }
      return hasChanged to this
    }

    fun reduceOnce(): Tuple<Boolean, Element> {
      val numbers = allNumbers()
      val (hasExploded, updatedElement) = tryExplode(numbers)

      if (!hasExploded) {
        //TODO: Try splitting
      }
      val hasChanged = hasExploded

      return hasChanged to this
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

  fun parse(input: String): Element {
    var pointer = 0
    var stack = ArrayDeque<Element>()
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
  val parsed = Day18.parse(input).addLinksBack()
  println(parsed)
  val allElements = parsed.allNumbers()
  println(allElements)

  val reducedOnce = parsed.reduceOnce().toString()
  println(reducedOnce)
}