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

  private fun isStartNode(node: String): Boolean =
    node == "start"

  private fun isEndNode(node: String): Boolean =
    node == "end"

  private fun isStartOrEndNode(node: String): Boolean =
    isStartNode(node) || isEndNode(node)

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

    fun countPathsDepthFirst(fromNode: String, toNode: String, isEligiblePathContinuation: (Path, String) -> Boolean): Int {
      fun find(pathsToExplore: Set<Path>, toNode: String, currentCount: Int, isEligiblePathContinuation: (Path, String) -> Boolean): Int {
        return if (pathsToExplore.isEmpty())
          currentCount
        else {
          val fromPath = pathsToExplore.first()
          val restOfPathsToExplore = pathsToExplore.filter { it != fromPath }
          val updatedCurrentCount = if (fromPath.lastPathNode == toNode)
            currentCount + 1
          else
            currentCount
          val nextPathsFromPath = edges.getOrElse(fromPath.lastPathNode) {
            emptyList()
          }.filter {
            isEligiblePathContinuation(fromPath, it)
          }.map {
            fromPath.appendNode(it)
          }.toSet()
          find(nextPathsFromPath + restOfPathsToExplore, toNode, updatedCurrentCount, isEligiblePathContinuation)
        }
      }
      return find(setOf(Path(listOf(fromNode))), toNode, 0, isEligiblePathContinuation)
    }

    // ?! - Helping Kotlin compiler optimize tail recursion - otherwise it does not do it for some reason, normally a compiler should do it
    fun countPathsDepthFirstTailRecursionOptimized(fromNode: String, toNode: String, isEligiblePathContinuation: (Path, String) -> Boolean): Int {
      var pathsToExplore = setOf(Path(listOf(fromNode)))
      var currentCount = 0
      while (!pathsToExplore.isEmpty()) {
        val fromPath = pathsToExplore.first()
        val restOfPathsToExplore = pathsToExplore.filter { it != fromPath }
        currentCount = if (fromPath.lastPathNode == toNode)
          currentCount + 1
        else
          currentCount
        val nextPathsFromPath = edges.getOrElse(fromPath.lastPathNode) {
          emptyList()
        }.filter {
          isEligiblePathContinuation(fromPath, it)
        }.map {
          fromPath.appendNode(it)
        }.toSet()
        pathsToExplore = nextPathsFromPath + restOfPathsToExplore
      }
      return currentCount
    }

    /*
     * Original and less efficient algorithm - much more data shifted around between procedure call stacks (all the paths being built),
     * depth-first solution is much more efficient as the the number of constructed paths held in memory at any moment is much smaller.
     */
    fun findPaths(fromNode: String, toNode: String, isEligiblePathContinuation: (Path, String) -> Boolean): Set<Path> {
      fun buildNextPathSegment(partialPaths: Set<Path>, completedPaths: Set<Path>): Set<Path> {
        return if (partialPaths.isEmpty()) {
          completedPaths
        } else {
          val nextNodes = partialPaths.fold(emptySet<Path>()) { currentPaths, partialPath ->
            val newNodesToVisit = edges.getOrElse(partialPath.lastPathNode) {
              emptyList()
            }.filter {
              isEligiblePathContinuation(partialPath, it)
            }
            val newPaths = newNodesToVisit.map { partialPath.appendNode(it) }.toSet()
            currentPaths + newPaths
          }
          val newFinishedPaths = nextNodes.filter { it.lastPathNode == toNode }.toSet()
          val newPartialPaths = nextNodes.filter { it.lastPathNode != toNode }.toSet()
          val newCompletedPaths = newFinishedPaths + completedPaths
          if (newPartialPaths != partialPaths) {
            buildNextPathSegment(newPartialPaths, newCompletedPaths)
          } else {
            newCompletedPaths
          }
        }
      }
      return buildNextPathSegment(setOf(Path(listOf(fromNode))), emptySet()).map {
        Path(it.nodes.reversed())
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
    graph.countPathsDepthFirstTailRecursionOptimized("start", "end", ::everyLowercaseNodeOnlyOnce)

  fun part2(graph: Graph): Int  =
    graph.countPathsDepthFirstTailRecursionOptimized("start", "end", ::oneLowercaseNodeMightBeTwice)
}

fun main() {
  val graph = Day12.parseInput(Day12.input)
  println(graph)
  println(Day12.part1(graph))
  println(Day12.part2(graph))
}