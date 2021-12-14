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
}

fun main() {
  val (polymer, rules) = Day14.parseInput(Day14.input)
  println(polymer)
  println(polymer.length())
  println(rules)
  println()

  val updatedPolymer = polymer.update(rules)
  println(updatedPolymer)
}