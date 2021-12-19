package io.github.antivanov.aoc2021

import java.util.ArrayDeque

object Day18 {

  abstract class Element(var children: List<Element>, open var parent: Element?) {
    fun addLinksBack(): Element {
      children.forEach {
        it.parent = this
        it.addLinksBack()
      }
      return this
    }

    abstract fun allNumbers(): List<Element>

    abstract fun reduceOnce(): Element
  }

  data class Pair(var left: Element, var right: Element, override var parent: Element? = null): Element(listOf(left, right), parent) {
    override fun allNumbers(): List<Element> =
      left.allNumbers() + right.allNumbers()

    override fun reduceOnce(): Element {
      TODO("Not yet implemented")
    }

    override fun toString(): String =
      "[$left, $right]"
  }
  data class Number(var value: Int, override var parent: Element? = null): Element(emptyList(), parent) {
    override fun allNumbers(): List<Element> =
      listOf(this)

    override fun reduceOnce(): Element {
      TODO("Not yet implemented")
    }

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
}