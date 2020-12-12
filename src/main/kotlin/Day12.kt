package main

import main.Type.*
import java.io.File
import kotlin.math.abs

private val PATTERN = Regex("([A-Z])([0-9]*)")

fun main() {
    val actions = mutableListOf<Action>()
    File("inputs/day12").forEachLine {
        val (action, value) = PATTERN.matchEntire(it)!!.destructured
        when (action) {
            "N" -> actions.add(Action(NORTH, value.toInt()))
            "S" -> actions.add(Action(SOUTH, value.toInt()))
            "E" -> actions.add(Action(EAST, value.toInt()))
            "W" -> actions.add(Action(WEST, value.toInt()))
            "L" -> actions.add(Action(LEFT, value.toInt()))
            "R" -> actions.add(Action(RIGHT, value.toInt()))
            "F" -> actions.add(Action(FORWARD, value.toInt()))
        }
    }

    var current = Triple(0, 0, EAST)
    for (action in actions) {
        current = action.perform(current)
    }
    println("(1) current location: $current, manhattan distance: ${abs(current.first) + abs(current.second)}")

    var currentShip = Pair(0, 0)
    var currentWaypoint = Pair(10, 1)
    for (action in actions) {
        val (newShip, newWaypoint) = action.perform(currentShip, currentWaypoint)
        currentShip = newShip
        currentWaypoint = newWaypoint
    }
    println("(1) current location: $currentShip, manhattan distance: ${abs(currentShip.first) + abs(currentShip.second)}")
}

class Action(private val type: Type, private val value: Int) {
    fun perform(current: Triple<Int, Int, Type>): Triple<Int, Int, Type> {
        return when (type) {
            NORTH -> current.copy(current.first, current.second + value, current.third)
            SOUTH -> current.copy(current.first, current.second - value, current.third)
            EAST -> current.copy(current.first + value, current.second, current.third)
            WEST -> current.copy(current.first - value, current.second, current.third)
            LEFT -> current.copy(current.first, current.second, nextType(current.third, -value))
            RIGHT -> current.copy(current.first, current.second, nextType(current.third, value))
            FORWARD -> Action(current.third, value).perform(current)
        }
    }

    private fun nextType(startingDirection: Type, degrees: Int): Type {
        if (degrees == 0) return startingDirection
        if (degrees < 0) {
            return when (startingDirection) {
                NORTH -> nextType(WEST, degrees + 90)
                WEST -> nextType(SOUTH, degrees + 90)
                SOUTH -> nextType(EAST, degrees + 90)
                EAST -> nextType(NORTH, degrees + 90)
                else -> throw RuntimeException("unexpected startingDirection $startingDirection")
            }
        }
        return when (startingDirection) {
            NORTH -> nextType(EAST, degrees - 90)
            EAST -> nextType(SOUTH, degrees - 90)
            SOUTH -> nextType(WEST, degrees - 90)
            WEST -> nextType(NORTH, degrees - 90)
            else -> throw RuntimeException("unexpected startingDirection $startingDirection")
        }
    }

    fun perform(ship: Pair<Int, Int>, waypoint: Pair<Int, Int>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        return when (type) {
            FORWARD -> Pair(Pair(ship.first + (waypoint.first * value), ship.second + (waypoint.second * value)), waypoint)
            NORTH -> Pair(ship, waypoint.copy(second = waypoint.second + value))
            SOUTH -> Pair(ship, waypoint.copy(second = waypoint.second - value))
            EAST -> Pair(ship, waypoint.copy(first = waypoint.first + value))
            WEST -> Pair(ship, waypoint.copy(first = waypoint.first - value))
            RIGHT -> Pair(ship, rotate(waypoint, value))
            LEFT -> Pair(ship, rotate(waypoint, -value))
        }
    }

    private fun rotate(waypoint: Pair<Int, Int>, degrees: Int): Pair<Int, Int> {
        if (degrees == 0) return waypoint
        if (degrees > 0) {
            val second = -waypoint.first
            val first = waypoint.second
            return rotate(Pair(first, second), degrees - 90)
        }
        val second = waypoint.first
        val first = -waypoint.second
        return rotate(Pair(first, second), degrees + 90)
    }
}

enum class Type {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    LEFT,
    RIGHT,
    FORWARD
}
