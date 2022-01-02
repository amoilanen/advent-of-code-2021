package io.github.antivanov.aoc2021

import arrow.core.None
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

  data class Point(val x: Int, val y: Int)

  data class Key(val key: String) {

    private val characters = key.toList()

    fun decode(value: Int): Char =
      characters[value]

    override fun toString(): String =
      key
  }

  data class Plane(val pixels: Set<Point>) {
    private val xs = pixels.map { it.x }
    private val ys= pixels.map { it.y }
    private val fromX = xs.minOrNull()!!
    private val toX = xs.maxOrNull()!!
    private val fromY = ys.minOrNull()!!
    private val toY = ys.maxOrNull()!!

    override fun toString(): String =
      (fromY .. toY).map { y ->
        (fromX .. toX).map { x ->
          if (pixels.contains(Point(x, y)))
            '#'
          else
            '.'
        }.joinToString("")
      }.joinToString("\n")
  }

  fun parse(input: String): Pair<Key, Plane> {
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
        if (row[x] == '#')
          Some(Point(x, y))
        else
          None
      }.flattenOption()
    }.toSet()
    return Plane(points)
  }
}

fun main() {
  val (key, initialPlane) = Day20.parse(Day20.input)
  println(key)
  println(initialPlane)
}