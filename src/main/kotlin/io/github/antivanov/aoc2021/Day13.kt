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

  data class DottedPaper(val dots: Set<Point>) {
    private val width = (dots.map { it.x } + setOf(-1)).maxOrNull()!! + 1
    private val height = (dots.map { it.y } + setOf(-1)).maxOrNull()!! + 1

    private fun isDotted(point: Point): Boolean =
      dots.contains(point)

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
    val paper = DottedPaper(dots)

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
}