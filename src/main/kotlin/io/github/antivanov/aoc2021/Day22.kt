package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.util.ParsingUtils

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

  data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange)

  data class RebootStep(val action: StepAction, val where: Cube)

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
}

fun main() {
  val steps = Day22.parseInput(Day22.input)
  println(steps)
}