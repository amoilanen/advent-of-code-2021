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

    private fun isLowercaseNode(node: String): Boolean =
      node.lowercase() == node

    private fun isUppercaseNode(node: String): Boolean =
      node.uppercase() == node

    fun findPaths(fromNode: String, toNode: String): Set<List<String>> {
      fun buildNextPathSegment(partialPaths: Set<List<String>>, completedPaths: Set<List<String>>): Set<List<String>> {
        return if (partialPaths.isEmpty()) {
          completedPaths
        } else {
          val newPaths = partialPaths.fold(emptySet<List<String>>()) { currentPaths, partialPath ->
            val lastPathNode = partialPath.first()
            val newNodesToVisit = edges.getOrElse(lastPathNode) {
              emptyList()
            }.filter { newNodeToVisit ->
              isUppercaseNode(newNodeToVisit) ||
                  (isLowercaseNode(newNodeToVisit) && !partialPath.contains(newNodeToVisit))
            }
            val newPaths = newNodesToVisit.map { listOf(it) + partialPath }.toSet()
            currentPaths + newPaths
          }
          val newFinishedPaths = newPaths.filter { it.first() == toNode }.toSet()
          val newPartialPaths = newPaths.filter { it.first() != toNode }.toSet()
          val newCompletedPaths = newFinishedPaths + completedPaths
          if (newPartialPaths != partialPaths) {
            buildNextPathSegment(newPartialPaths, newCompletedPaths)
          } else {
            newCompletedPaths
          }
        }
      }
      return buildNextPathSegment(setOf(listOf(fromNode)), emptySet()).map {
        it.reversed()
      }.toSet()
    }

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

  val foundPaths = graph.findPaths("start", "end")
  println("Found paths: ")
  foundPaths.forEach {
    println(it)
  }
  println("Total number of paths = ${foundPaths.size}")
}