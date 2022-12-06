package days2022

import Day
import kotlin.math.max

fun main() {
    println(Day01.toString())
}

object Day01 : Day(1, "Calorie Counting") {

    override fun partOne(): Any {
        inputString.let {
            val elves = it.split("\n\n")
            var maxCalories = 0
            val listElvesCals = mutableListOf<Int>()
            var sum: Int

            elves.forEach { elf ->
                sum = elf.split("\n").sumOf { calories -> calories.toIntOrNull() ?: 0 }
                maxCalories = max(maxCalories, sum)
                listElvesCals.add(sum)
            }
            return maxCalories
        }
    }

    override fun partTwo(): Any {
        inputString.let {
            val elves = it.split("\n\n")
            var maxCals = 0
            val listElvesCals = mutableListOf<Int>()
            var sum: Int

            elves.forEach { elf ->
                sum = elf.split("\n").sumOf { calories -> calories.toIntOrNull() ?: 0 }
                maxCals = max(maxCals, sum)
                listElvesCals.add(sum)
            }

            listElvesCals.sortDescending()
            return listElvesCals.subList(0, 3).sum()
        }
    }
}


