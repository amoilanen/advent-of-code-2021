package io.github.antivanov.aoc2021

import arrow.core.*
import io.github.antivanov.aoc2021.util.ParsingUtils

object Day4 {
  val input = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
""".trimIndent()

  data class MatchableNumbers(val boardId: Int, val numbers: Set<Int>)

  data class Board(val id: Int, val cells: List<List<Int>>, val size: Int) {

    fun rowsAndColumns(): List<MatchableNumbers> {
      val columns = (0 until size).map { columnIndex ->
        cells.map { it[columnIndex] }.toSet()
      }
      val rows = cells.map { it.toSet() }
      return (rows + columns).map { MatchableNumbers(id, it) }
    }
  }

  private fun parseDrawnNumbers(input: String): List<Int> =
    input.split(",").map { it.toInt() }

  private fun parseBoard(boardInput: List<String>): Board =
    Board(
      0,
      boardInput.map {
        it.split(" +".toRegex()).map {
          it.toInt()
        }
      },
      boardInput.size
    )

  fun parseInput(inputLines: List<String>): Pair<List<Int>, List<Board>> {
    val groupedInputLines = ParsingUtils.splitByElement(inputLines, "")
    val drawnNumbers = parseDrawnNumbers(groupedInputLines[0][0])
    val boards = groupedInputLines.tail().mapIndexed { index, boardInput ->
      parseBoard(boardInput).copy(id = index)
    }
    return drawnNumbers to boards
  }

  private fun findWinningMatchableNumbers(numbersToDraw: List<Int>, toMatch: List<MatchableNumbers>, minimumPossibleDrawnNumbersCount: Int): Option<Pair<List<Int>, MatchableNumbers>> {
    fun find(numbersToDraw: List<Int>, toMatch: List<MatchableNumbers>, alreadyDrawnNumbersCount: Int): Option<Pair<List<Int>, MatchableNumbers>> {
      val drawnNumbers = numbersToDraw.subList(0, alreadyDrawnNumbersCount)
      val foundMatchableNumbers = toMatch.find { drawnNumbers.containsAll(it.numbers) }.toOption()
      return if (foundMatchableNumbers.isDefined()) {
        foundMatchableNumbers.map { drawnNumbers to it }
      } else if (alreadyDrawnNumbersCount >= numbersToDraw.size) {
        None
      } else {
        find(numbersToDraw, toMatch, alreadyDrawnNumbersCount + 1)
      }
    }
    return find(numbersToDraw, toMatch, minimumPossibleDrawnNumbersCount)
  }

  private fun winningBoardScore(numbersDrawnToWin: List<Int>, board: Board): Int {
    val lastDrawnNumber = numbersDrawnToWin.last()
    val unmarkedNumbers = board.cells.flatMap { row ->
      row.filter {
        !numbersDrawnToWin.contains(it)
      }
    }
    return unmarkedNumbers.sum() * lastDrawnNumber
  }

  fun part1(drawnNumbers: List<Int>, boards: List<Board>): Option<Int> {
    val boardIdToBoard = boards.associateBy({ it.id }, { it })
    val allMatchableNumbers = boards.flatMap { it.rowsAndColumns() }
    val foundWinningMatchableNumbers = findWinningMatchableNumbers(drawnNumbers, allMatchableNumbers, boards[0].size)
    return foundWinningMatchableNumbers.flatMap { (numbersDrawnToWin, matchedNumbers) ->
      val matchedBoard = boardIdToBoard[matchedNumbers.boardId].toOption()
      matchedBoard.map {
        winningBoardScore(numbersDrawnToWin, it)
      }
    }
  }
}

fun main() {
  val (drawnNumbers, boards) = Day4.parseInput(Day4.input.split("\n").map { it.trimIndent() })
  println(Day4.part1(drawnNumbers, boards))
}