package io.github.antivanov.aoc2021

object Day8 {
  val input = """
    be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
    edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
    fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
    fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
    aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
    fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
    dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
    bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
    egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
    gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
  """.trimIndent()

  fun parseDisplayInput(input: String): Pair<List<String>, List<String>> {
    val lineSides = input.split("|").map { it.trim() }.map {
      it.split("\\s++".toRegex()).map { it.trim() }
    }
    return lineSides[0] to lineSides[1]
  }

  fun parseInput(input: String): List<Pair<List<String>, List<String>>> =
    input.split("\n").map { parseDisplayInput(it) }

  fun realLetterOf(letter: Char, letterFrequencies: Map<Char, Int>, lettersInDigitTwo: List<Char>, lettersInDigitFour: List<Char>): Char =
    when (letterFrequencies[letter]) {
      8 ->
        if (lettersInDigitTwo.contains(letter))
          'c'
        else
          'a'
      7 ->
        if (lettersInDigitFour.contains(letter))
          'd'
        else
          'g'
      6 -> 'b'
      4 -> 'e'
      9 -> 'f'
      else -> '?'
    }

  val sevenSegmentDisplayDigitToInt: Map<Set<Char>, Int> = hashMapOf(
    "abcefg" to 0,
    "cf" to 1,
    "acdeg" to 2,
    "acdfg" to 3,
    "bcdf" to 4,
    "abdfg" to 5,
    "abdefg" to 6,
    "acf" to 7,
    "abcdefg" to 8,
    "abcdfg" to 9
  ).mapKeys { it.key.toList().toSet() }

  fun sevenSegmentDisplayToInt(display: Set<Char>): Int =
    sevenSegmentDisplayDigitToInt.getOrDefault(display, -1)

  fun decipherDisplayReading(displayInput: Pair<List<String>, List<String>>): List<Int> {
    val letterFrequencies = displayInput.first.flatMap {
      it.toList()
    }.groupBy {
      it
    }.mapValues {
      it.value.size
    }
    val lettersInDigitFour = displayInput.first.find { it.length == 4 }!!.toList()
    val lettersInDigitTwo = displayInput.first.find { it.length == 2 }!!.toList()

    val displayReadingInRealLetters = displayInput.second.map { displayDigit ->
      val decodedDigit = displayDigit.toList().map {
        realLetterOf(it, letterFrequencies, lettersInDigitTwo, lettersInDigitFour)
      }.toSet()
      decodedDigit
    }
    val displayReadingAsNumbers = displayReadingInRealLetters.map {
      sevenSegmentDisplayToInt(it)
    }
    return displayReadingAsNumbers
  }

  fun part1(displayInputs: List<Pair<List<String>, List<String>>>): Int {
    val displayedDigits = displayInputs.flatMap { decipherDisplayReading(it) }
    val digitCounts = displayedDigits.groupBy { it }.mapValues { it.value.size }
    return digitCounts[1]!! + digitCounts[4]!! + digitCounts[7]!! + digitCounts[8]!!
  }
}

fun main() {
  val displayInputs = Day8.parseInput(Day8.input)
  println(Day8.part1(displayInputs))
}