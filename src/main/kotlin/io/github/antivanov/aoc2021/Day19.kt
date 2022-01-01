package io.github.antivanov.aoc2021

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.toOption
import io.github.antivanov.aoc2021.util.ParsingUtils
import javax.xml.crypto.dsig.TransformService
import kotlin.math.abs

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

  open class Transformation(val f: (Point) -> Point) {
    companion object {
      val Identity = Transformation { p: Point -> p }
    }

    fun compose(other: Transformation): Transformation =
      Transformation { p: Point ->
        f(other.apply(p))
      }

    fun apply(p: Point): Point =
      f(p)
  }

  class LinearShift(vector: Point): Transformation({ p: Point ->
    p.copy(
      x = p.x + vector.x,
      y = p.y + vector.y,
      z = p.z + vector.z
    )
  })

  val AxisDirectionChanges: List<Transformation> = listOf(Transformation.Identity) + listOf(
    { p: Point -> p.copy(x = -p.x) },
    { p: Point -> p.copy(y = -p.y) },
    { p: Point -> p.copy(z = -p.z) },
    { p: Point -> p.copy(x = -p.x, y = -p.y) },
    { p: Point -> p.copy(x = -p.x, z = -p.z) },
    { p: Point -> p.copy(y = -p.y, z = -p.z) },
    { p: Point -> p.copy(x = -p.x, y = -p.y, z = -p.z) }
  ).map {
    Transformation(it)
  }

  val AxisRotations: List<Transformation> = listOf(Transformation.Identity) + listOf(
    { p: Point -> p.copy(x = p.y, y = p.z, z = p.x) },
    { p: Point -> p.copy(x = p.z, y = p.x, z = p.y) },
    { p: Point -> p.copy(x = p.z, y = p.y, z = p.x) },
    { p: Point -> p.copy(x = p.y, y = p.x, z = p.z) },
    { p: Point -> p.copy(x = p.x, y = p.z, z = p.y) }
  ).map {
    Transformation(it)
  }

  val PossibleTransformations: List<Transformation> =
    AxisDirectionChanges.flatMap { axisDirectionChange ->
      AxisRotations.map { axisRotation ->
        axisDirectionChange.compose(axisRotation)
      }
    }

  data class Point(val x: Int, val y: Int, val z: Int) {
    companion object {
      val Zero = Point(0, 0, 0)
    }

    fun distanceTo(other: Point): Int =
      listOf(x - other.x, y - other.y, z - other.z).map {
        abs(it) * abs(it)
      }.sum()

    fun manhattanDistanceTo(other: Point): Int =
      listOf(x - other.x, y - other.y, z - other.z).map {
        abs(it)
      }.sum()

    fun shiftBy(vector: Point): Point =
      this.copy(
        x = this.x + vector.x,
        y = this.y + vector.y,
        z = this.z + vector.z
      )

    override fun toString(): String =
      "($x, $y, $z)"
  }

  private fun computeDistancesFromPoints(points: List<Point>, distance: (Point, Point) -> Int): List<Pair<Point, Int>> =
    points.indices.flatMap { firstBeaconIndex ->
      (firstBeaconIndex + 1 until points.size).flatMap { secondBeaconIndex ->
        val first = points[firstBeaconIndex]
        val second = points[secondBeaconIndex]
        val distance = distance(first, second)
        listOf(first to distance, second to distance)
      }
    }

  private fun pairwiseDistances(points: List<Point>, distance: (Point, Point) -> Int): List<Int> =
    computeDistancesFromPoints(points, distance).map { it.second }

  data class Scanner(val id: Int, val beacons: List<Point>) {

    val distancesFromPoints: List<Pair<Point, Int>> = computeDistancesFromPoints(beacons, Point::distanceTo)

    /*
     * Fairly native but working approach (will not work in the general case) assuming that distances between
     * points are sufficiently different for the same scanner.
     */
    val pointToPointDistances: List<Int> = distancesFromPoints.map { it.second }

    override fun toString(): String {
      val beacons = beacons.joinToString("\n")
      return """
Scanner($id,
$beacons
)""".trimIndent()
    }
  }

  fun parse(input: String): List<Scanner> {
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

  // At least 12 elements in the intersection
  private const val AdjacentScannerIntersectionSize = 12
  private const val MinIntersectionSize = 12
  private const val MinimalPointToPointDistancesIntersectionSize = (AdjacentScannerIntersectionSize * (AdjacentScannerIntersectionSize - 1)) / 2

  fun haveIntersection(first: Scanner, second: Scanner): Boolean {
    val intersection = first.pointToPointDistances.filter {
      second.pointToPointDistances.contains(it)
    }
    /*
     * Should be MinimalPointToPointDistancesIntersectionSize but for some reason does not work with the input data =>
     * separate variable MinIntersectionSize
     */
    return intersection.size >= MinIntersectionSize
  }

  fun intersectionPoints(first: Scanner, second: Scanner): Pair<Set<Point>, Set<Point>> {
    val distanceIntersection = first.pointToPointDistances.intersect(second.pointToPointDistances.toSet())
    val firstPoints = first.distancesFromPoints.filter { distanceIntersection.contains(it.second) }.map { it.first }.toSet()
    val secondPoints = second.distancesFromPoints.filter { distanceIntersection.contains(it.second) }.map { it.first }.toSet()
    return firstPoints to secondPoints
  }

  fun findTransformationToFirst(first: Set<Point>, second: Set<Point>): Transformation? {
    val firstSmallestPoint = smallestPointOf(first)
    val transformationsToTry = PossibleTransformations.map { transformation ->
      val secondTransformed = second.map {
        transformation.apply(it)
      }.toSet()
      val secondTransformedSmallestPoint = smallestPointOf(secondTransformed)
      val shiftVector = Point(
        firstSmallestPoint.x - secondTransformedSmallestPoint.x,
        firstSmallestPoint.y - secondTransformedSmallestPoint.y,
        firstSmallestPoint.z - secondTransformedSmallestPoint.z
      )
      val linearShift = LinearShift(shiftVector)
      linearShift.compose(transformation)
    }
    return transformationsToTry.find { transformation ->
      val secondTransformed = second.map {
        transformation.apply(it)
      }
      secondTransformed.toSet() == first.toSet()
    }
  }

  fun findTransformationToFirst(first: Scanner, second: Scanner): Option<Transformation> {
    val (firstScannerPoints, secondScannerPoints) = intersectionPoints(first, second)
    return findTransformationToFirst(firstScannerPoints, secondScannerPoints).toOption()
  }

  private fun smallestPointOf(points: Set<Point>): Point =
    points.sortedWith { first, second ->
      if (first.x > second.x) 1
      else if (first.x < second.x) -1
      else if (first.y > second.y) 1
      else if (first.y < second.y) -1
      else if (first.z > second.z) 1
      else if (first.z < second.z) - 1
      else 0
    }.first()

  private fun buildScannerGraph(scanners: List<Scanner>): Map<Pair<Scanner, Scanner>, Transformation> =
    scanners.flatMap { scanner ->
      scanners.flatMap { otherScanner ->
        if (otherScanner != scanner && haveIntersection(scanner, otherScanner)) {
          val transformation = findTransformationToFirst(scanner, otherScanner)
          when (transformation) {
            is Some -> listOf((otherScanner to scanner) to transformation.value)
            is None -> emptyList()
          }
        } else
          emptyList()
      }
    }.toMap()

  /*
   * Floyd Warshall algorithm for building a transitive closure of a graph
   * https://www.programiz.com/dsa/floyd-warshall-algorithm
   */
  private fun buildClosure(scanners: List<Scanner>, scannerGraph: Map<Pair<Scanner, Scanner>, Transformation>): Map<Pair<Scanner, Scanner>, Transformation> {
    var currentGraph = scannerGraph
    var newEdges = scannerGraph.toList()
    while (newEdges.isNotEmpty()) {
      newEdges = scanners.flatMap { first ->
        scanners.filter {
          currentGraph.contains(first to it) && first != it
        }.flatMap { second ->
          val firstToSecond = currentGraph[first to second]!!
          scanners.filter {
            currentGraph.contains(second to it) && second != it
          }.flatMap { third ->
            if (!currentGraph.contains(first to third) && first != third) {
              val secondToThird = currentGraph[second to third]!!
              val firstToThird = secondToThird.compose(firstToSecond)
              listOf((first to third) to firstToThird)
            } else {
              emptyList()
            }
          }
        }
      }
      currentGraph = (currentGraph.toList() + newEdges).toMap()
    }
    return currentGraph
  }

  fun showGraph(scannerGraph: Map<Pair<Scanner, Scanner>, Transformation>) {
    val edges = scannerGraph.map {
      it.key.first.id to it.key.second.id
    }
    println(edges)
  }

  private fun findTransformationsToFirst(scanners: List<Scanner>): List<Pair<Scanner, Transformation>> {
    val scannerGraph = buildScannerGraph(scanners)
    val scannerGraphClosure = buildClosure(scanners, scannerGraph)
    val firstScanner = scanners.find { it.id == 0 }!!
    val scannersToJoin = scanners.filter { it.id != 0 }

    return scannersToJoin.map { scanner ->
      val transformation = scannerGraphClosure[scanner to firstScanner]!!
      scanner to transformation
    }
  }

  // An improved version over collectBeacons
  fun joinBeacons(scanners: List<Scanner>): Set<Point> {
    val firstScanner = scanners.find { it.id == 0 }!!
    val transformationsToFirst = findTransformationsToFirst(scanners)

    val firstScannerBeacons = firstScanner.beacons.toSet()
    val otherTransformedBeacons = transformationsToFirst.map {
      val scanner = it.first
      val transformation = it.second
      scanner.beacons.map { p ->
        transformation.apply(p)
      }.toSet()
    }.flatten().toSet()

    return firstScannerBeacons.union(otherTransformedBeacons)
  }

  private fun findScannerCoordinates(scanners: List<Scanner>): List<Point> {
    val transformationsToFirst = findTransformationsToFirst(scanners)
    val scannerPoint = Point(0, 0, 0)

    val transformedScanners = transformationsToFirst.map {
      val transformationToFirst = it.second
      transformationToFirst.apply(scannerPoint)
    }
    return listOf(scannerPoint) + transformedScanners
  }

  fun part1(scanners: List<Scanner>): Int =
    joinBeacons(scanners).size

  fun part2(scanners: List<Scanner>): Int {
    val scanners = findScannerCoordinates(scanners)
    val distances = pairwiseDistances(scanners, Point::manhattanDistanceTo)
    return distances.maxOrNull()!!
  }
}

fun main() {
  val scanners = Day19.parse(Day19.input)
  println(Day19.part1(scanners))
  println(Day19.part2(scanners))
}