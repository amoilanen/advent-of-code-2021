package io.github.antivanov.aoc2021

import arrow.core.getOrElse
import arrow.core.toOption

object Day16 {
  val input = "820D4100A1000085C6E8331F8401D8E106E1680021708630C50200A3BC01495B99CF6852726A88014DC9DBB30798409BBDF5A4D97F5326F050C02F9D2A971D9B539E0C93323004B4012960E9A5B98600005DA7F11AFBB55D96AFFBE1E20041A64A24D80C01E9D298AF0E22A98027800BD4EE3782C91399FA92901936E0060016B82007B0143C2005280146005300F7840385380146006900A72802469007B0001961801E60053002B2400564FFCE25FEFE40266CA79128037500042626C578CE00085C718BD1F08023396BA46001BF3C870C58039587F3DE52929DFD9F07C9731CC601D803779CCC882767E668DB255D154F553C804A0A00DD40010B87D0D6378002191BE11C6A914F1007C8010F8B1122239803B04A0946630062234308D44016CCEEA449600AC9844A733D3C700627EA391EE76F9B4B5DA649480357D005E622493112292D6F1DF60665EDADD212CF8E1003C29193E4E21C9CF507B910991E5A171D50092621B279D96F572A94911C1D200FA68024596EFA517696EFA51729C9FB6C64019250034F3F69DD165A8E33F7F919802FE009880331F215C4A1007A20C668712B685900804ABC00D50401C89715A3B00021516E164409CE39380272B0E14CB1D9004632E75C00834DB64DB4980292D3918D0034F3D90C958EECD8400414A11900403307534B524093EBCA00BCCD1B26AA52000FB4B6C62771CDF668E200CC20949D8AE2790051133B2ED005E2CC953FE1C3004EC0139ED46DBB9AC9C2655038C01399D59A3801F79EADAD878969D8318008491375003A324C5A59C7D68402E9B65994391A6BCC73A5F2FEABD8804322D90B25F3F4088F33E96D74C0139CF6006C0159BEF8EA6FBE3A9CEC337B859802B2AC9A0084C9DCC9ECD67DD793004E669FA2DE006EC00085C558C5134001088E308A20"

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

  abstract class Packet(version: Int, type: Int) {
    abstract fun evaluate(): Long
  }

  data class Literal(val version: Int, val value: Long): Packet(version, Type) {
    companion object {
      val Type = 4
      val NumberBitsGroupLength = 5
    }

    override fun evaluate(): Long =
      value
  }
  data class Operator(val version: Int, val type: Int, val packets: List<Packet>): Packet(version, type) {

    override fun evaluate(): Long =
      when (type) {
        0 ->
          packets.fold(0) { acc, it ->
            acc + it.evaluate()
          }
        1 ->
          packets.fold(1) { acc, it ->
            acc * it.evaluate()
          }
        2 ->
          packets.map { it.evaluate() }.minOrNull()!!
        3 ->
          packets.map { it.evaluate() }.maxOrNull()!!
        5 -> {
          if (packets.size != 2) {
            throw IllegalStateException("'greater than' can only have two subpackets but had ${packets.size}")
          }
          if (packets[0].evaluate() > packets[1].evaluate())
            1
          else
            0
        }
        6 -> {
          if (packets.size != 2) {
            throw IllegalStateException("'less than' can only have two subpackets but had ${packets.size}")
          }
          if (packets[0].evaluate() < packets[1].evaluate())
            1
          else
            0
        }
        7 -> {
          if (packets.size != 2) {
            throw IllegalStateException("'equal to' can only have two subpackets but had ${packets.size}")
          }
          if (packets[0].evaluate() == packets[1].evaluate())
            1
          else
            0
        }
        else -> throw IllegalStateException("Unknown operator type $type")
      }
  }

  private val SystemBitsPadding = 6

  private fun bitsToInt(bits: List<Char>): Int =
    bits.joinToString("").toInt(2)

  private fun bitsToLong(bits: List<Char>): Long =
    bits.joinToString("").toLong(2)

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
    val encodedNumber = bitsToLong(numberBitIndices.map {
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

  fun part2(input: String): Long {
    val (packet, _) = parse(input)
    return packet.evaluate()
  }
}

fun main() {
  println(Day16.part1(Day16.input))
  println(Day16.part2(Day16.input))
}