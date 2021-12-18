package io.github.antivanov.aoc2021

object Day17 {
  val input = "target area: x=20..30, y=-10..-5"

  private val targetAreaRegex = "target area: x=([\\d\\-]+)\\.\\.([\\d\\-]+), y=([\\d\\-]+)..([\\d\\-]+)".toRegex()

  fun parseInput(input: String): Area {
    return targetAreaRegex.matchEntire(input)!!.destructured.let { (x1, x2, y1, y2) ->
      val xs = listOf(x1, x2).map { it.toInt() }.sorted()
      val ys = listOf(y1, y2).map { it.toInt() }.sorted().reversed()
      Area(
        Vector(xs[0], ys[0]),
        Vector(xs[1], ys[1])
      )
    }
  }

  // Starting point of the vector is at the start of the coordinates (0, 0)
  data class Vector(val x: Int, val y: Int)
  data class Area(val topLeft: Vector, val bottomRight: Vector)

  data class ProjectileState(val position: Vector, val velocity: Vector) {

    fun move(): ProjectileState {
      val updatedPosition = Vector(
        position.x + velocity.x,
           position.y + velocity.y
      )
      val updatedVelocity = Vector(
        if (velocity.x > 0)
          velocity.x - 1
        else if (velocity.x < 0)
          velocity.x + 1
        else
          0,
        velocity.y - 1
      )
      return ProjectileState(updatedPosition, updatedVelocity)
    }

    fun isWithinArea(area: Area): Boolean =
      position.x in (area.topLeft.x..area.bottomRight.x) &&
        position.y in (area.bottomRight.y..area.topLeft.y)

    fun projectileSnapshotsBefore(leftBottom: Vector): List<ProjectileState> {
      var currentState = this
      var states = emptyList<ProjectileState>()
      while ((currentState.position.x <= leftBottom.x) && (currentState.position.y >= leftBottom.y)) {
        states = states + currentState
        currentState = currentState.move()
      }
      return states
    }

    fun hitsTarget(targetArea: Area): Boolean {
      val snapshots = projectileSnapshotsBefore(targetArea.bottomRight)
      return snapshots.any { it.isWithinArea(targetArea) }
    }

    fun maximumHeightBefore(leftBottom: Vector): Int  =
      projectileSnapshotsBefore(leftBottom).map { it.position.y }.maxOrNull()!!
  }

  fun getCandidateVelocitiesToHitArea(targetArea: Area): List<Vector> {

    /*
     * If horizontal velocity is larger than the horizontally farthest point of the target area
     * the target will be immediately overshot.
     */
    val maxXVelocity = targetArea.bottomRight.x

    /*
     * Reasoning to get the upper boundary for yv - velocity along the yt axis:
     *
     * Let's call targetArea.bottomRight.x `d`
     *
     * 1. The number of steps to hit the target area can not be larger than `d` because horizontally projectile cannot move
     * slower than the minimal velocity of 1
     * 2. If `yv` is the vertical velocity and target is hit in `s` steps, then after `s` steps the velocity should be negative =>
     * `yv - s < 0` => `yv < s`
     * 3. Based on 1. the upper boundary for `yv` is `d`
     */
    val maxYVelocity = targetArea.bottomRight.x

    /*
     * If vertical velocity is lower than the coordinate of the vertically farthest point of the target area,
     * the target will be immediately overshot.
     */
    val minYVelocity = targetArea.bottomRight.y

    return (0..maxXVelocity).flatMap { vx ->
      (minYVelocity..maxYVelocity).map { vy ->
        Vector(vx, vy)
      }
    }
  }

  fun velocitiesToHitTarget(targetArea: Area): List<Vector> {
    val initialState = ProjectileState(Vector(0, 0), Vector(0, 0))
    val candidateVelocities = getCandidateVelocitiesToHitArea(targetArea)
    return candidateVelocities.filter {
      initialState.copy(velocity = it).hitsTarget(targetArea)
    }
  }

  fun part1(targetArea: Area): Int {
    val initialState = ProjectileState(Vector(0, 0), Vector(0, 0))
    val maxHeightsWhenHittingTargets = velocitiesToHitTarget(targetArea).map {
      initialState.copy(velocity = it).maximumHeightBefore(targetArea.bottomRight)
    }
    return maxHeightsWhenHittingTargets.maxOrNull()!!
  }

  fun part2(targetArea: Area): Int =
    velocitiesToHitTarget(targetArea).size
}

fun main() {
  val targetArea = Day17.parseInput(Day17.input)
  println(Day17.part1(targetArea))
  println(Day17.part2(targetArea))
}