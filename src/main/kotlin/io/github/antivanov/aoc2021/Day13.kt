package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.util.ParsingUtils

object Day13 {
  val input = """
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along y=7
    fold along x=5
  """.trimIndent()

  data class Point(val x: Int, val y: Int)

  data class DottedPaper(val dots: Set<Point>, val width: Int, val height: Int) {
    private fun isDotted(point: Point): Boolean =
      dots.contains(point)

    fun foldAlongY(foldY: Int): DottedPaper {
      val dotsBelow = dots.filter { it.y > foldY }
      val dotsAbove = dots.filter { it.y < foldY }

      val aboveHeight = foldY

      val dotsBelowRelativeFoldCoordinates = dotsBelow.map {
        it.copy(y = it.y - foldY - 1)
      }
      val belowHeight = height - aboveHeight - 1

      val maxDotsBelowRelativeY = belowHeight - 1
      val dotsBelowShift = if (aboveHeight > belowHeight)
        aboveHeight - belowHeight
      else
        0
      val foldedDotsBelow = dotsBelowRelativeFoldCoordinates.map {
        it.copy(y = dotsBelowShift + maxDotsBelowRelativeY - it.y)
      }.toSet()

      val dotsAboveShift = if (belowHeight > aboveHeight)
        belowHeight - aboveHeight
      else
        0
      val foldedDotsAbove = dotsAbove.map {
        it.copy(y = it.y - dotsAboveShift)
      }.toSet()

      val allFoldedDots = foldedDotsBelow + foldedDotsAbove
      val updatedHeight = maxOf(belowHeight, aboveHeight)
      return DottedPaper(allFoldedDots, width, updatedHeight)
    }

    fun foldAlongX(x: Int): DottedPaper {
      return this
    }

    override fun toString(): String =
      (0 until height).map { y ->
        (0 until width).map { x ->
          if (isDotted(Point(x, y)))
            '#'
          else
            '.'
        }.joinToString("")
      }.joinToString("\n")
  }

  data class FoldingInstruction(val axis: String, val value: Int)

  private val foldingInstructionRegex = "fold along (x|y)=(\\d+)".toRegex()

  fun parseInput(input: String): Pair<DottedPaper, List<FoldingInstruction>> {
    val lines = input.split("\n").map { it.trim() }
    val paperAndInstructionInputs = ParsingUtils.splitByElement(lines, "")

    val paperDots = paperAndInstructionInputs[0]
    val dots = paperDots.map { dotInput ->
      val xAndY = dotInput.split(",").map {
        it.toInt()
      }
      Point(xAndY[0], xAndY[1])
    }.toSet()

    val width = (dots.map { it.x } + setOf(-1)).maxOrNull()!! + 1
    val height = (dots.map { it.y } + setOf(-1)).maxOrNull()!! + 1
    val paper = DottedPaper(dots, width, height)

    val instructionsInput = paperAndInstructionInputs[1]
    val instructions = instructionsInput.map {
      foldingInstructionRegex.matchEntire(it)!!.destructured.let { (axis, value) ->
        FoldingInstruction(axis, value.toInt())
      }
    }

    return paper to instructions
  }
}

fun main() {
  val (paper, instructions) = Day13.parseInput(Day13.input)

  println(paper)
  println(instructions)

  println()
  println("Folded:")
  val foldedPaper = paper.foldAlongY(7)
  println(foldedPaper)
}