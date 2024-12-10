package days2024

import Day

fun main() {
    println(Day07.toString())
}

enum class Operators {
    Sum, Mul
}

enum class Operators2 {
    Sum, Mul, Or
}

private const val PART1_CALC_ITERATION_DEEP = 20000
private const val PART2_CALC_ITERATION_DEEP = 1500000

object Day07 : Day(2024, 7, "Bridge Repair") {
    private val calibrationEquations = inputStringList.map { equation ->
        equation.split(":")[0].toLong() to equation.split(":")[1].trim().split(" ").map { it.toLong() }
    }

    override fun beforeParts() {
        println(calibrationEquations)
    }

    override fun partOne(): Any {
        val validResults = mutableSetOf<Long>()

        calibrationEquations.forEach {
            val result = it.first
            val terms = it.second

            // Solved by brute force
            repeat(PART1_CALC_ITERATION_DEEP) {
                var value: Long = 0

                terms.forEachIndexed { index, term ->
                    val operator = Operators.values().random()
                    if (index < terms.size) {
                        when (operator) {
                            Operators.Sum -> value += term
                            Operators.Mul -> value *= term
                        }
                    }
                }

                if (value == result) {
                    validResults.add(result)
                    return@repeat
                }
            }
        }
        println("Part One - Total Valid results: ${validResults.size} - $validResults")
        return validResults.sumOf { it }
    }

    override fun partTwo(): Any {
        val validResults = mutableSetOf<Long>()
        calibrationEquations.forEach {
            val result = it.first
            val terms = it.second

            // Solved by brute force
            repeat(PART2_CALC_ITERATION_DEEP) {
                var value: Long = 0

                terms.forEachIndexed { index, term ->
                    val operator = Operators2.values().random()
                    if (index < terms.size) {
                        when (operator) {
                            Operators2.Sum -> value += term
                            Operators2.Mul -> value *= term
                            Operators2.Or -> value = (value.toString() + term.toString()).toLong()
                        }
                    }
                }

                if (value == result) {
                    validResults.add(result)
                    return@repeat
                }
            }
        }
        println("Part Two - Total Valid results: ${validResults.size} - $validResults")
        return validResults.sumOf { it }
    }
}
