package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Some
import arrow.core.flattenOption

object Day9 {

  val input = """
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
  """.trimIndent()

  fun parseInput(input: String): List<List<Int>> =
    input.split("\n").map {
      it.trim()
    }.map {
      it.toList().map { digit ->
        digit.toString().toInt()
      }
    }

  data class Point(val x: Int, val y: Int)

  data class Locations(val points: List<List<Int>>) {
    val width = points[0].size
    val height = points.size

    fun getHeightAt(p: Point): Int =
      points[p.y][p.x]

    private fun getAdjacentPoints(p: Point): List<Point> {
      val top = if (p.y > 0) Some(p.copy(y = p.y - 1)) else None
      val bottom = if (p.y < height - 1) Some(p.copy(y = p.y + 1)) else None
      val left = if (p.x > 0) Some(p.copy(x = p.x - 1)) else None
      val right = if (p.x < width - 1) Some(p.copy(x = p.x + 1)) else None
      return listOf(top, bottom, left, right).flattenOption()
    }

    fun getAllPoints(): List<Point> =
      (0 until width).flatMap { x ->
        (0 until height).map { y ->
          Point(x, y)
        }
      }

    fun isLowPoint(p: Point): Boolean {
      val adjacentPoints = getAdjacentPoints(p)
      val pointHeight = getHeightAt(p)
      val adjacentHeights = adjacentPoints.map { getHeightAt(it) }
      return adjacentHeights.all { pointHeight < it}
    }
  }

  fun getLowPoints(locations: Locations): List<Point> =
    locations.getAllPoints().filter {
      locations.isLowPoint(it)
    }

  fun part1(input: List<List<Int>>): Int {
    val locations = Locations(input)
    val lowPoints = getLowPoints(locations)
    val lowPointHeights = lowPoints.map {
      locations.getHeightAt(it)
    }
    return lowPointHeights.map {
      it + 1
    }.sum()
  }

  fun basinPointsOnLinesFrom(locations: Locations, p: Point): Set<Point> {
    val basinPointToLeft = (0..p.x).map {
      Point(it, p.y)
    }.reversed().takeWhile {
      locations.getHeightAt(it) < 9
    }
    val basinPointToRight = (p.x until locations.width).map {
      Point(it, p.y)
    }.takeWhile {
      locations.getHeightAt(it) < 9
    }
    val basinPointToTop = (0..p.y).map {
      Point(p.x, it)
    }.reversed().takeWhile {
      locations.getHeightAt(it) < 9
    }
    val basinPointToBottom = (p.y until locations.height).map {
      Point(p.x, it)
    }.takeWhile {
      locations.getHeightAt(it) < 9
    }
    return (basinPointToLeft + basinPointToRight + basinPointToTop + basinPointToBottom).toSet()
  }

  fun basinPointsFrom(locations: Locations, p: Point): Set<Point> {
    var currentBasinPoints = emptySet<Point>()
    var newBasinPoints = listOf(p).toSet()
    while (newBasinPoints.isNotEmpty()) {
      currentBasinPoints += newBasinPoints
      newBasinPoints = newBasinPoints.flatMap {
        basinPointsOnLinesFrom(locations, it).filter {
          !currentBasinPoints.contains(it)
        }
      }.toSet()
    }
    return currentBasinPoints
  }
}

fun main() {
  val parsed = Day9.parseInput(Day9.input)
  println(Day9.part1(parsed))

  val locations = Day9.Locations(parsed)
  val lowPoint = Day9.Point(6, 4)
  val basinPart = Day9.basinPointsOnLinesFrom(locations, lowPoint)
  println(basinPart)
  val basin = Day9.basinPointsFrom(locations, lowPoint)
  println(basin)
  println(basin.size)
}