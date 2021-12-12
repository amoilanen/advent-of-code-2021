package io.github.antivanov.aoc2021

object Day12 {
  val input = """
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
  """.trimIndent()

  data class Graph(val edges: Map<String, List<String>>) {

    override fun toString(): String =
      edges.map {
        "${it.key} -> ${it.value}"
      }.joinToString("\n")
  }

  fun parseInput(input: String): Graph {
    val lines = input.split("\n").map {
      it.trim()
    }
    val allEdges = lines.flatMap {
      val parts = it.split("-")
      listOf(parts[0] to parts[1], parts[1] to parts[0])
    }
    val groupedEdges = allEdges.groupBy {
      it.first
    }.mapValues {
      it.value.map {
        it.second
      }
    }
    return Graph(groupedEdges)
  }
}

fun main() {
  val graph = Day12.parseInput(Day12.input)
  println(graph)
}