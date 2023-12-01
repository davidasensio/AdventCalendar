package days2022

import Day

fun main() {
    println(Day04.toString())
}

object Day04 : Day(2022, 4, "Camp Cleanup") {
    override fun partOne(): Any {
        var sum = 0

        inputStringList.forEach {
            val (elf1, elf2) = it.split(",")
            val elf1Range = elf1.split("-")[0].toInt()..elf1.split("-")[1].toInt()
            val elf2Range = elf2.split("-")[0].toInt()..elf2.split("-")[1].toInt()
            var allContained1 = true
            var allContained2 = true

            elf1Range.forEach { number ->
                if (number !in elf2Range) {
                    allContained1 = false
                }
            }
            elf2Range.forEach { number ->
                if (number !in elf1Range) {
                    allContained2 = false
                }
            }
            if (allContained1 || allContained2) sum++
        }
        return sum
    }

    override fun partTwo(): Any {
        var sum = 0

        inputStringList.forEach {
            val (elf1, elf2) = it.split(",")
            val elf1Range = elf1.split("-")[0].toInt()..elf1.split("-")[1].toInt()
            val elf2Range = elf2.split("-")[0].toInt()..elf2.split("-")[1].toInt()
            var anyContained1 = false
            var anyContained2 = false

            elf1Range.forEach { number ->
                if (number in elf2Range) {
                    anyContained1 = true
                }
            }
            elf2Range.forEach { number ->
                if (number in elf1Range) {
                    anyContained2 = true
                }
            }
            if (anyContained1 || anyContained2) sum++
        }
        return sum
    }
}
