package io.github.antivanov.aoc2021

import java.util.ArrayDeque
import kotlin.Pair as Tuple

object Day18 {

  abstract class Element(var children: List<Element>, open var parent: Element?) {
    fun addLinksBack(): Element {
      children.forEach {
        it.parent = this
        it.addLinksBack()
      }
      return this
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

    abstract fun allNumbers(): List<Number>

    val NestednessLevelToExplode = 5

    fun reduceOnce(): Tuple<Boolean, Element> {
      var hasChanged = false
      val numbers = allNumbers()
      val nestednessLevels = numbers.map { it.nestednessLevel() }.zip(0 until numbers.size)
      //println("Nestedness levels:")
      //println(nestednessLevels)
      val indicesOfDeepPairs = nestednessLevels.filter {
        it.first == NestednessLevelToExplode
      }.map {
        it.second
      }

      //TODO: Extract sub-methods
      if (indicesOfDeepPairs.size > 0) {
        hasChanged = true
        //TODO: More careful handling of indices
        val explodingLeftIndex = indicesOfDeepPairs[0]
        val explodingRightIndex = indicesOfDeepPairs[1]
        val firstRegularNumberToLeft = if (explodingLeftIndex > 0)
          numbers[explodingLeftIndex - 1]
        else
          null
        val firstRegularNumberToRight = if (explodingRightIndex < numbers.size - 1)
          numbers[explodingRightIndex + 1]
        else
          null
        val explodingPair = numbers[explodingLeftIndex].parent!!
        val explodingPairLeft = numbers[explodingLeftIndex]
        val explodingPairRight = numbers[explodingRightIndex]
        val explodingPairParent = explodingPair.parent
        when (explodingPairParent) {
          //TODO: Parent is always a Pair, cannot be a Number, express is better in the types
          is Pair ->
            if (explodingPairParent.left == explodingPair)
              explodingPairParent.left = Number(0, explodingPairParent)
            else
              explodingPairParent.right = Number(0, explodingPairParent)
          else -> {}
        }
        if (firstRegularNumberToLeft != null)
          firstRegularNumberToLeft.value += explodingPairLeft.value
        if (firstRegularNumberToRight != null)
          firstRegularNumberToRight.value += explodingPairRight.value
      } else {
        //TODO: Implement splitting
      }

      return hasChanged to this
    }
  }

  data class Pair(var left: Element, var right: Element, override var parent: Element? = null): Element(listOf(left, right), parent) {
    override fun allNumbers(): List<Number> =
      left.allNumbers() + right.allNumbers()

    override fun toString(): String =
      "[$left,$right]"
  }

  data class Number(var value: Int, override var parent: Element? = null): Element(emptyList(), parent) {
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