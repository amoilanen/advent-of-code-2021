package io.github.antivanov.aoc2021

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
  data class Polymer(val pairCounts: Map<String, Int>, val start: Char, val end: Char)

  fun parseInput(input: String): Pair<Polymer, List<Rule>> {
    val lines = input.split("\n").map { it.trim() }
    val polymerInput = lines[0]
    val rulesInput = lines.drop(2)
    val input = parsePolymer(polymerInput)
    val rules = rulesInput.map { parseRule(it) }
    return input to rules
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
  println(rules)
}