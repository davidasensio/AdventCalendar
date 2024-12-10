package days2024

import Day
import kotlin.math.abs

fun main() {
    println(Day08.toString())
}


object Day08 : Day(2024, 8, "Resonant Collinearity") {
    private val antennaMap = inputStringList.map { it.toCharArray() }
    private val antinodeMap = antennaMap.map { it.copyOf() }

    private val ANTINODE = '#'
    private val REGEX_ANTENNA = Regex("[A-Za-z0-9]")
    private val REGEX_ANTINODE = Regex("[$ANTINODE]")

    override fun partOne(): Any {

        antennaMap.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if ("$cell".matches(regex = REGEX_ANTENNA)) {
                    addAntinodes(cell, rowIndex, colIndex, false)
                }
            }
        }

        // printMap(antinodeMap)

        return countOnMap(antinodeMap, REGEX_ANTINODE)
    }

    override fun partTwo(): Any {
        antennaMap.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if ("$cell".matches(regex = REGEX_ANTENNA)) {
                    addAntinodes(cell, rowIndex, colIndex, true)
                }
            }
        }

        // printMap(antinodeMap)

        return countOnMap(antinodeMap, REGEX_ANTINODE)
    }

    private fun addAntinodes(
        cellSource: Char,
        rowIndexSource: Int,
        colIndexSource: Int,
        withHarmonics: Boolean = false,
    ) {
        antennaMap.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if (rowIndex != rowIndexSource && colIndex != colIndexSource) {
                    if (cell == cellSource) {
                        val colDistance = abs(colIndex - colIndexSource)
                        val rowDistance = abs(rowIndex - rowIndexSource)
                        var targetCell = Pair<Int, Int>(-1, -1)
                        var isInside = true
                        var iteration = 0

                        do {
                            val colDistanceIteration = colDistance * if (withHarmonics) iteration else 1
                            val rowDistanceIteration = rowDistance * if (withHarmonics) iteration else 1
                            iteration++

                            if (rowIndex <= rowIndexSource) {
                                if (colIndex <= colIndexSource) {
                                    targetCell = Pair(
                                        rowIndex - rowDistanceIteration,
                                        colIndex - colDistanceIteration
                                    )
                                } else {
                                    targetCell = Pair(
                                        rowIndex - rowDistanceIteration,
                                        colIndex + colDistanceIteration
                                    )
                                }
                            } else {
                                if (colIndex <= colIndexSource) {
                                    targetCell = Pair(
                                        rowIndex + rowDistanceIteration,
                                        colIndex - colDistanceIteration
                                    )
                                } else {
                                    targetCell = Pair(
                                        rowIndex + rowDistanceIteration,
                                        colIndex + colDistanceIteration
                                    )
                                }
                            }
                            if (targetCell.first >= 0 && targetCell.first < antinodeMap.size && targetCell.second >= 0 && targetCell.second < antinodeMap[0].size) {
                                antinodeMap[targetCell.first][targetCell.second] = ANTINODE
                            } else {
                                isInside = false
                            }
                        } while (isInside && withHarmonics)
                    }
                }
            }
        }
    }

    private fun countOnMap(map: List<CharArray>, regex: Regex) = map.sumOf { row -> row.count { "$it".matches(regex) } }

    private fun printMap(map: List<CharArray>) {
        map.forEach { row ->
            println(row.joinToString(separator = ""))
        }
    }
}
