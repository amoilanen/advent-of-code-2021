package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Some
import arrow.core.toOption

object Day22 {

  val input = """
    on x=10..12,y=10..12,z=10..12
    on x=11..13,y=11..13,z=11..13
    off x=9..11,y=9..11,z=9..11
    on x=10..10,y=10..10,z=10..10
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

  data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange)

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
    return steps.reversed()
  }

  fun isOn(point: Point, rebootSteps: List<RebootStep>): Boolean {
    val lastStep = rebootSteps.find { it.isAffecting(point) }.toOption()
    return when (lastStep) {
      is Some -> lastStep.value.action == StepAction.ON
      is None -> false
    }
  }

  fun countPointsBeingOn(points: Cube, rebootSteps: List<RebootStep>): Int =
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

  fun part1(rebootSteps: List<RebootStep>): Int {
    val cubeDimension = -50..50
    return countPointsBeingOn(Cube(cubeDimension, cubeDimension, cubeDimension), rebootSteps)
  }
}

fun main() {
  val steps = Day22.parseInput(Day22.input)
  println(steps)
  println(Day22.part1(steps))
}