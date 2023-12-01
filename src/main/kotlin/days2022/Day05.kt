package days2022

import Day

fun main() {
    println(Day05.toString())
}

object Day05 : Day(2022, 5, "Supply Stacks") {
    private const val MOVES_SEPARATOR = "\n\n"
    private const val POSITION_SEPARATOR = "\n 1"
    private const val CRATE_LENGTH = 4

    override fun partOne(): Any {
        val (stacks, movesBlock) = inputString.split(MOVES_SEPARATOR)
        val stacksRows = stacks.split(POSITION_SEPARATOR)[0].split("\n")

        // println(stacksRows)
        // println("==============")
        // println(movesBlock)

        val stackNumber = stacksRows.first().length / 3
        // println("Stacks number = $stackNumber")

        val arrayCrates = Array(stackNumber) { mutableListOf<String>() }
        for (index in stacksRows.lastIndex downTo 0) {
            val chunked = stacksRows[index].chunked(CRATE_LENGTH)
            repeat(chunked.size) { repeatIndex ->
                chunked[repeatIndex].let { crate ->
                    // println(crate)
                    if (crate.isNotBlank()) {
                        arrayCrates[repeatIndex].add(crate)
                    }
                }
            }
        }

        // printCrateStacks(arrayCrates)
        // println(arrayCrates.toString())

        // Start movements
        var result = arrayCrates
        // println("Movements:")
        val moveRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

        movesBlock.split("\n").forEach {
            if (it.isNotBlank()) {
                val (number, from, to) = moveRegex.find(it)!!.destructured
                result = moveCrates(number.toInt(), from.toInt(), to.toInt(), arrayCrates)
                // printCrateStacks(result)
            }
        }
        // printCrateStacks(result)

        val cratesOnTop = result.map { it.lastOrNull() }
        return "CratesOnTop = $cratesOnTop"
    }

    override fun partTwo(): Any {
        val (stacks, movesBlock) = inputString.split(MOVES_SEPARATOR)
        val stacksRows = stacks.split(POSITION_SEPARATOR)[0].split("\n")

        // println(stacksRows)
        // println("==============")
        // println(movesBlock)

        val stackNumber = stacksRows.first().length / 3
        // println("Stacks number = $stackNumber")

        val arrayCrates = Array(stackNumber) { mutableListOf<String>() }
        for (index in stacksRows.lastIndex downTo 0) {
            val chunked = stacksRows[index].chunked(CRATE_LENGTH)
            repeat(chunked.size) { repeatIndex ->
                chunked[repeatIndex].let { crate ->
                    // println(crate)
                    if (crate.isNotBlank()) {
                        arrayCrates[repeatIndex].add(crate)
                    }
                }
            }
        }

        // printCrateStacks(arrayCrates)
        // println(arrayCrates.toString())

        // Start movements
        var result = arrayCrates
        // println("Movements:")
        val moveRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

        movesBlock.split("\n").forEach {
            if (it.isNotBlank()) {
                val (number, from, to) = moveRegex.find(it)!!.destructured
                // println("This: $number, $from, $to")
                result = moveCratesInBlock(number.toInt(), from.toInt(), to.toInt(), arrayCrates)
                // printCrateStacks(result)
            }
        }
        // printCrateStacks(result)

        val cratesOnTop = result.map { it.lastOrNull() }
        return "CratesOnTop = $cratesOnTop"
    }

    private fun moveCrates(
        number: Int,
        fromStack: Int,
        toStack: Int,
        arrayCrates: Array<MutableList<String>>
    ): Array<MutableList<String>> {
        repeat(number) {
            val source = arrayCrates[fromStack - 1]
            val element = source.removeAt(source.lastIndex)
            val target = arrayCrates[toStack - 1]
            target.add(element)
        }
        return arrayCrates
    }

    private fun moveCratesInBlock(
        number: Int,
        fromStack: Int,
        toStack: Int,
        arrayCrates: Array<MutableList<String>>
    ): Array<MutableList<String>> {
        val block = mutableListOf<String>()
        repeat(number) {
            val source = arrayCrates[fromStack - 1]
            val element = source.removeAt(source.lastIndex)
            block.add(element)
        }
            val target = arrayCrates[toStack - 1]
            target.addAll(block.reversed())
        return arrayCrates
    }

    private fun printCrateStacks(arrayCrates: Array<MutableList<String>>) {
        arrayCrates.forEach {
            println(it)
        }
    }
}
