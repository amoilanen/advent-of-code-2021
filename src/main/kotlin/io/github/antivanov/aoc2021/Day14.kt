package io.github.antivanov.aoc2021

import arrow.core.flatten
import arrow.core.toOption

object Day14 {

  val input = """
    NNCB

    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
  """.trimIndent()

  data class Rule(val fromPair: String, val toPairs: List<String>)

  data class Rules(val pairToRuleMapping: Map<String, Rule>)

  data class Polymer(val pairCounts: Map<String, Int>, val start: Char, val end: Char) {
    fun length(): Int {
      val pairsCount = pairCounts.map { it.value }.sum()
      return pairsCount + 1 // start and end symbol participate in a single pair each -> we did not count one of them
    }

    fun computeElementFrequencies(): Map<Char, Int> {
      val frequencies = pairCounts.flatMap {
        val pair = it.key
        val frequency = it.value
        pair.toList().map { it to frequency }
      }.groupBy {
        it.first
      }.mapValues { sameSymbolRepetitions ->
        sameSymbolRepetitions.value.map { pair ->
          pair.second
        }.sum()
      }
      // start and end symbol participate in a single pair each -> we did not count one of them
      val doubledFrequencies = frequencies + (start to (frequencies[start]!! + 1)) +  (end to (frequencies[end]!! + 1))
      return doubledFrequencies.mapValues { it.value / 2 }
    }

    fun update(rules: Rules): Polymer {
      val updatedPairsNotNormalized = pairCounts.flatMap {
        val pair = it.key
        val pairCount = it.value
        val newPairs = rules.pairToRuleMapping[pair].toOption().toList().map { it.toPairs }.flatten()
        newPairs.map { it to pairCount }
      }
      val updatedPairs = updatedPairsNotNormalized.groupBy {
        it.first
      }.mapValues { samePairRepetitions ->
        samePairRepetitions.value.map { pair ->
          pair.second
        }.sum()
      }
      return Polymer(updatedPairs, start, end)
    }
  }

  fun parseInput(input: String): Pair<Polymer, Rules> {
    val lines = input.split("\n").map { it.trim() }
    val polymer = parsePolymer(lines[0])
    val rules = parseRules(lines.drop(2))
    return polymer to rules
  }

  fun parsePolymer(polymerInput: String): Polymer {
    val pairs = polymerInput.toList().zipWithNext().map {
      it.toList().joinToString("")
    }
    val pairCounts = pairs.groupBy { it }
      .mapValues {
        it.value.size
      }
    return Polymer(pairCounts, polymerInput.first(), polymerInput.last())
  }

  fun parseRules(rulesInput: List<String>): Rules {
    val rules = rulesInput.map { parseRule(it) }
    val pairToRuleMapping = rules.map { it.fromPair to it }.toMap()
    return Rules(pairToRuleMapping)
  }

  fun parseRule(ruleInput: String): Rule {
    val ruleParts = ruleInput.split("->").map {
      it.trim()
    }
    val fromPair = ruleParts[0]
    val insertSymbol = ruleParts[1][0]
    val fromPairChars = fromPair.toList()
    val toPairs = listOf(fromPairChars[0] to insertSymbol, insertSymbol to fromPairChars[1]).map {
      it.toList().joinToString("")
    }
    return Rule(fromPair, toPairs)
  }

  fun updateTimes(initial: Polymer, rules: Rules, times: Int): Polymer =
    (1..times).fold(initial) { current, _ ->
      current.update(rules)
    }

  fun part1(polymer: Polymer, rules: Rules): Int {
    val updatedPolymer = updateTimes(polymer, rules, 10)
    val elementFrequencies = updatedPolymer.computeElementFrequencies()

    val frequencies = elementFrequencies.map { it.value }.sorted()
    val smallestFrequency = frequencies.first()
    val largestFrequency = frequencies.last()
    return largestFrequency - smallestFrequency
  }
}

fun main() {
  val (polymer, rules) = Day14.parseInput(Day14.input)
  println(polymer)
  println(polymer.length())
  println(rules)
  println()

  val updatedPolymer = polymer.update(rules)
  println(updatedPolymer)

  val updateTimes = listOf(2, 5, 10)
  updateTimes.forEach {
    val updatedPolymer = Day14.updateTimes(polymer, rules, it)
    val lengthOfUpdatedPolymer = updatedPolymer.length()
    val frequencies = updatedPolymer.computeElementFrequencies()
    println("times = ${it}, length = ${lengthOfUpdatedPolymer}")
    println(frequencies)
  }

  println()
  println(Day14.part1(polymer, rules))
}