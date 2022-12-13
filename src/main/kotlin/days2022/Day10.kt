package days2022

import Day

fun main() {
    println(Day10.toString())
}

object Day10 : Day(10, "Cathode-Ray Tube", false) {
    private val instructionsPerCycle: List<Instruction>

    init {
        val instructions = inputStringList.map { line ->
            line.split(" ").let {
                when (it.size) {
                    2 -> Instruction.AddX(it.last().toInt())
                    else -> Instruction.Noop
                }
            }
        }
        instructionsPerCycle = instructions.flatMap {
            if (it == Instruction.Noop) listOf(it) else listOf(Instruction.Noop, it)
        }
    }

    override fun partOne(): Any {
        val targetCycles = listOf(20, 60, 100, 140, 180, 220)
        var sumByCycles = 0
        var X = 1

        instructionsPerCycle.forEachIndexed { index, instruction ->
            val cycle = index + 1
            logLn("X is $X at cycle $cycle")
            if (targetCycles.contains(cycle)) sumByCycles += (X * cycle)

            X += when (instruction) {
                is Instruction.AddX -> instruction.inc
                Instruction.Noop -> 0
            }
        }
        return "Signal strength during the targeted cycles $sumByCycles"
    }

    /**
     *  If the sprite is positioned such that one of its three pixels is the pixel currently being drawn,
     *  the screen produces a lit pixel (#); otherwise, the screen leaves the pixel dark (.).
     */
    override fun partTwo(): Any {
        val crtRowSize = 40
        val crt = Array(6) { CharArray(crtRowSize) { '.' } }
        var X = 1

        instructionsPerCycle.forEachIndexed { index, instruction ->
            val cycle = index + 1
            logLn("X is $X at cycle $cycle")

            val crtRow = index / crtRowSize
            val crtColumn = index % crtRowSize

            val isPixelTouchingSprite = crtColumn in X - 1..X + 1
            crt[crtRow][crtColumn] = if (isPixelTouchingSprite) '#' else '.'


            X += when (instruction) {
                is Instruction.AddX -> instruction.inc
                Instruction.Noop -> 0
            }
        }
        printCrt(crt)
        return "Look at the rendered image above to see the eight capital letters on the CRT"
    }

    private fun printCrt(crt: Array<CharArray>) {
        crt.forEach {
            it.forEach { print(it.toString().padStart(2, ' ')) }
            println()
        }
    }

    sealed class Instruction() {
        object Noop : Instruction()
        data class AddX(val inc: Int) : Instruction()
    }
}

