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

  val day16TestInput = "A0016C880162017C3686B18A3D4780"

  @Test
  fun test_day16_part1() {
    assertEquals(31, Day16.part1(day16TestInput))
  }

  @Test
  fun test_day16_part2() {
    assertEquals(54, Day16.part2(day16TestInput))
  }

  val day17TestInput = Day17.parseInput("target area: x=20..30, y=-10..-5")

  @Test
  fun test_day17_part1() {
    assertEquals(45, Day17.part1(day17TestInput))
  }

  @Test
  fun test_day17_part2() {
    assertEquals(112, Day17.part2(day17TestInput))
  }

  val day18TestInput = Day18.parse("""
    [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
    [[[5,[2,8]],4],[5,[[9,9],0]]]
    [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
    [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
    [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
    [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
    [[[[5,4],[7,7]],8],[[8,3],8]]
    [[9,3],[[9,9],[6,[4,9]]]]
    [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
    [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
  """.trimIndent())

  @Test
  fun test_day18_part1() {
    assertEquals(4140, Day18.part1(day18TestInput))
  }

  @Test
  fun test_day18_part2() {
    assertEquals(3993, Day18.part2(day18TestInput))
  }

  val day19TestInput = Day19.parse("""
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
  """.trimIndent())

  @Test
  fun test_day19_part1() {
    assertEquals(79, Day19.part1(day19TestInput))
  }

  @Test
  fun test_day19_part2() {
    assertEquals(3621, Day19.part2(day19TestInput))
  }

  val day20TestInput = Day20.parseInput("""
..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
.#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
.#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###
  """.trimIndent())

  @Test
  fun test_day20_part1() {
    val (key, initialPlane) = day20TestInput
    assertEquals(35, Day20.part1(key, initialPlane))
  }

  @Test
  fun test_day20_part2() {
    val (key, initialPlane) = day20TestInput
    assertEquals(3351, Day20.part2(key, initialPlane))
  }

  val day21Input = Day21.parseInput("""
Player 1 starting position: 4
Player 2 starting position: 8
""".trimIndent()
  )

  @Test
  fun test_day21_part1() {
    val (firstPosition, secondPosition) = day21Input
    assertEquals(739785, Day21.part1(firstPosition, secondPosition))
  }

  @Test
  fun test_day21_part2() {
    val (firstPosition, secondPosition) = day21Input
    assertEquals(444356092776315, Day21.part2(firstPosition, secondPosition))
  }

  val day22InputPart1 = Day22.parseInput("""
    on x=-20..26,y=-36..17,z=-47..7
    on x=-20..33,y=-21..23,z=-26..28
    on x=-22..28,y=-29..23,z=-38..16
    on x=-46..7,y=-6..46,z=-50..-1
    on x=-49..1,y=-3..46,z=-24..28
    on x=2..47,y=-22..22,z=-23..27
    on x=-27..23,y=-28..26,z=-21..29
    on x=-39..5,y=-6..47,z=-3..44
    on x=-30..21,y=-8..43,z=-13..34
    on x=-22..26,y=-27..20,z=-29..19
    off x=-48..-32,y=26..41,z=-47..-37
    on x=-12..35,y=6..50,z=-50..-2
    off x=-48..-32,y=-32..-16,z=-15..-5
    on x=-18..26,y=-33..15,z=-7..46
    off x=-40..-22,y=-38..-28,z=23..41
    on x=-16..35,y=-41..10,z=-47..6
    off x=-32..-23,y=11..30,z=-14..3
    on x=-49..-5,y=-3..45,z=-29..18
    off x=18..30,y=-20..-8,z=-3..13
    on x=-41..9,y=-7..43,z=-33..15
    on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
    on x=967..23432,y=45373..81175,z=27513..53682
  """.trimIndent())

  @Test
  fun test_day22_part1() {
    assertEquals(590784, Day22.part1(day22InputPart1))
  }

  val day22InputPart2 = Day22.parseInput("""
    on x=-5..47,y=-31..22,z=-19..33
    on x=-44..5,y=-27..21,z=-14..35
    on x=-49..-1,y=-11..42,z=-10..38
    on x=-20..34,y=-40..6,z=-44..1
    off x=26..39,y=40..50,z=-2..11
    on x=-41..5,y=-41..6,z=-36..8
    off x=-43..-33,y=-45..-28,z=7..25
    on x=-33..15,y=-32..19,z=-34..11
    off x=35..47,y=-46..-34,z=-11..5
    on x=-14..36,y=-6..44,z=-16..29
    on x=-57795..-6158,y=29564..72030,z=20435..90618
    on x=36731..105352,y=-21140..28532,z=16094..90401
    on x=30999..107136,y=-53464..15513,z=8553..71215
    on x=13528..83982,y=-99403..-27377,z=-24141..23996
    on x=-72682..-12347,y=18159..111354,z=7391..80950
    on x=-1060..80757,y=-65301..-20884,z=-103788..-16709
    on x=-83015..-9461,y=-72160..-8347,z=-81239..-26856
    on x=-52752..22273,y=-49450..9096,z=54442..119054
    on x=-29982..40483,y=-108474..-28371,z=-24328..38471
    on x=-4958..62750,y=40422..118853,z=-7672..65583
    on x=55694..108686,y=-43367..46958,z=-26781..48729
    on x=-98497..-18186,y=-63569..3412,z=1232..88485
    on x=-726..56291,y=-62629..13224,z=18033..85226
    on x=-110886..-34664,y=-81338..-8658,z=8914..63723
    on x=-55829..24974,y=-16897..54165,z=-121762..-28058
    on x=-65152..-11147,y=22489..91432,z=-58782..1780
    on x=-120100..-32970,y=-46592..27473,z=-11695..61039
    on x=-18631..37533,y=-124565..-50804,z=-35667..28308
    on x=-57817..18248,y=49321..117703,z=5745..55881
    on x=14781..98692,y=-1341..70827,z=15753..70151
    on x=-34419..55919,y=-19626..40991,z=39015..114138
    on x=-60785..11593,y=-56135..2999,z=-95368..-26915
    on x=-32178..58085,y=17647..101866,z=-91405..-8878
    on x=-53655..12091,y=50097..105568,z=-75335..-4862
    on x=-111166..-40997,y=-71714..2688,z=5609..50954
    on x=-16602..70118,y=-98693..-44401,z=5197..76897
    on x=16383..101554,y=4615..83635,z=-44907..18747
    off x=-95822..-15171,y=-19987..48940,z=10804..104439
    on x=-89813..-14614,y=16069..88491,z=-3297..45228
    on x=41075..99376,y=-20427..49978,z=-52012..13762
    on x=-21330..50085,y=-17944..62733,z=-112280..-30197
    on x=-16478..35915,y=36008..118594,z=-7885..47086
    off x=-98156..-27851,y=-49952..43171,z=-99005..-8456
    off x=2032..69770,y=-71013..4824,z=7471..94418
    on x=43670..120875,y=-42068..12382,z=-24787..38892
    off x=37514..111226,y=-45862..25743,z=-16714..54663
    off x=25699..97951,y=-30668..59918,z=-15349..69697
    off x=-44271..17935,y=-9516..60759,z=49131..112598
    on x=-61695..-5813,y=40978..94975,z=8655..80240
    off x=-101086..-9439,y=-7088..67543,z=33935..83858
    off x=18020..114017,y=-48931..32606,z=21474..89843
    off x=-77139..10506,y=-89994..-18797,z=-80..59318
    off x=8476..79288,y=-75520..11602,z=-96624..-24783
    on x=-47488..-1262,y=24338..100707,z=16292..72967
    off x=-84341..13987,y=2429..92914,z=-90671..-1318
    off x=-37810..49457,y=-71013..-7894,z=-105357..-13188
    off x=-27365..46395,y=31009..98017,z=15428..76570
    off x=-70369..-16548,y=22648..78696,z=-1892..86821
    on x=-53470..21291,y=-120233..-33476,z=-44150..38147
    off x=-93533..-4276,y=-16170..68771,z=-104985..-24507
  """.trimIndent())

  @Test
  fun test_day22_part2() {
    assertEquals(2758514936282235, Day22.part2(day22InputPart2))
  }
}