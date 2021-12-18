package io.github.antivanov.aoc2021

object Day17 {
  val input = "target area: x=20..30, y=-10..-5"

  // Starting point of the vector is at the start of the coordinates (0, 0)
  data class Vector(val x: Int, val y: Int)
  data class Area(val topLeft: Vector, val bottomRight: Vector)

  data class ProjectileState(val position: Vector, val speed: Vector) {

    fun move(): ProjectileState {
      val updatedPosition = Vector(
        position.x + speed.x,
           position.y + speed.y
      )
      val updatedVelocity = Vector(
        if (speed.x > 0)
          speed.x - 1
        else if (speed.x < 0)
          speed.x + 1
        else
          0,
        speed.y - 1
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

    fun hitsTargetArea(targetArea: Area): Boolean =
      projectileSnapshotsBefore(targetArea.bottomRight).any { it.isWithinArea(targetArea) }
  }

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
}

fun main() {
  val targetArea = Day17.parseInput(Day17.input)
  println(targetArea)

  val initialSpeed = Day17.Vector(7, 2)
  val initialPoint = Day17.Vector(0,0)
  val initialState = Day17.ProjectileState(initialPoint, initialSpeed)
  val projectileSnapshots = initialState.projectileSnapshotsBefore(targetArea.bottomRight)
  projectileSnapshots.forEach {
    println(it)
  }
  val positionsWithinArea = projectileSnapshots.filter { it.isWithinArea(targetArea) }
  println(positionsWithinArea)
}