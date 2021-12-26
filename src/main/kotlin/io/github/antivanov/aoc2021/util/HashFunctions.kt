package io.github.antivanov.aoc2021.util

object HashFunctions {
 private val PrimaryNumber = 37

 fun listOfNumbersHash(elements: List<Long>): Long  =
   elements.fold(1) { acc, it ->
     acc * PrimaryNumber + it
   }
}