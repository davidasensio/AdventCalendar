package days2022

import Day

fun main() {
    println(Day02.toString())
}

object Day02 : Day(2022, 2, "Rock Paper Scissors") {

    override fun partOne(): Any {
        var totalScore = 0

        inputStringList.forEach {
            if (it.isNotEmpty()) {
                // A, B, C -> Rock, Paper, Scissors
                // X, Y, Z -> Rock, Paper, Scissors

                val score = when (it) {
                    "A X" -> 1 + 3
                    "B Y" -> 2 + 3
                    "C Z" -> 3 + 3

                    "A Y" -> 2 + 6
                    "B Z" -> 3 + 6
                    "C X" -> 1 + 6

                    "A Z" -> 3 + 0
                    "B X" -> 1 + 0
                    "C Y" -> 2 + 0

                    else -> 0
                }

                totalScore += score
            }
        }
        return "Total score is: $totalScore"
    }


    override fun partTwo(): Any {
        var totalScore = 0

        inputStringList.forEach {
            if (it.isNotEmpty()) {
                val (enemy, matchResult) = it.split(" ")

                // A, B, C -> Rock, Paper, Scissors
                // X, Y, Z -> Rock, Paper, Scissors

                // X to Lose, Y to Draw, Z to Win

                val score = when (matchResult) {
                    "X" -> when (enemy) {
                        "A" -> 3 // Z
                        "B" -> 1 // X
                        "C" -> 2 // Y
                        else -> 0
                    }

                    "Y" -> when (enemy) {
                        "A" -> 1 + 3 // X
                        "B" -> 2 + 3 // Y
                        "C" -> 3 + 3 // Z
                        else -> 0
                    }

                    "Z" -> when (enemy) {
                        "A" -> 2 + 6 // Y
                        "B" -> 3 + 6 // Z
                        "C" -> 1 + 6 // X
                        else -> 0
                    }

                    else -> 0
                }

                totalScore += score
            }
        }
        return "Total score is: $totalScore"
    }
}


