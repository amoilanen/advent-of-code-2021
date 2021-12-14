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

  fun <T> mergeCounts(pairs: List<Pair<T, Long>>): Map<T, Long> =
    pairs.groupBy {
      it.first
    }.mapValues { repetitions ->
      repetitions.value.map { pair ->
        pair.second
      }.sum()
    }

  data class Polymer(val pairCounts: Map<String, Long>, val start: Char, val end: Char) {
    fun length(): Long {
      val pairsCount = pairCounts.map { it.value }.sum()
      return pairsCount + 1 // start and end symbol participate in a single pair each -> we did not count one of them
    }

    fun computeElementFrequencies(): Map<Char, Long> {
      val frequencies = mergeCounts(pairCounts.flatMap {
        val pair = it.key
        val frequency = it.value
        pair.toList().map { it to frequency }
      })
      // start and end symbol participate in a single pair each -> we did not count one of them
      val doubledFrequencies = frequencies + (start to (frequencies[start]!! + 1)) +  (end to (frequencies[end]!! + 1))
      return doubledFrequencies.mapValues { it.value / 2 }
    }

    fun update(rules: Rules): Polymer {
      val updatedPairs = mergeCounts(pairCounts.flatMap { pairAndCount ->
        val pair = pairAndCount.key
        val pairCount = pairAndCount.value
        val newPairs = rules.pairToRuleMapping[pair].toOption().toList().map {
          it.toPairs
        }.flatten()
        newPairs.map { it to pairCount }
      })
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
        it.value.size.toLong()
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

  fun updateTimes(initial: Polymer, rules: Rules, steps: Int): Polymer =
    (1..steps).fold(initial) { current, _ ->
      current.update(rules)
    }

  fun differenceMostCommonAndLessCommonFrequencies(polymer: Polymer, rules: Rules, steps: Int): Long {
    val updatedPolymer = updateTimes(polymer, rules, steps)
    val elementFrequencies = updatedPolymer.computeElementFrequencies()

    val frequencies = elementFrequencies.map { it.value }.sorted()
    val smallestFrequency = frequencies.first()
    val largestFrequency = frequencies.last()
    return largestFrequency - smallestFrequency
  }

  fun part1(polymer: Polymer, rules: Rules): Long =
    differenceMostCommonAndLessCommonFrequencies(polymer, rules, 10)

  fun part2(polymer: Polymer, rules: Rules): Long =
    differenceMostCommonAndLessCommonFrequencies(polymer, rules, 40)
}

fun main() {
  val (polymer, rules) = Day14.parseInput(Day14.input)
  println(Day14.part1(polymer, rules))
  println(Day14.part2(polymer, rules))
}