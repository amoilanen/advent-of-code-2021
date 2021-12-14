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
      val (foldedDots, updatedHeight) = foldDots(
        foldY,
        height,
        { p -> p.y },
        { p, value -> p.copy(y = value) }
      )
      return DottedPaper(foldedDots, width, updatedHeight)
    }

    fun foldAlongX(foldX: Int): DottedPaper {
      val (foldedDots, updatedWidth) = foldDots(
        foldX,
        width,
        { p -> p.x },
        { p, value -> p.copy(x = value) }
      )
      return DottedPaper(foldedDots, updatedWidth, height)
    }

    private fun foldDots(foldValue: Int, dimensionValue: Int, coordinateAccessor: (Point) -> Int, coordinateUpdator: (Point, Int) -> Point): Pair<Set<Point>, Int> {
      val dotsAfter = dots.filter { coordinateAccessor(it) > foldValue }
      val dotsBefore = dots.filter { coordinateAccessor(it) < foldValue }

      val beforeDimension = foldValue

      val dotsAfterRelativeFoldCoordinates = dotsAfter.map {
        coordinateUpdator(it, coordinateAccessor(it) - foldValue - 1)
      }
      val afterDimension = dimensionValue - beforeDimension - 1

      val dotsAfterShift = if (beforeDimension > afterDimension)
        beforeDimension - afterDimension
      else
        0
      val foldedDotsAfter = dotsAfterRelativeFoldCoordinates.map {
        coordinateUpdator(it, dotsAfterShift + afterDimension - 1 - coordinateAccessor(it))
      }.toSet()

      val dotsBeforeShift = if (afterDimension > beforeDimension)
        afterDimension - beforeDimension
      else
        0
      val foldedDotsBefore = dotsBefore.map {
        coordinateUpdator(it, dotsBeforeShift + coordinateAccessor(it))
      }.toSet()

      val updatedDimension = maxOf(afterDimension, beforeDimension)
      return (foldedDotsAfter + foldedDotsBefore) to updatedDimension
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
    val paper = parseDottedPaper(paperAndInstructionInputs[0])
    val instructions = parseFoldingInstructions(paperAndInstructionInputs[1])
    return paper to instructions
  }

  fun parseDottedPaper(paperInput: List<String>): DottedPaper {
    val dots = paperInput.map { dotInput ->
      val xAndY = dotInput.split(",").map {
        it.toInt()
      }
      Point(xAndY[0], xAndY[1])
    }.toSet()

    val width = (dots.map { it.x } + setOf(-1)).maxOrNull()!! + 1
    val height = (dots.map { it.y } + setOf(-1)).maxOrNull()!! + 1
    return DottedPaper(dots, width, height)
  }

  fun parseFoldingInstructions(instructionsInput: List<String>): List<FoldingInstruction> =
    instructionsInput.map {
      foldingInstructionRegex.matchEntire(it)!!.destructured.let { (axis, value) ->
        FoldingInstruction(axis, value.toInt())
      }
    }

  fun applyInstruction(paper: DottedPaper, instruction: FoldingInstruction): DottedPaper =
    when (instruction.axis) {
      "x" -> paper.foldAlongX(instruction.value)
      "y" -> paper.foldAlongY(instruction.value)
      else -> paper
    }

  fun part1(paper: DottedPaper, instructions: List<FoldingInstruction>): Int {
    val firstInstruction = instructions.first()
    val partiallyFoldedPaper = applyInstruction(paper, firstInstruction)
    return partiallyFoldedPaper.dots.size
  }

  fun part2(paper: DottedPaper, instructions: List<FoldingInstruction>): DottedPaper =
    instructions.fold(paper) { currentPaper, instruction ->
      applyInstruction(currentPaper, instruction)
    }
}

fun main() {
  val (paper, instructions) = Day13.parseInput(Day13.input)
  println(Day13.part1(paper, instructions))
  println(Day13.part2(paper, instructions))
}