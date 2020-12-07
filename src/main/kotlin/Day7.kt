package main

import com.google.common.graph.MutableValueGraph
import com.google.common.graph.ValueGraphBuilder
import java.io.File

fun main() {
    val graph = ValueGraphBuilder.directed().build<String, Int>()
    File("inputs/Day7").forEachLine {
        val (subjectBag, children) = it.split(" contain ")
        val subject = subjectBag.replace("bags", "bag")
        graph.addNode(subject)
        if (children == "no other bags.") {
            return@forEachLine
        }
        children.split(", ").forEach { s ->
            val count = s.first().toString().toInt()
            val child = s.replace(".", "").replace("bags", "bag").substring(2)
            graph.putEdgeValue(subject, child, count)
        }
    }
    println("predecessors: ${getPredecessors(graph, "shiny gold bag").count()}")
    println("total Bags: ${getTotalBags(graph, "shiny gold bag")}")
}

fun getPredecessors(graph: MutableValueGraph<String, Int>?, node: String): Set<String> {
    val predecessors = graph!!.predecessors(node)
    if (predecessors.isEmpty()) {
        return setOf()
    }
    val result = mutableSetOf<String>()
    result.addAll(predecessors)
    predecessors.forEach { p -> result.addAll(getPredecessors(graph, p)) }
    return result
}

fun getTotalBags(graph: MutableValueGraph<String, Int>?, node: String): Int {
    val successors = graph!!.successors(node)
    if (successors.isEmpty()) {
        return 0
    }
    var result = 0
    successors.forEach { s ->
        run {
            val edgeValue = graph.edgeValue(node, s).get();
            result += edgeValue + edgeValue * getTotalBags(graph, s);
        }
    }
    return result
}
