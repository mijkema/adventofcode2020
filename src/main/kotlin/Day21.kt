package main

import java.io.File

fun main() {
    val ingredients = mutableMapOf<String, Int>()
    var allergens = mutableMapOf<String, MutableSet<String>>()
    File("inputs/Day21").readLines().forEach{
        val (ingredientString, allergenString) = it.split(" (contains ")
        val ing = ingredientString.split(' ')
        ing.forEach { i -> ingredients[i] = ingredients.getOrDefault(i, 0) + 1 }
        allergenString.replace(")", "").split(", ").forEach {a ->
            val opts = allergens.getOrDefault(a, ingredients.keys)
            allergens[a] = opts.intersect(ing).toMutableSet()
        }
    }
    val solution = ingredients
        .filter { e -> allergens.none { it.value.contains(e.key) } }
        .values
        .sum()
    println("solution: ${solution}")

    // part2
    val dangerList = mutableMapOf<String, String>()
    while (allergens.isNotEmpty()) {
        allergens.forEach{e ->
            if (e.value.size == 1) {
                val value = e.value.first()
                dangerList[e.key] = value
                if (allergens.isNotEmpty()) {
                    allergens.forEach { it.value.remove(value) }
                }
            }
        }
        allergens = allergens.filter { e -> e.value.isNotEmpty() }.toMutableMap()
    }
    val solutionPart2 = dangerList
        .keys
        .sorted()
        .map { dangerList[it] }
        .joinToString(separator = ",")
    println(solutionPart2)
}
