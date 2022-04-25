package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.toOption
import io.github.antivanov.aoc2021.util.ListUtils.pairsOf

object Day22 {

  val input = """
on x=-20..26,y=-36..17,z=-47..7
on x=-20..33,y=-21..23,z=-26..28
on x=-22..28,y=-29..23,z=-38..16
on x=-46..7,y=-6..46,z=-50..-1
on x=-49..1,y=-3..46,z=-24..28
on x=2..47,y=-22..22,z=-23..27
on x=-27..23,y=-28..26,z=-21..29
on x=-39..5,y=-6..47,z=-3..44
on x=-30..21,y=-8..43,z=-13..34
on x=-22..26,y=-27..20,z=-29..19
off x=-48..-32,y=26..41,z=-47..-37
on x=-12..35,y=6..50,z=-50..-2
off x=-48..-32,y=-32..-16,z=-15..-5
on x=-18..26,y=-33..15,z=-7..46
off x=-40..-22,y=-38..-28,z=23..41
on x=-16..35,y=-41..10,z=-47..6
off x=-32..-23,y=11..30,z=-14..3
on x=-49..-5,y=-3..45,z=-29..18
off x=18..30,y=-20..-8,z=-3..13
on x=-41..9,y=-7..43,z=-33..15
on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
on x=967..23432,y=45373..81175,z=27513..53682
  """.trimIndent()

  enum class StepAction {
    ON,
    OFF;
    companion object {
      fun parse(input: String): StepAction =
        if (input == "on")
          ON
        else
          OFF
    }
  }

  data class Point(val x: Int, val y: Int, val z: Int)

  data class Segment(val values: IntRange) {
    fun intersectWith(other: Segment): Segment =
      if (values.last < other.values.first || other.values.last < values.first)
        Segment(IntRange.EMPTY)
      else {
        val (_, start, end, _) = listOf(values.first, values.last, other.values.first, other.values.last).sorted()
        Segment(start..end)
      }
  }

  data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange) {
    val middlePoint: Point = Point((xs.first + xs.last) / 2, (ys.first + ys.last) / 2, (zs.first + zs.last) / 2)
    val pointCount: Long = (xs.last - xs.first + 1).toLong() * (ys.last - ys.first + 1) * (zs.last - zs.first + 1)

    fun intersectWith(other: Cube): Cube {
      val intersectionXs = Segment(xs).intersectWith(Segment(other.xs)).values
      val intersectionYs = Segment(ys).intersectWith(Segment(other.ys)).values
      val intersectionZs = Segment(zs).intersectWith(Segment(other.zs)).values
      return Cube(intersectionXs, intersectionYs, intersectionZs)
    }

    companion object {
      val EMPTY = Cube(IntRange.EMPTY, IntRange.EMPTY, IntRange.EMPTY)
    }
  }

  data class RebootStep(val action: StepAction, val where: Cube) {
    fun isAffecting(point: Point): Boolean =
      point.x in where.xs && point.y in where.ys && point.z in where.zs
  }

  fun parseInput(input: String): List<RebootStep> {
    val lines = input.split("\n").map { it.trim() }
    val steps = lines.map { line ->
      val (actionInput, cubeInput) = line.split(" ")
      val action = StepAction.parse(actionInput.trim())
      val (xs, ys, zs) = cubeInput.split(",").map {
        val (from, to) = it.drop(2).split("..").map { it.toInt() }
        from..to
      }
      RebootStep(action, Cube(xs, ys, zs))
    }
    return steps
  }

  private fun isOn(point: Point, rebootSteps: List<RebootStep>): Boolean {
    val lastStep = rebootSteps.findLast { it.isAffecting(point) }.toOption()
    return when (lastStep) {
      is Some -> lastStep.value.action == StepAction.ON
      is None -> false
    }
  }

  data class RangeBoundary(val value: Int, val type: RangeBoundaryType) {
    companion object {
      object RangeBoundaryComparator: Comparator<RangeBoundary>{
        private fun directionValue(a: RangeBoundary): Int =
          if (a.type == RangeBoundaryType.LEFT)
            -1
          else
            1

        override fun compare(a: RangeBoundary?, b: RangeBoundary?): Int =
          if (a!!.value == b!!.value)
            directionValue(a!!) - directionValue(b!!)
          else
            a!!.value - b!!.value
      }
    }
  }

  enum class RangeBoundaryType {
    LEFT,
    RIGHT;
  }

  fun countPointsBeingOnInCubeAfterSteps(points: Cube, rebootSteps: List<RebootStep>): Long =
    points.xs.fold(0) { xCount, x ->
      points.ys.fold(xCount) { yCount, y ->
        points.zs.fold(yCount) { zCount, z ->
          val currentPoint = Point(x, y, z)
          if (isOn(currentPoint, rebootSteps))
            zCount + 1
          else
            zCount
        }
      }
    }

  fun sequentialRangesFrom(ranges: List<IntRange>): List<IntRange> {
    val boundaries = ranges.flatMap {
      listOf(
        RangeBoundary(it.first, RangeBoundaryType.LEFT),
        RangeBoundary(it.last, RangeBoundaryType.RIGHT)
      )
    }.toSet().sortedWith(RangeBoundary.Companion.RangeBoundaryComparator)
    val boundariesAfterIntersection = boundaries.flatMap { boundary ->
      if (boundary.type == RangeBoundaryType.LEFT) {
        listOf(RangeBoundary(boundary.value - 1, RangeBoundaryType.RIGHT), boundary)
      } else {
        listOf(boundary, RangeBoundary(boundary.value + 1, RangeBoundaryType.LEFT))
      }
    }.sortedBy { it.value }.toSet().map { it.value }
    val boundariesFirstAndLastBoundariesDropped = boundariesAfterIntersection.drop(1).dropLast(1)
    val ranges = pairsOf(boundariesFirstAndLastBoundariesDropped).map {
      it.first..it.second
    }
    return ranges
  }

  fun countPointsBeingOnAfterSteps(rebootSteps: List<RebootStep>, withinCube: Option<Cube>): Long =
    when (withinCube) {
      is Some ->
        countPointsBeingOnInCubeAfterSteps(withinCube.value, rebootSteps)
      is None -> {
        val xs = sequentialRangesFrom(rebootSteps.map { it.where.xs })
        val ys = sequentialRangesFrom(rebootSteps.map { it.where.ys })
        val zs = sequentialRangesFrom(rebootSteps.map { it.where.zs })
        xs.fold(0) { accX, x ->
          ys.fold(accX) { accY, y ->
            zs.fold(accY) { accZ, z ->
              val cube = Cube(x, y, z)
              if (isOn(cube.middlePoint, rebootSteps))
                accZ + cube.pointCount
              else
                accZ
            }
          }
        }
      }
    }

  fun part1(rebootSteps: List<RebootStep>): Long {
    val coordinateRange = -50..50
    val boundingCube = Cube(coordinateRange, coordinateRange, coordinateRange)
    return countPointsBeingOnAfterSteps(rebootSteps, Some(boundingCube))
  }

  fun part2(rebootSteps: List<RebootStep>): Long {
    return countPointsBeingOnAfterSteps(rebootSteps, None)
  }
}

fun main() {
  val steps = Day22.parseInput(Day22.input)
  println(steps)
  println(Day22.part1(steps))
  println(Day22.part2(steps))
}