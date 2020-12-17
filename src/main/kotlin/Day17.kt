package main

import java.io.File

val GRID_SIZE = 25

fun main() {
    var grid = newGrid()
    File("inputs/Day17").readLines().forEachIndexed{ x, row ->
        row.forEachIndexed { y, c ->
            grid[GRID_SIZE / 2][x + (GRID_SIZE / 2)][y + (GRID_SIZE / 2)] = c == '#'
        }
    }

    // part 1
    println("cycle 0: ${getCount(grid)}")
    (1..6).forEach{
        grid = executeCycle(grid)
        println("cycle $it: ${getCount(grid)}")
    }
    println()

    // part 2
    var grid2 = newGrid2()
    File("inputs/Day17").readLines().forEachIndexed{ x, row ->
        row.forEachIndexed { y, c ->
            grid2[GRID_SIZE / 2][GRID_SIZE / 2][x + (GRID_SIZE / 2)][y + (GRID_SIZE / 2)] = c == '#'
        }
    }
    println("cycle 0: ${getCount2(grid2)}")
    (1..6).forEach{
        grid2 = executeCycle2(grid2)
        println("cycle $it: ${getCount2(grid2)}")
    }
    println()
}

fun getCount(grid: MutableList<MutableList<MutableList<Boolean>>>): Int {
    return grid.flatten().flatten().filter { x -> x }.count()
}

fun getCount2(grid: MutableList<MutableList<MutableList<MutableList<Boolean>>>>): Int {
    return grid.flatten().flatten().flatten().filter { x -> x }.count()
}

fun executeCycle(grid: MutableList<MutableList<MutableList<Boolean>>>): MutableList<MutableList<MutableList<Boolean>>> {
    val newGrid = newGrid()
    // cycle
    grid.forEachIndexed{z, plane ->
        plane.forEachIndexed{x, row ->
            row.forEachIndexed{y, value ->
                val neighbours = getNeighbours(grid, z, x, y)
                val active = neighbours.filter { x -> x }.count()
                val newValue = if (value) active == 2 || active == 3 else active == 3
                newGrid[z][x][y] = newValue
            }
        }
    }
    return newGrid
}

fun executeCycle2(grid: MutableList<MutableList<MutableList<MutableList<Boolean>>>>): MutableList<MutableList<MutableList<MutableList<Boolean>>>> {
    val newGrid = newGrid2()
    // cycle
    grid.forEachIndexed{z, plane ->
        plane.forEachIndexed{x, row ->
            row.forEachIndexed{y, col ->
                col.forEachIndexed { w, value ->
                    val neighbours = getNeighbours2(grid, z, x, y, w)
                    val active = neighbours.filter { x -> x }.count()
                    val newValue = if (value) active == 2 || active == 3 else active == 3
                    newGrid[z][x][y][w] = newValue
                }
            }
        }
    }
    return newGrid
}

fun newGrid(): MutableList<MutableList<MutableList<Boolean>>> {
    val grid = mutableListOf<MutableList<MutableList<Boolean>>>()
    (0..GRID_SIZE).forEach{
        val zDim = mutableListOf<MutableList<Boolean>>()
        (0..GRID_SIZE).forEach{
            val xDim = mutableListOf<Boolean>()
            (0..GRID_SIZE).forEach{
                xDim.add(false)
            }
            zDim.add(xDim)
        }
        grid.add(zDim)
    }
    return grid
}

fun newGrid2(): MutableList<MutableList<MutableList<MutableList<Boolean>>>> {
    val grid = mutableListOf<MutableList<MutableList<MutableList<Boolean>>>>()
    (0..GRID_SIZE).forEach{
        val zDim = mutableListOf<MutableList<MutableList<Boolean>>>()
        (0..GRID_SIZE).forEach{
            val xDim = mutableListOf<MutableList<Boolean>>()
            (0..GRID_SIZE).forEach{
                val yDim = mutableListOf<Boolean>()
                (0..GRID_SIZE).forEach{
                    yDim.add(false)
                }
                xDim.add(yDim)
            }
            zDim.add(xDim)
        }
        grid.add(zDim)
    }
    return grid
}

fun getNeighbours(
    grid: MutableList<MutableList<MutableList<Boolean>>>,
    z: Int,
    x: Int,
    y: Int
): MutableList<Boolean> {
    val result = mutableListOf<Boolean>()
    (z-1..z+1).forEach{ zIndex ->
        (x-1..x+1).forEach{ xIndex ->
            (y-1..y+1).forEach loop@{ yIndex ->
                if (zIndex == z && xIndex == x && yIndex == y) return@loop
                val value = grid.getOrNull(zIndex)?.getOrNull(xIndex)?.getOrNull(yIndex)
                result.add(value == true)
            }
        }
    }
    return result
}

fun getNeighbours2(
    grid: MutableList<MutableList<MutableList<MutableList<Boolean>>>>,
    z: Int,
    x: Int,
    y: Int,
    w: Int
): MutableList<Boolean> {
    val result = mutableListOf<Boolean>()
    (z-1..z+1).forEach{ zIndex ->
        (x-1..x+1).forEach{ xIndex ->
            (y-1..y+1).forEach{ yIndex ->
                (w-1..w+1).forEach loop@{ wIndex ->
                    if (zIndex == z && xIndex == x && yIndex == y && w == wIndex) return@loop
                    val value = grid.getOrNull(zIndex)?.getOrNull(xIndex)?.getOrNull(yIndex)
                        ?.getOrNull(wIndex)
                    result.add(value == true)
                }
            }
        }
    }
    return result
}
