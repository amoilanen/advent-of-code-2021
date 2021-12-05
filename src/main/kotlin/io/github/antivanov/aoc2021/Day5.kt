package io.github.antivanov.aoc2021

import kotlin.math.max
import kotlin.math.min

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
      val minX = min(start.x, end.x)
      val maxX = max(start.x, end.x)
      val minY = min(start.y, end.y)
      val maxY = max(start.y, end.y)
      return (minX..maxX).flatMap { x ->
        (minY..maxY).map { y ->
          Point(x, y)
        }
      }
    }

    fun isHorizontalOrVerticalLine(): Boolean =
      start.x == end.x || start.y == end.y
  }

  val lineRegex = "(\\d+),(\\d+)\\s*->\\s*(\\d+),(\\d+)".toRegex()

  private fun parseLine(lineInput: String): Line =
    lineRegex.matchEntire(lineInput)!!.destructured.let { (startX, startY, endX, endY) ->
      Line(Point(startX.toInt(), startY.toInt()), Point(endX.toInt(), endY.toInt()))
    }

  fun parseInput(inputLines: List<String>): List<Line> =
    inputLines.map { parseLine(it) }

  fun part1(lines: List<Line>): Int {
    val pointsOfVerticalOrHorizontalLines = lines.filter {
      it.isHorizontalOrVerticalLine()
    }.flatMap {
      it.getPoints()
    }
    val pointMarkings: Map<Point, Int> = pointsOfVerticalOrHorizontalLines.groupBy { it }.mapValues { it.value.size }
    val pointsMarkedMultipleTimes: Set<Point> = pointMarkings.filterValues { it >= 2 }.keys
    return pointsMarkedMultipleTimes.size
  }
}

fun main() {
  val inputLines = Day5.input.split("\n").map { it.trimIndent() }
  val lines = Day5.parseInput(inputLines)

  println(Day5.part1(lines))
}