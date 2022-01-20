package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.flattenOption
import io.github.antivanov.aoc2021.util.ParsingUtils

object Day20 {

  val input = """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
.#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
.#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###
"""

  data class Point(val x: Int, val y: Int) {

    fun relatedPointGroup(): List<Point> =
      (y - 1 .. y + 1).flatMap { y ->
        (x - 1 .. x + 1).map { x ->
          Point(x, y)
        }
      }
  }

  data class Key(val key: String) {

    private val characters = key.toList()

    fun decode(point: Point, pointGroupValue: Int): Option<Point> =
      if (characters[pointGroupValue] == LightPointSymbol)
        Some(point)
      else
        None

    val isFirstLight = characters.first() == LightPointSymbol
    val isLastLight = characters.last() == LightPointSymbol

    override fun toString(): String =
      key

    companion object {
      const val LightPointSymbol = '#'
      const val DarkPointSymbol = '.'
    }
  }

  data class Plane(val lightPoints: Set<Point>, val isOutsideLight: Boolean) {
    private val xs = lightPoints.map { it.x }
    private val ys= lightPoints.map { it.y }
    private val fromX = xs.minOrNull()!!
    private val toX = xs.maxOrNull()!!
    private val fromY = ys.minOrNull()!!
    private val toY = ys.maxOrNull()!!

    private fun isOutsidePoint(point: Point): Boolean =
      point.x !in (fromX..toX) || point.y !in (fromY..toY)

    private fun isLightPoint(point: Point): Boolean =
      lightPoints.contains(point) || (isOutsidePoint(point) && isOutsideLight)

    private fun computeGroupValue(group: List<Point>): Int {
      val digits = group.map {
        if (isLightPoint(it)) 1
        else 0
      }
      return digits.joinToString("").toInt(2)
    }

    fun enhance(key: Key): Plane {
      val enhancedLightPoints = (fromY - 1..toY + 1).flatMap { y ->
        (fromX - 1..toX + 1).map { x ->
          val point = Point(x, y)
          val pointGroup = point.relatedPointGroup()
          val pointGroupValue = computeGroupValue(pointGroup)
          key.decode(point, pointGroupValue)
        }.flattenOption()
      }.toSet()
      val updatedIsOutsideLight = if (isOutsideLight)
        key.isLastLight
      else
        key.isFirstLight

      return Plane(enhancedLightPoints, isOutsideLight = updatedIsOutsideLight)
    }

    override fun toString(): String =
      (fromY .. toY).map { y ->
        (fromX .. toX).map { x ->
          if (lightPoints.contains(Point(x, y)))
            Key.LightPointSymbol
          else
            Key.DarkPointSymbol
        }.joinToString("")
      }.joinToString("\n")
  }

  fun parseInput(input: String): Pair<Key, Plane> {
    val lines = input.split("\n").map { it.trim() }
    val keyAndPlaneInputs = ParsingUtils.splitByElement(lines, "")
    val key = parseKey(keyAndPlaneInputs[0])
    val plane = parsePlane(keyAndPlaneInputs[1])
    return key to plane
  }

  private fun parseKey(input: List<String>): Key =
    Key(input.joinToString(""))

  private fun parsePlane(input: List<String>): Plane {
    val points = input.indices.flatMap { y ->
      val row = input[y]
      row.indices.map { x ->
        if (row[x] == Key.LightPointSymbol)
          Some(Point(x, y))
        else
          None
      }.flattenOption()
    }.toSet()
    return Plane(points, isOutsideLight = false)
  }

  fun part1(key: Key, initialPlane: Plane): Int {
    val finalPlane = (1..2).fold(initialPlane) { plane, step ->
      println()
      println("step $step")
      println()
      val enhancedPlane = plane.enhance(key)
      println(enhancedPlane)
      println()
      enhancedPlane
    }
    return finalPlane.lightPoints.size
  }
}

fun main() {
  val (key, initialPlane) = Day20.parseInput(Day20.input)
  println(Day20.part1(key, initialPlane))
}