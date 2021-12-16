package io.github.antivanov.aoc2021

object Day16 {

  open class Packet(version: Int, type: Int)

  data class Literal(val version: Int, val value: Int): Packet(version, Literal.Type) {
    companion object {
      val Type = 4
      val NumberBitsGroupLength = 5
    }
  }
  data class Operator(val version: Int, val type: Int, val packets: List<Packet>): Packet(version, type)

  private val SystemBitsPadding = 6
  val BitsBlockLength = 4

  private fun bitsToInt(bits: List<Char>): Int =
    bits.joinToString("").toInt(2)

  private fun paddingLengthToFinishBlock(length: Int, blockLength: Int): Int =
    (blockLength - (length % blockLength)) % blockLength

  fun parse(input: String, pointer: Int = 0): Pair<Packet, Int> {
    val value = input.toInt(16)
    val binaryRepresentation = value.toString(2).toList()
    return parsePacket(binaryRepresentation, pointer)
  }

  fun parsePacket(input: List<Char>, pointer: Int): Pair<Packet, Int> {
    val version = bitsToInt(input.subList(pointer, SystemBitsPadding / 2))
    val type = bitsToInt(input.subList(pointer + SystemBitsPadding / 2, SystemBitsPadding))

    return if (type == Literal.Type) {
      return parseLiteral(input, version, pointer + SystemBitsPadding)
    } else {
      //TODO: Implement parsing Operator
      val updatedPointer = 1
      Literal(0, -1) to updatedPointer
    }
  }

  fun parseLiteral(input: List<Char>, version: Int, pointer: Int): Pair<Packet, Int> {
    val inputAfterPointer = input.subList(pointer, input.size)
    val numberEnd = (0 until inputAfterPointer.size).find { idx ->
      (idx % Literal.NumberBitsGroupLength == 0) and (inputAfterPointer[idx] == '0')
    }!!
    val literalLengthWithoutPadding = numberEnd + Literal.NumberBitsGroupLength

    val numberBitIndices = (0 until literalLengthWithoutPadding).filter { it % Literal.NumberBitsGroupLength > 0 }
    val encodedNumber = bitsToInt(numberBitIndices.map {
      inputAfterPointer[it]
    })
    val paddingLength = paddingLengthToFinishBlock(SystemBitsPadding + literalLengthWithoutPadding, BitsBlockLength)
    val fullLiteralLength = literalLengthWithoutPadding + paddingLength

    val updatedPointer = pointer + fullLiteralLength
    return Literal(version, encodedNumber) to updatedPointer
  }
}

fun main() {
  val input = "D2FE28"
  println(Day16.parse(input))
}