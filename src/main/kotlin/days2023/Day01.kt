package days2023

import Day

fun main() {
    println(Day01.toString())
}

object Day01 : Day(2023, 1, "Trebuchet?!") {

    override fun partOne(): Any {
        var calibrationValues = 0

        inputStringList.forEach { line ->
            val firstDigit = line.firstOrNull { it.isDigit() } ?: 0
            val lastDigit = line.lastOrNull { it.isDigit() } ?: 0
            val calibrationValue = "$firstDigit$lastDigit".toInt()
            calibrationValues += calibrationValue
        }
        return calibrationValues
    }

    override fun partTwo(): Any {
        var calibrationValues = 0
        val digitsInLetters = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        inputStringList.forEach { line ->
            val firstDigitIndex = line.indexOfFirst { it.isDigit() }
            val lastDigitIndex = line.indexOfLast { it.isDigit() }
            val firstDigitInLettersIndex = line.indexOfAny(digitsInLetters)
            val lastDigitInLettersIndex = line.lastIndexOfAny(digitsInLetters)

            val firstDigit = line.firstOrNull { it.isDigit() }
            val lastDigit = line.lastOrNull { it.isDigit() }
            val firstDigitInLetters = getFirstNumberInLetters(digitsInLetters, line)
            val lastDigitInLetters = getLastNumberInLetters(digitsInLetters, line)

            val firstNumber = when {
                firstDigitInLettersIndex == -1 -> firstDigit
                (firstDigitIndex != -1 && firstDigitIndex < firstDigitInLettersIndex) -> firstDigit
                else -> firstDigitInLetters
            }

            val lastNumber = when {
                lastDigitInLettersIndex == -1 -> lastDigit
                (lastDigitIndex != -1 && lastDigitIndex > lastDigitInLettersIndex) -> lastDigit
                else -> lastDigitInLetters
            }

            val calibrationValue = "$firstNumber$lastNumber".toInt()
            println(calibrationValue)
            calibrationValues += calibrationValue
        }
        return calibrationValues
    }

    private fun getFirstNumberInLetters(digitsInLetters: List<String>, line: String): Int? {
        val occurrences = mutableListOf<Pair<Int, Int>>() // Number and Position,
        digitsInLetters.forEachIndexed { index, digitInLetters -> // index is the number in letters
            if (line.contains(digitInLetters)) occurrences.add(Pair(index, line.indexOf(digitInLetters)))
        }
        return occurrences.sortedBy { it.second }.firstOrNull()?.first // Ordered by appearance and the get the position
    }

    private fun getLastNumberInLetters(digitsInLetters: List<String>, line: String): Int? {
        val occurrences = mutableListOf<Pair<Int, Int>>() // Number and Position,
        digitsInLetters.forEachIndexed { index, digitInLetters -> // index is the number in letters
            if (line.contains(digitInLetters)) occurrences.add(Pair(index, line.lastIndexOf(digitInLetters)))
        }
        return occurrences.sortedByDescending { it.second }
            .firstOrNull()?.first // Ordered by appearance and the get the position
    }
}


