package io.github.antivanov.aoc2021

object Day5 {
  val input = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
  """.trimIndent()

  data class Point(val x: Int, val y : Int)
  data class Line(val start: Point, val end: Point) {
    fun getPoints(): List<Point> {
      val xLength = Math.abs(end.x - start.x)
      val yLength = Math.abs(end.y - start.y)
      val xDirection = if (xLength > 0)
        (end.x - start.x) / xLength
      else
        0
      val yDirection = if (yLength > 0)
        (end.y - start.y) / yLength
      else
        0
      val steps = 0..Math.max(xLength, yLength)
      return steps.map { step ->
        Point(start.x + xDirection * step, start.y + yDirection * step)
      }
    }

    fun isHorizontalOrVerticalLine(): Boolean =
      start.x == end.x || start.y == end.y

    fun isDiagonalLine(): Boolean =
      Math.abs(start.x - end.x) == Math.abs(start.y - end.y)
  }

  private val lineRegex = "(\\d+),(\\d+)\\s*->\\s*(\\d+),(\\d+)".toRegex()

  private fun parseLine(lineInput: String): Line =
    lineRegex.matchEntire(lineInput)!!.destructured.let { (startX, startY, endX, endY) ->
      Line(Point(startX.toInt(), startY.toInt()), Point(endX.toInt(), endY.toInt()))
    }

  fun parseInput(inputLines: List<String>): List<Line> =
    inputLines.map { parseLine(it) }

  private fun computeCommonPointsCount(lines: List<Line>): Int {
    val linePoints = lines.flatMap { it.getPoints() }
    val pointMarkings: Map<Point, Int> = linePoints.groupBy { it }.mapValues { it.value.size }
    val pointsMarkedMultipleTimes: Set<Point> = pointMarkings.filterValues { it >= 2 }.keys
    return pointsMarkedMultipleTimes.size
  }

  fun part1(lines: List<Line>): Int {
    return computeCommonPointsCount(lines.filter {
      it.isHorizontalOrVerticalLine()
    })
  }

  fun part2(lines: List<Line>): Int {
    return computeCommonPointsCount(lines.filter {
      it.isHorizontalOrVerticalLine() || it.isDiagonalLine()
    })
  }
}

fun main() {
  val inputLines = Day5.input.split("\n").map { it.trimIndent() }
  val lines = Day5.parseInput(inputLines)

  println(Day5.part1(lines))
  println(Day5.part2(lines))
}