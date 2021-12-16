package io.github.antivanov.aoc2021

object Day16 {

  open class Packet(version: Int, type: Int)

  data class Literal(val version: Int, val value: Int): Packet(version, Literal.Type) {
    companion object {
      val Type = 4
    }
  }
  data class Operator(val version: Int, val type: Int, val packets: List<Packet>): Packet(version, type)

  fun parsePacket(input: String, pointer: Int = 0): Pair<Packet, Int> {
    val value = input.toInt(16)
    val binaryRepresentation = value.toString(2).toList()
    val version = binaryRepresentation.subList(pointer, 3).joinToString("").toInt(2)
    val type = binaryRepresentation.subList(pointer + 3, 6).joinToString("").toInt(2)

    return if (type == Literal.Type) {
      val numberInput = binaryRepresentation.subList(pointer + 6, binaryRepresentation.size)
      val numberEnd = (0 until numberInput.size).find { idx ->
       (idx % 5 == 0) and (numberInput[idx] == '0')
      }!!
      val numberBitIndices = (0 until numberEnd + 5).filter { it % 5 > 0 }
      val numberBits = numberBitIndices.map {
       numberInput[it]
      }
      println(numberBits)
      val number = numberBits.joinToString("").toInt(2)
      val literalLengthWithoutPadding = 6 + numberEnd + 5
      val literalPaddingLength = (4 - (literalLengthWithoutPadding % 4)) % 4
      val fullLiteralLength = literalLengthWithoutPadding + literalPaddingLength

      val updatedPointer = pointer + fullLiteralLength
      Literal(version, number) to updatedPointer
    } else {
      //TODO: Implement parsing Operator
      val updatedPointer = 1
      Literal(0, -1) to updatedPointer
    }
  }
}

fun main() {
  val input = "D2FE28"
  println(Day16.parsePacket(input))
}