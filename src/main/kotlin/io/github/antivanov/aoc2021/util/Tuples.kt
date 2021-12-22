package io.github.antivanov.aoc2021.util

object Tuples {

  fun <T> tuplesOfSize(size: Int, elements: List<T>): List<List<T>> {
    fun <T> buildPartialTuplesOfSize(size: Int, fromElements: List<T>, partiallyBuiltGroups: List<List<T>>): List<List<T>> {
      return if (size == 0)
        partiallyBuiltGroups
      else {
        val groupsWithDifferentNextElementSelected = fromElements.map { nextElement ->
          val newPartiallyBuiltGroups = partiallyBuiltGroups.map {
            it + nextElement
          }
          val remainingElements = fromElements.filter { it != nextElement }
          buildPartialTuplesOfSize(size - 1, remainingElements, newPartiallyBuiltGroups)
        }
        return groupsWithDifferentNextElementSelected.flatten()
      }
    }
    return buildPartialTuplesOfSize(size, elements, listOf(emptyList<T>()))
  }
}

fun main() {
  val elements = listOf(1, 2, 3, 4, 5)
  val tuples = Tuples.tuplesOfSize(2, elements)
  println(tuples)
}