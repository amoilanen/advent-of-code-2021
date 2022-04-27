package io.github.antivanov.aoc2021

import arrow.core.Option
import arrow.core.none

object Day23 {

  val input = """
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########
  """.trimIndent()

  enum class Amphipod { A, B, C, D }

  data class BurrowState(val hallWay: List<Option<Amphipod>>,
                         val roomA: Pair<Option<Amphipod>, Option<Amphipod>>,
                         val roomB: Pair<Option<Amphipod>, Option<Amphipod>>,
                         val roomC: Pair<Option<Amphipod>, Option<Amphipod>>,
                         val roomD: Pair<Option<Amphipod>, Option<Amphipod>>) {

    fun isFinal(): Boolean =
      //TODO: Implement
      false
  }

  fun parseInput(input: String): BurrowState {
    //TODO: Implement
    val emptyRoom = none<Amphipod>() to none<Amphipod>()
    return BurrowState(emptyList(), emptyRoom, emptyRoom, emptyRoom, emptyRoom)
  }
}

fun main() {
  val initialState = Day23.parseInput(Day23.input)
  println(initialState)
}