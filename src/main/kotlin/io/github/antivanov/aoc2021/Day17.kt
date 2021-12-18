package io.github.antivanov.aoc2021

object Day17 {
  val input = "target area: x=20..30, y=-10..-5"

  data class Point(val x: Int, val y: Int)
  data class Area(val topLeft: Point, val bottomRight: Point)

  private val targetAreaRegex = "target area: x=([\\d\\-]+)\\.\\.([\\d\\-]+), y=([\\d\\-]+)..([\\d\\-]+)".toRegex()

  fun parseInput(input: String): Area {
    return targetAreaRegex.matchEntire(input)!!.destructured.let { (x1, x2, y1, y2) ->
      val xs = listOf(x1, x2).map { it.toInt() }.sorted()
      val ys = listOf(y1, y2).map { it.toInt() }.sorted().reversed()
      Area(
        Point(xs[0], ys[0]),
        Point(xs[1], ys[1])
      )
    }
  }
}

fun main() {
  val targetArea = Day17.parseInput(Day17.input)
  println(targetArea)
}