package io.github.antivanov.aoc2021

object Day2 {
  val input = """
forward 5
down 5
forward 8
up 3
down 8
forward 2
""".trimIndent()

  data class SubmarinePosition(val depth: Int, val horizontal: Int) {

    fun update(instruction: MovementInstruction): SubmarinePosition {
      return when (instruction) {
        is MovementInstruction.Up -> this.copy(depth = this.depth - instruction.length)
        is MovementInstruction.Down -> this.copy(depth = this.depth + instruction.length)
        is MovementInstruction.Forward -> this.copy(horizontal = this.horizontal + instruction.length)
        is MovementInstruction.Unknown -> this
      }
    }
  }

  sealed class MovementInstruction(open val length: Int) {
    data class Up(override val length: Int): MovementInstruction(length)
    data class Down(override val length: Int): MovementInstruction(length)
    data class Forward(override val length: Int): MovementInstruction(length)
    object Unknown: MovementInstruction(0)
  }

  val instructionRegex = "([a-z]+) (\\d+)".toRegex()

  fun parseInstructions(instructions: List<String>): List<MovementInstruction> {
    return instructions.map {
      instructionRegex.matchEntire(it)?.destructured?.let { (instruction, length) ->
        instruction to length.toInt()
      }
    }.filterNotNull().map {
      when (it.first) {
        "forward" -> MovementInstruction.Forward(it.second)
        "up" -> MovementInstruction.Up(it.second)
        "down" -> MovementInstruction.Down(it.second)
        else -> MovementInstruction.Unknown
      }
    }
  }

  fun computeUpdatedPosition(initialPosition: SubmarinePosition, instructions: List<MovementInstruction>): SubmarinePosition {
    return instructions.fold(
      initialPosition
    ) { position, instruction ->
      position.update(instruction)
    }
  }

  fun part1(instructions: List<MovementInstruction>): Int {
    val initialPosition = SubmarinePosition(0, 0)
    val updatedPosition = computeUpdatedPosition(initialPosition, instructions)
    return updatedPosition.depth * updatedPosition.horizontal
  }
}

fun main() {
  val instructions = Day2.parseInstructions(Day2.input.split("\n"))
  println(Day2.part1(instructions))
}