package io.github.antivanov.aoc2021

import arrow.core.Some
import kotlin.test.Test
import kotlin.test.assertEquals

class DaysSpec {

  val day1TestInput = listOf(
    199,
    200,
    208,
    210,
    200,
    207,
    240,
    269,
    260,
    263
  )

  @Test
  fun test_day1_part1() {
    assertEquals(7, Day1.part1(day1TestInput))
  }

  @Test
  fun test_day1_part2() {
    assertEquals(5, Day1.part2(day1TestInput))
  }

  val day2TestInput = listOf(
    Day2.MovementInstruction.Forward(5),
    Day2.MovementInstruction.Down(5),
    Day2.MovementInstruction.Forward(8),
    Day2.MovementInstruction.Up(3),
    Day2.MovementInstruction.Down(8),
    Day2.MovementInstruction.Forward(2)
  )

  @Test
  fun test_day2_part1() {
    assertEquals(150, Day2.part1(day2TestInput))
  }

  @Test
  fun test_day2_part2() {
    assertEquals(900, Day2.part2(day2TestInput))
  }

  val day3TestInput: List<List<Int>> = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
  """.trimIndent().split("\n").map { it.toCharArray().toList().map { it.toString().toInt() } }

  @Test
  fun test_day3_part1() {
    assertEquals(198, Day3.part1(day3TestInput))
  }

  @Test
  fun test_day3_part2() {
    assertEquals(230, Day3.part2(day3TestInput))
  }

  val day4TestInput = Day4.parseInput("""
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
  """.trimIndent().split("\n").map { it.trimIndent() }
  )

  @Test
  fun test_day4_part1() {
    val (drawnNumbers, boards) = day4TestInput
    assertEquals(Some(4512), Day4.part1(drawnNumbers, boards))
  }

  @Test
  fun test_day4_part2() {
    val (drawnNumbers, boards) = day4TestInput
    assertEquals(Some(1924), Day4.part2(drawnNumbers, boards))
  }

  val day5TestInput = Day5.parseInput("""
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
  """.trimIndent().split("\n"))

  @Test
  fun test_day5_part1() {
    assertEquals(5, Day5.part1(day5TestInput))
  }

  @Test
  fun test_day5_part2() {
    assertEquals(12, Day5.part2(day5TestInput))
  }

  val day6TestInput = listOf(3, 4, 3, 1, 2)

  @Test
  fun test_day6_part1() {
    assertEquals(5934, Day6.part1(day6TestInput))
  }

  @Test
  fun test_day6_part2() {
    assertEquals(26984457539, Day6.part2(day6TestInput))
  }

  val day7TestInput = Day7.parseInput("16,1,2,0,4,2,7,1,2,14")

  @Test
  fun test_day7_part1() {
    assertEquals(37, Day7.part1(day7TestInput))
  }

  @Test
  fun test_day7_part2() {
    assertEquals(168, Day7.part2(day7TestInput))
  }

  val day8TestInput = Day8.parseInput("""
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
  """.trimIndent())

  @Test
  fun test_day8_part1() {
    assertEquals(26, Day8.part1(day8TestInput))
  }

  @Test
  fun test_day8_part2() {
    assertEquals(61229, Day8.part2(day8TestInput))
  }

  val day9TestInput = Day9.parseInput("""
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
  """.trimIndent())

  @Test
  fun test_day9_part1() {
    assertEquals(15, Day9.part1(day9TestInput))
  }

  @Test
  fun test_day9_part2() {
    assertEquals(1134, Day9.part2(day9TestInput))
  }

  val day10TestInput = Day10.parseInput("""
    [({(<(())[]>[[{[]{<()<>>
    [(()[<>])]({[<{<<[]>>(
    {([(<{}[<>[]}>{[]{[(<()>
    (((({<>}<{<{<>}{[]{[]{}
    [[<[([]))<([[{}[[()]]]
    [{[{({}]{}}([{[{{{}}([]
    {<[[]]>}<{[{[{[]{()[[[]
    [<(<(<(<{}))><([]([]()
    <{([([[(<>()){}]>(<<{{
    <{([{{}}[<[[[<>{}]]]>[]]
  """.trimIndent())

  @Test
  fun test_day10_part1() {
    assertEquals(26397, Day10.part1(day10TestInput))
  }

  @Test
  fun test_day10_part2() {
    assertEquals(288957, Day10.part2(day10TestInput))
  }

  val day11TestInput = Day11.parseInput("""
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
  """.trimIndent())

  @Test
  fun test_day11_part1() {
    assertEquals(1656, Day11.part1(day11TestInput))
  }

  @Test
  fun test_day11_part2() {
    assertEquals(195, Day11.part2(day11TestInput))
  }

  val day12TestInput = Day12.parseInput("""
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
  """.trimIndent())

  @Test
  fun test_day12_part1() {
    assertEquals(226, Day12.part1(day12TestInput))
  }

  @Test
  fun test_day12_part2() {
    assertEquals(3509, Day12.part2(day12TestInput))
  }

  val day13TestInput = Day13.parseInput("""
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along y=7
    fold along x=5
  """.trimIndent())

  @Test
  fun test_day13_part1() {
    val (day13Paper, day13Instructions) = day13TestInput
    assertEquals(17, Day13.part1(day13Paper, day13Instructions))
  }

  @Test
  fun test_day13_part2() {
    val (day13Paper, day13Instructions) = day13TestInput
    assertEquals("""
#####
#...#
#...#
#...#
#####
.....
.....
""".trimIndent(), Day13.part2(day13Paper, day13Instructions).toString())
  }

  val day14TestInput = Day14.parseInput("""
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
  """.trimIndent())

  @Test
  fun test_day14_part1() {
    val (polymer, rules) = day14TestInput
    assertEquals(1588, Day14.part1(polymer, rules))
  }

  @Test
  fun test_day14_part2() {
    val (polymer, rules) = day14TestInput
    assertEquals(2188189693529, Day14.part2(polymer, rules))
  }

  val day15TestInput = Day15.parseInput("""
    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
  """.trimIndent())

  @Test
  fun test_day15_part1() {
    assertEquals(40, Day15.part1(day15TestInput))
  }

  @Test
  fun test_day15_part2() {
    assertEquals(315, Day15.part2(day15TestInput))
  }
}