package io.github.antivanov.aoc2021

object Day16 {
  val input = "A0016C880162017C3686B18A3D4780"

  val bitEncodingRules: Map<Char, List<Char>> = hashMapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
  ).mapValues { it.value.toList() }

  open class Packet(version: Int, type: Int)

  data class Literal(val version: Int, val value: Int): Packet(version, Type) {
    companion object {
      val Type = 4
      val NumberBitsGroupLength = 5
    }
  }
  data class Operator(val version: Int, val type: Int, val packets: List<Packet>): Packet(version, type)

  private val SystemBitsPadding = 6

  private fun bitsToInt(bits: List<Char>): Int =
    bits.joinToString("").toInt(2)

  private fun isPaddedByZeroesFrom(input: List<Char>, pointer: Int): Boolean {
    val remainingBits = input.subList(pointer, input.size)
    return remainingBits.all {
      it == '0'
    }
  }

  fun parse(input: String, pointer: Int = 0): Pair<Packet, Int> {
    val binaryRepresentation = input.flatMap { bitEncodingRules[it]!! }
    val (topLevelPacket, pointer) =  parsePacket(binaryRepresentation, pointer)
    if (!isPaddedByZeroesFrom(binaryRepresentation, pointer))
      throw IllegalStateException("Non-zero padding bits left after pointer $pointer")
    return topLevelPacket to binaryRepresentation.size
  }

  fun parsePacket(input: List<Char>, pointer: Int): Pair<Packet, Int> {
    val version = bitsToInt(input.subList(pointer, pointer + SystemBitsPadding / 2))
    val type = bitsToInt(input.subList(pointer + SystemBitsPadding / 2, pointer + SystemBitsPadding))

    return if (type == Literal.Type) {
      parseLiteral(input, version, pointer + SystemBitsPadding)
    } else {
      parseOperator(input, version, type, pointer + SystemBitsPadding)
    }
  }

  fun parseOperator(input: List<Char>, version: Int, type: Int, pointer: Int): Pair<Packet, Int> {
    val lengthTypeId = input[pointer]
    return if (lengthTypeId == '0') {
      val bitLengthOfSubpackets = bitsToInt(input.subList(pointer + 1, pointer + 16))
      val readUntilPointer = pointer + 16 + bitLengthOfSubpackets
      var nextPointer = pointer + 16
      var childPackets = emptyList<Packet>()
      while (nextPointer < readUntilPointer) {
        val (childPacket, updatedPointer) = parsePacket(input, nextPointer)
        nextPointer = updatedPointer
        childPackets = childPackets + childPacket
      }
      Operator(version, type, childPackets) to nextPointer
    } else {
      val totalNumberOfDirectChildPackets = bitsToInt(input.subList(pointer + 1, pointer + 12))
      var nextPointer = pointer + 12
      var childPackets = emptyList<Packet>()
      while (childPackets.size < totalNumberOfDirectChildPackets) {
        val (childPacket, updatedPointer) = parsePacket(input, nextPointer)
        nextPointer = updatedPointer
        childPackets = childPackets + childPacket
      }
      Operator(version, type, childPackets) to nextPointer
    }
  }

  fun parseLiteral(input: List<Char>, version: Int, pointer: Int): Pair<Packet, Int> {
    val inputAfterPointer = input.subList(pointer, input.size)
    val numberEnd = (0 until inputAfterPointer.size).find { idx ->
      (idx % Literal.NumberBitsGroupLength == 0) and (inputAfterPointer[idx] == '0')
    }!!
    val literalLength = numberEnd + Literal.NumberBitsGroupLength

    val numberBitIndices = (0 until literalLength).filter { it % Literal.NumberBitsGroupLength > 0 }
    val encodedNumber = bitsToInt(numberBitIndices.map {
      inputAfterPointer[it]
    })

    return Literal(version, encodedNumber) to pointer + literalLength
  }

  fun versionNumberSumOf(packet: Packet): Int =
    when (packet) {
      is Literal -> packet.version
      is Operator ->
        packet.version + packet.packets.map(::versionNumberSumOf).sum()
      else -> 0
    }

  fun part1(input: String): Int {
    val (packet, _) = parse(input)
    return versionNumberSumOf(packet)
  }
}

fun main() {
  println(Day16.part1(Day16.input))
}