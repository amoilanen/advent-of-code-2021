package io.github.antivanov.aoc2021

// Limitation: Typealiases cannot be defined inside objects in Kotlin unlike in Scala
typealias SubmarinePositionUpdate = (Day2.SubmarinePosition, Day2.MovementInstruction) -> Day2.SubmarinePosition

object Day2 {
  val input = """
forward 5
down 5
forward 8
up 3
down 8
forward 2
""".trimIndent()

  data class SubmarinePosition(val depth: Int, val horizontal: Int, val aim: Int)

  private fun updatePosition(position: SubmarinePosition, instruction: MovementInstruction): SubmarinePosition {
    return when (instruction) {
      is MovementInstruction.Up -> position.copy(depth = position.depth - instruction.value)
      is MovementInstruction.Down -> position.copy(depth = position.depth + instruction.value)
      is MovementInstruction.Forward -> position.copy(horizontal = position.horizontal + instruction.value)
      is MovementInstruction.Unknown -> position
    }
  }

  private fun updatePositionWithAim(position: SubmarinePosition, instruction: MovementInstruction): SubmarinePosition {
    return when (instruction) {
      is MovementInstruction.Up -> position.copy(aim = position.aim - instruction.value)
      is MovementInstruction.Down -> position.copy(aim = position.aim + instruction.value)
      is MovementInstruction.Forward ->
        position.copy(
          horizontal = position.horizontal + instruction.value,
          depth = position.depth + position.aim * instruction.value
        )
      is MovementInstruction.Unknown -> position
    }
  }

  sealed class MovementInstruction(open val value: Int) {
    data class Up(override val value: Int): MovementInstruction(value)
    data class Down(override val value: Int): MovementInstruction(value)
    data class Forward(override val value: Int): MovementInstruction(value)
    object Unknown: MovementInstruction(0)
  }

  private val instructionRegex = "([a-z]+) (\\d+)".toRegex()

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

  private fun computeUpdatedPosition(initialPosition: SubmarinePosition, instructions: List<MovementInstruction>, positionUpdate: SubmarinePositionUpdate): SubmarinePosition {
    return instructions.fold(
      initialPosition
    ) { position, instruction ->
      positionUpdate(position, instruction)
    }
  }

  private fun computeFinalPositionHash(instructions: List<MovementInstruction>, positionUpdate: SubmarinePositionUpdate): Int {
    val initialPosition = SubmarinePosition(0, 0, 0)
    val updatedPosition = computeUpdatedPosition(initialPosition, instructions, positionUpdate)
    return updatedPosition.depth * updatedPosition.horizontal
  }

  fun part1(instructions: List<MovementInstruction>): Int {
    return computeFinalPositionHash(instructions, ::updatePosition)
  }

  fun part2(instructions: List<MovementInstruction>): Int {
    return computeFinalPositionHash(instructions, ::updatePositionWithAim)
  }
}

fun main() {
  val instructions = Day2.parseInstructions(Day2.input.split("\n"))
  println(Day2.part1(instructions))
  println(Day2.part2(instructions))
}