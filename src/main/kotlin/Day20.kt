package main

import java.io.File
import kotlin.math.sqrt

fun main() {
    val imageStrings = File("inputs/Day20").readText().split("\n\n")
    val tiles = imageStrings
        .filter(String::isNotEmpty)
        .map { l ->
            l.split(':')[0].split(' ')[1].toInt() to
                    l.split(":\n")[1].lines().filter(String::isNotEmpty)
        }
        .toMap()

    var sum = 0L
    val neighbours = mutableMapOf<Int, MutableList<Int>>()
    tiles.forEach { (tile, image) ->
        tiles.filter { it.key != tile }.forEach { (nTile, nImage) ->
            val dirs = isNeighbour(image, nImage)
            if (dirs.isNotEmpty()) {
                val n = neighbours.getOrDefault(tile, mutableListOf())
                if (!n.contains(nTile)) n.add(nTile)
                neighbours[tile] = n
            }
        }
        if (neighbours[tile]!!.size == 2) {
            if (sum == 0L) {
                sum = tile.toLong()
            } else {
                sum *= tile
            }
        }
    }
    println(sum)

    // construct image
    val gridSize = sqrt(tiles.size.toDouble()).toInt()
    val fullImage = mutableListOf<MutableList<List<String>>>()
    val indices = mutableListOf<MutableList<Int>>()
    (1..gridSize).forEach {
        fullImage.add(mutableListOf())
        indices.add(mutableListOf())
    }
    val firstImage = neighbours.filter { e -> e.value.size == 2 }.keys.first()
    val n = neighbours[firstImage]!!
    val initial = getRotations(tiles[firstImage]!!).first { r ->
        getRightNeighbour(r, tiles[n[0]]!!) != null &&
        getBottomNeighbour(r, tiles[n[1]]!!) != null }
    fullImage[0].add(initial.toMutableList())
    indices[0].add(firstImage)
    (0 until gridSize).forEach { row ->
        (0 until gridSize).forEach loop@{ col ->
            if (row == 0 && col == 0) return@loop
            if (col == 0) {
                val top = fullImage[row - 1][col]
                neighbours[indices[row - 1][col]]!!.forEach { candidate ->
                    val image = getBottomNeighbour(top, tiles[candidate]!!)
                    if (image != null) {
                        fullImage[row].add(image)
                        indices[row].add(candidate)
                        return@loop
                    }
                }
            } else {
                val left = fullImage[row][col - 1]
                neighbours[indices[row][col - 1]]!!.forEach { candidate ->
                    val image = getRightNeighbour(left, tiles[candidate]!!)
                    if (image != null) {
                        fullImage[row].add(image)
                        indices[row].add(candidate)
                        return@loop
                    }
                }
            }
        }
    }
    // crop
    val cropped = fullImage
        .map { r -> r.map(::crop) }
    val singleImage = cropped
        .map { r -> r[0].mapIndexed{i, _ -> r.map { t -> t[i] }.joinToString(separator = "")} }
        .flatten()

    // find sea monsters
    val seaMonster = """
                          # 
        #    ##    ##    ###
         #  #  #  #  #  #   
    """.trimIndent()
    val seaMonsterMap = mutableListOf<Pair<Int, Int>>()
    seaMonster.lines().forEachIndexed{i, l -> l.forEachIndexed{i2, c ->
        if (c == '#') seaMonsterMap.add(Pair(i, i2))
    }}

    // count the monsters
    getRotations(singleImage).forEach{ si ->
        var count = 0
        si.forEachIndexed{ r, l -> l.forEachIndexed{c, _ ->
            val isMonster = seaMonsterMap.all {
                si.getOrNull(r + it.first)?.getOrNull(c + it.second) == '#'
            }
            if (isMonster) count++
        }}
        if (count > 0) {
            println(singleImage.joinToString().count{c -> c == '#'} - (count * seaMonsterMap.size))
        }
    }
}

fun crop(t: List<String>): List<String> {
    return t.filterIndexed{ i, _ -> i != 0 && i != t.size - 1}
        .map { s -> s.substring(1, s.length - 1) }
}

fun printImage(fullImage: List<List<List<String>>>) {
    println("image:")
    fullImage.forEach { row ->
        printRow(row)
        println()
    }
}

fun printRow(row: List<List<String>>) {
    if (row.isEmpty()) return
    row[0].forEachIndexed { i, _ ->
        row.forEach { t -> print("${t[i]} ") }
        println()
    }
}

fun printTile(t: List<String>) {
    t.forEach(::println)
}

fun isNeighbour(image: List<String>, nImage: List<String>): Set<String> {
    val result = mutableSetOf<String>()
    val rotations = getRotations(nImage)
    if (rotations.map { i -> i.first() }.contains(image.first())) result.add("TOP")
    if (rotations.map(::getRight).contains(getRight(image))) result.add("RIGHT")
    if (rotations.map { i -> i.last() }.contains(image.last())) result.add("BOTTOM")
    if (rotations.map(::getLeft).contains(getLeft(image))) result.add("LEFT")

    return result
}

fun getRightNeighbour(image: List<String>, nImage: List<String>): List<String>? {
    val rotations = getRotations(nImage)
    val right = getRight(image)
    return rotations.firstOrNull { r -> getLeft(r) == right }
}

fun getBottomNeighbour(image: List<String>, nImage: List<String>): List<String>? {
    val rotations = getRotations(nImage)
    val bottom = image.last()
    return rotations.firstOrNull { r -> r.first() == bottom }
}

fun getRotations(nImage: List<String>): List<List<String>> {
    return listOf(
        nImage,
        nImage.map(String::reversed),
        nImage
            .mapIndexed { i, _ ->
                nImage.reversed().map { s -> s[i] }.joinToString(separator = "")
            },
        nImage
            .mapIndexed { i, _ -> nImage.reversed().map { s -> s[i] }.joinToString(separator = "") }
            .map(String::reversed),
        nImage.reversed(),
        nImage.reversed().map { s -> s.reversed() },
        nImage
            .mapIndexed { i, _ ->
                nImage.map { s -> s[s.length - i - 1] }.joinToString(separator = "")
            },
        nImage
            .mapIndexed { i, _ ->
                nImage.map { s -> s[s.length - i - 1] }.joinToString(separator = "")
            }
            .map(String::reversed),
    )
}

fun getRight(image: List<String>): String {
    return image.map { s -> s.last() }.joinToString(separator = "")
}

fun getLeft(image: List<String>): String {
    return image.map { s -> s.first() }.joinToString(separator = "")
}
