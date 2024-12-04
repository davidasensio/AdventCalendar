package days2024

import Day

fun main() {
    println(Day04.toString())
}

object Day04 : Day(2024, 4, "Ceres Search") {
    private const val XMAS = "XMAS"
    private val xmasArray = inputStringList.map { it.toCharArray() }

    override fun partOne(): Any {
        val xmasHorizontalLines = xmasArray.map { String(it) }
        val xmasVerticalLines = transpose(xmasArray).map { String(it) }
        val xmasDiagonalLines = getDiagonalLines(xmasArray)

        val horizontalCount = xmasHorizontalLines.sumOf { countWordOccurrences(it, XMAS, true) }
        val verticalCount = xmasVerticalLines.sumOf { countWordOccurrences(it, XMAS, true) }
        val diagonalCount = xmasDiagonalLines.sumOf { countWordOccurrences(it, XMAS, true) }

        return horizontalCount + verticalCount + diagonalCount
    }

    override fun partTwo(): Any {
        var xmasCrossedCount = 0
        for (i in 1 until xmasArray.size - 1) {
            for (j in 1 until xmasArray[i].size - 1) {
                if (xmasArray[i][j] == 'A') {
                    val isMASForward = xmasArray[i - 1][j - 1] == 'M' && xmasArray[i + 1][j + 1] == 'S'
                    val isMASBackward = xmasArray[i - 1][j + 1] == 'M' && xmasArray[i + 1][j - 1] == 'S'
                    val isSAMForward = xmasArray[i - 1][j - 1] == 'S' && xmasArray[i + 1][j + 1] == 'M'
                    val isSAMBackward = xmasArray[i - 1][j + 1] == 'S' && xmasArray[i + 1][j - 1] == 'M'
                    val crossFound = listOf(isMASForward, isMASBackward, isSAMForward, isSAMBackward).count { it } == 2
                    if (crossFound) {
                        xmasCrossedCount++
                    }
                }
            }
        }
        return xmasCrossedCount
    }

    private fun countWordOccurrences(line: String, word: String, alsoCheckReversed: Boolean = true): Int {
        val count = word.toRegex().findAll(line).count()
        val countReversed = if (alsoCheckReversed) word.toRegex().findAll(line.reversed()).count() else 0
        return count + countReversed
    }

    private fun transpose(matrix: List<CharArray>): List<CharArray> {
        val transposed = MutableList(matrix[0].size) { CharArray(matrix.size) }
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                transposed[j][i] = matrix[i][j]
            }
        }
        return transposed
    }

    // For the code of this function I was helped by Copilot
    private fun getDiagonalLines(xmasArrayMatrix: List<CharArray>, minLength: Int = XMAS.length): List<String> {
        val diagonals = mutableListOf<String>()

        // Top-left to bottom-right diagonals
        for (k in 0 until xmasArrayMatrix.size + xmasArrayMatrix[0].size - 1) {
            val diagonal = StringBuilder()
            for (j in 0..k) {
                val i = k - j
                if (i < xmasArrayMatrix.size && j < xmasArrayMatrix[0].size) {
                    diagonal.append(xmasArrayMatrix[i][j])
                }
            }
            if (diagonal.isNotEmpty() && diagonal.length >= minLength) {
                diagonals.add(diagonal.toString())
            }
        }

        // Top-right to bottom-left diagonals
        for (k in 0 until xmasArrayMatrix.size + xmasArrayMatrix[0].size - 1) {
            val diagonal = StringBuilder()
            for (j in 0..k) {
                val i = k - j
                if (i < xmasArrayMatrix.size && j < xmasArrayMatrix[0].size) {
                    diagonal.append(xmasArrayMatrix[i][xmasArrayMatrix[0].size - 1 - j])
                }
            }
            if (diagonal.isNotEmpty() && diagonal.length >= minLength) {
                diagonals.add(diagonal.toString())
            }
        }

        return diagonals
    }
}
