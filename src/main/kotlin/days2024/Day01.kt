package days2024

import Day
import kotlin.math.abs

fun main() {
    println(Day01.toString())
}

object Day01 : Day(2024, 1, "Historian Hysteria") {
    private val list1 = mutableListOf<Int>()
    private val list2 = mutableListOf<Int>()

    override fun beforeParts() {
        inputStringList.forEach { line ->
            val pairs = line.split(regex = "\\s+".toRegex())
            list1.add(pairs.first().toInt())
            list2.add(pairs.last().toInt())
        }
    }

    override fun partOne(): Any {
        var resultSumDiff = 0

        val list1Sorted = list1.sorted()
        val list2Sorted = list2.sorted()

        list1Sorted.forEachIndexed { index, i ->
            resultSumDiff += abs(list1Sorted[index] - list2Sorted[index])
        }

        return resultSumDiff
    }

    override fun partTwo(): Any {
        var similarityScore = 0

        list1.forEach { number ->
            similarityScore += number * list2.count { it == number }
        }
        return similarityScore
    }
}


