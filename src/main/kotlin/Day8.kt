package main

import java.io.File

fun main() {
    val instructions = read()
    runProgram(instructions)
    instructions.forEach{i ->
        val alteredList = alterInstruction(instructions, i)
        if (alteredList != instructions) {
            runProgram(alteredList)
        } else {
            println("not altered")
        }

    }
}

fun alterInstruction(instructions: List<Instruction>, i: Instruction): List<Instruction> {
    if (i.isA("acc")) return instructions

    val newInstructions = mutableListOf<Instruction>()
    newInstructions.addAll(instructions)
    if (i.isA("nop")) newInstructions[instructions.indexOf(i)] = Instruction("jmp", i.amount)
    if (i.isA("jmp")) newInstructions[instructions.indexOf(i)] = Instruction("nop", i.amount)
    return newInstructions
}

fun runProgram(instructions: List<Instruction>) {
    var pointer = 0
    val seenInstructions = mutableListOf<Instruction>()
    var accValue = 0
    while (pointer < instructions.size && !seenInstructions.contains(instructions.get(pointer))) {
        val instruction = instructions.get(pointer)
        seenInstructions.add(instruction)
        if (instruction.isA("nop")) {
            pointer++
        } else if (instruction.isA("acc")) {
            accValue += instruction.amount
            pointer++
        } else if (instruction.isA("jmp")) {
            pointer += instruction.amount
        }
    }
    if (pointer >= instructions.size) {
        println("(complete) pointer: $pointer, value: $accValue")
    } else {
        println("pointer: $pointer, value: $accValue")
    }
}

fun read(): List<Instruction> {
    val result = mutableListOf<Instruction>()

    File("inputs/Day8").forEachLine{
        val (command, amount) = it.split(' ')
        result.add(Instruction(command, amount.toInt()))
    }

    return result
}

class Instruction(val command: String, val amount: Int) {
    override fun toString(): String {
        return "Instruction(command='$command', amount=$amount)"
    }

    fun isA(s: String): Boolean {
        return command == s
    }
}
