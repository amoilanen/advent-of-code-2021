package io.github.antivanov.aoc2021

object Day12 {
  val input = """
fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
  """.trimIndent()

  private fun isLowercaseNode(node: String): Boolean =
    node.lowercase() == node

  private fun isUppercaseNode(node: String): Boolean =
    node.uppercase() == node

  private fun isStartOrEndNode(node: String): Boolean =
    node == "start" || node == "end"

  fun hasLowercaseNodeAtLeastTwice(nodes: List<String>): Boolean =
    nodes.filter {
      isLowercaseNode(it)
    }.groupBy {
      it
    }.any {
      it.value.size >= 2
    }

  data class Path(val nodes: List<String>, val alreadyHasLowercaseNodeAtLeastTwice: Boolean = false) {

    val lastPathNode: String = nodes.first()

    fun appendNode(node: String): Path {
      val newNodes = listOf(node) + nodes
      return Path(newNodes, alreadyHasLowercaseNodeAtLeastTwice || hasLowercaseNodeAtLeastTwice(newNodes))
    }

  }

  data class Graph(val edges: Map<String, List<String>>) {

    fun findPaths(fromNode: String, toNode: String, isEligiblePathContinuation: (Path, String) -> Boolean): Set<List<String>> {
      fun buildNextPathSegment(partialPaths: Set<Path>, completedPaths: Set<Path>): Set<Path> {
        return if (partialPaths.isEmpty()) {
          completedPaths
        } else {
          val newPaths = partialPaths.fold(emptySet<Path>()) { currentPaths, partialPath ->
            val newNodesToVisit = edges.getOrElse(partialPath.lastPathNode) {
              emptyList()
            }.filter {
              isEligiblePathContinuation(partialPath, it)
            }
            val newPaths = newNodesToVisit.map { partialPath.appendNode(it) }.toSet()
            currentPaths + newPaths
          }
          val newFinishedPaths = newPaths.filter { it.lastPathNode == toNode }.toSet()
          val newPartialPaths = newPaths.filter { it.lastPathNode != toNode }.toSet()
          val newCompletedPaths = newFinishedPaths + completedPaths
          if (newPartialPaths != partialPaths) {
            buildNextPathSegment(newPartialPaths, newCompletedPaths)
          } else {
            newCompletedPaths
          }
        }
      }
      return buildNextPathSegment(setOf(Path(listOf(fromNode))), emptySet()).map {
        it.nodes.reversed()
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

  private fun everyLowercaseNodeOnlyOnce(path: Path, continuation: String): Boolean =
    isUppercaseNode(continuation) ||
        (isLowercaseNode(continuation) && !path.nodes.contains(continuation))

  private fun oneLowercaseNodeMightBeTwice(path: Path, continuation: String): Boolean =
    isUppercaseNode(continuation) ||
    (
      isLowercaseNode(continuation) &&
      (
        !path.nodes.contains(continuation) ||
        (
          !path.alreadyHasLowercaseNodeAtLeastTwice &&
          !isStartOrEndNode(continuation)
        )
      )
    )

  fun part1(graph: Graph): Int  =
    graph.findPaths("start", "end", ::everyLowercaseNodeOnlyOnce).size

  fun part2(graph: Graph): Int  =
    graph.findPaths("start", "end", ::oneLowercaseNodeMightBeTwice).size
}

fun main() {
  val graph = Day12.parseInput(Day12.input)
  println(graph)
  println(Day12.part1(graph))
  println(Day12.part2(graph))
}