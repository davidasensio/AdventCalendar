package days2022

import Day

fun main() {
    println(Day03.toString())
}

object Day03 : Day(3, "Rucksack Reorganization") {

    override fun partOne(): Any {
        var sum = 0
        inputStringList.forEach { rucksack ->
            val (comp1, comp2) = splitLine(rucksack)
            val repeatedLetter = comp1.toCharArray().intersect(comp2.toCharArray().toList().toSet())
            val repeatedLetterPriority = getLetterPriority(repeatedLetter.first())
            // println("Repeated letter priority is $repeatedLetterPriority")
            sum += repeatedLetterPriority
        }

        return "Total is $sum"
    }


    override fun partTwo(): Any {
        val total = arrayListOf(0, 0, 0)

        val chunkedList = inputStringList.chunked(3)
        chunkedList.forEachIndexed { index, strings ->
            val inter1 = strings.first().toCharArray().intersect(strings.last().toCharArray().toList().toSet())
            val intersection = inter1.toCharArray().intersect(strings[1].toCharArray().toList().toSet())
            total[index % 3] += getLetterPriority(intersection.first())
        }

        return "Total is ${total.sum()}"
    }

    private fun splitLine(line: String): Pair<String, String> =
        Pair(line.substring(0, line.length / 2), line.substring(line.length / 2))

    private fun getLetterPriority(char: Char): Int {
        val lowerStart = 97
        val upperStart = 65
        val inc = if (char.isUpperCase()) 27 else 1
        val dec = if (char.isUpperCase()) upperStart else lowerStart
        return char.code - dec + inc
    }
}


