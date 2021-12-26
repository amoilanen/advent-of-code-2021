package io.github.antivanov.aoc2021

import io.github.antivanov.aoc2021.util.ParsingUtils
import io.github.antivanov.aoc2021.util.HashFunctions.listOfNumbersHash
import io.github.antivanov.aoc2021.util.Tuples.setsOfSize
import kotlin.math.abs
import kotlin.math.sign

object Day19 {

  val input = """
    --- scanner 0 ---
    404,-588,-901
    528,-643,409
    -838,591,734
    390,-675,-793
    -537,-823,-458
    -485,-357,347
    -345,-311,381
    -661,-816,-575
    -876,649,763
    -618,-824,-621
    553,345,-567
    474,580,667
    -447,-329,318
    -584,868,-557
    544,-627,-890
    564,392,-477
    455,729,728
    -892,524,684
    -689,845,-530
    423,-701,434
    7,-33,-71
    630,319,-379
    443,580,662
    -789,900,-551
    459,-707,401

    --- scanner 1 ---
    686,422,578
    605,423,415
    515,917,-361
    -336,658,858
    95,138,22
    -476,619,847
    -340,-569,-846
    567,-361,727
    -460,603,-452
    669,-402,600
    729,430,532
    -500,-761,534
    -322,571,750
    -466,-666,-811
    -429,-592,574
    -355,545,-477
    703,-491,-529
    -328,-685,520
    413,935,-424
    -391,539,-444
    586,-435,557
    -364,-763,-893
    807,-499,-711
    755,-354,-619
    553,889,-390

    --- scanner 2 ---
    649,640,665
    682,-795,504
    -784,533,-524
    -644,584,-595
    -588,-843,648
    -30,6,44
    -674,560,763
    500,723,-460
    609,671,-379
    -555,-800,653
    -675,-892,-343
    697,-426,-610
    578,704,681
    493,664,-388
    -671,-858,530
    -667,343,800
    571,-461,-707
    -138,-166,112
    -889,563,-600
    646,-828,498
    640,759,510
    -630,509,768
    -681,-892,-333
    673,-379,-804
    -742,-814,-386
    577,-820,562

    --- scanner 3 ---
    -589,542,597
    605,-692,669
    -500,565,-823
    -660,373,557
    -458,-679,-417
    -488,449,543
    -626,468,-788
    338,-750,-386
    528,-832,-391
    562,-778,733
    -938,-730,414
    543,643,-506
    -524,371,-870
    407,773,750
    -104,29,83
    378,-903,-323
    -778,-728,485
    426,699,580
    -438,-605,-362
    -469,-447,-387
    509,732,623
    647,635,-688
    -868,-804,481
    614,-800,639
    595,780,-596

    --- scanner 4 ---
    727,592,562
    -293,-554,779
    441,611,-461
    -714,465,-776
    -743,427,-804
    -660,-479,-426
    832,-632,460
    927,-485,-438
    408,393,-506
    466,436,-512
    110,16,151
    -258,-428,682
    -393,719,612
    -211,-452,876
    808,-476,-593
    -575,615,604
    -485,667,467
    -680,325,-822
    -627,-443,-432
    872,-547,-609
    833,512,582
    807,604,487
    839,-516,451
    891,-625,532
    -652,-548,-490
    30,-46,-14
  """.trimIndent()

  data class Point(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point): Int =
      listOf(x - other.x, y - other.y, z - other.z).map {
        abs(it)
      }.sum()

    override fun toString(): String =
      "($x, $y, $z)"
  }

  data class PointGroup(val points: Set<Point>, val allPointDistances: Map<Pair<Point, Point>, Int>) {
    private fun computeSignaturesToPoints(points: Set<Point>): Map<Int, Point> {
      val pointDistances = setsOfSize(2, points).map { twoPoints ->
        val (first, second) = twoPoints.toList()
        first to allPointDistances.get(first to second)!!
      }
      val pointSignatures = pointDistances.groupBy {
        it.first
      }.mapValues {
        listOfNumbersHash(it.value.map { pointAndDistance ->
          pointAndDistance.second
        }.sorted())
      }
      return pointSignatures.toList().map {
        it.second to it.first
      }.toMap()
    }
    val signaturesToPoints: Map<Int, Point> = computeSignaturesToPoints(points)
    val groupSignature: Int = listOfNumbersHash(signaturesToPoints.toList().map { it.first }.sorted())

    override fun hashCode(): Int =
      groupSignature.toInt()

    override fun equals(other: Any?): Boolean =
      other is PointGroup && groupSignature == other.groupSignature

    override fun toString(): String =
      points.toString()
  }

  val FingerprintPointGroupSize = 3

  data class Scanner(val id: Int, val beacons: List<Point>) {

    private fun allPointDistances(): Map<Pair<Point, Point>, Int> =
      setsOfSize(2, beacons.toSet()).flatMap { twoPoints ->
        val (first, second) = twoPoints.toList()
        val distance = first.distanceTo(second)
        listOf((first to second) to distance, (second to first) to distance)
      }.toMap()

    fun pointGroupsOf(groupSize: Int): Set<PointGroup> =
      setsOfSize(groupSize, beacons.toSet()).map {
        PointGroup(it, allPointDistances())
      }.toSet()

    override fun toString(): String {
      val beacons = beacons.joinToString("\n")
      return """
Scanner($id,
$beacons
)""".trimIndent()
    }
  }

  fun parseInput(input: String): List<Scanner> {
    val lines = input.split("\n").map { it.trim() }
    val scannerInputs = ParsingUtils.splitByElement(lines, "")
    return scannerInputs.indices.zip(scannerInputs).map {
      parseScanner(it.first, it.second)
    }
  }

  private fun parseScanner(id: Int, input: List<String>): Scanner {
    val beacons = input.drop(1).map {
      parseBeacon(it)
    }
    return Scanner(id, beacons)
  }

  private fun parseBeacon(input: String): Point {
    val (x, y, z) = input.split(",").map { it.toInt() }
    return Point(x, y, z)
  }

  fun intersectGroups(first: Set<PointGroup>, second: Set<PointGroup>): Set<PointGroup> {
    val firstSignaturesAndGroups = first.map {
      it.groupSignature to it
    }
    val secondSignatures = second.map {
      it.groupSignature
    }
    return firstSignaturesAndGroups.filter {
      secondSignatures.contains(it.first)
    }.toMap().values.toSet()
  }
}

fun main() {
  val parsed = Day19.parseInput(Day19.input)
  val (firstScanner, secondScanner, thirdScanner, fourthScanner, fifthScanner) = parsed
  println()
  println(firstScanner)
  println()
  println(secondScanner)
  println()

  println("Checking group intersections:")
  val firstGroups = firstScanner.pointGroupsOf(Day19.FingerprintPointGroupSize)
  val secondGroups = secondScanner.pointGroupsOf(Day19.FingerprintPointGroupSize)
  val thirdGroups = thirdScanner.pointGroupsOf(Day19.FingerprintPointGroupSize)

  println("Intersection first & second:")
  val firstAndSecond = Day19.intersectGroups(firstGroups, secondGroups)
  println(firstAndSecond.size)
  println(firstAndSecond.take(5))
  println()

  println("Intersection first & third:")
  val firstAndThird = Day19.intersectGroups(firstGroups, thirdGroups)
  println(firstAndThird.size)
  println(firstAndThird.take(5))
  println()

  //TODO: Compute the number of groups having the same signature inside the same beacon - ?

  //TODO: Compute frequencies of points in the intersection and the number of detected point - do we detect all the common points?

  //TODO: Do we combinatorially detect all the detectable tuples which are common to both scanners?
  // 12 * 11 * 10 / 3! = 22 * 10 = 220 combinations

  //TODO: How to deal with false positives, i.e. first and third scanner?

  //Scanners which intersect have the most amount of point groups with matching signatures?
  //The most frequent point in the intersections is the real intersection point?

  //TODO:
}