package days2022

import Day
import java.awt.Point
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.max

fun main() {
    println(Day09.toString())
}

object Day09 : Day(9, "Rope Bridge", false) {
    private const val START = 's'
    private const val HEAD_VISIT = 'H'
    private const val TAIL_VISIT = 'T'
    private const val MAP_SIZE = 700
    private val ropeMap = Array(MAP_SIZE) { CharArray(MAP_SIZE) { '.' } }

    private enum class Direction { L, R, U, D }
    private data class Move(val direction: Direction, val times: Int)

    private val moves: List<Move> = inputStringList.map {
        val (direction, times) = it.split(" ")
        Move(Direction.valueOf(direction), times.toInt())
    }

    override fun partOne(): Any {
        val startX = MAP_SIZE / 2
        val startY = MAP_SIZE / 2
        var hX = startX
        var hY = startY
        var tX = startX
        var tY = startY
        var maxSizeX = 0
        var maxSizeY = 0

        ropeMap[hY][hX] = START
        val movesSize = moves.size
        moves.forEachIndexed { index, move ->
            repeat(move.times) {
                when (move.direction) {
                    Direction.L -> hX--
                    Direction.R -> hX++
                    Direction.U -> hY--
                    Direction.D -> hY++
                }
                if (!isHeadAdjacentToTailInMap(Point(hX, hY), Point(tX, tY))) {
                    if (hY == tY) { // Same row
                        if (hX > tX) tX++ else tX--
                    } else if (hX == tX) { // Same column
                        if (hY > tY) tY++ else tY--
                    } else {
                        if (abs(hY - tY) > 1) { // Two rows of difference
                            tX = hX
                            if (hY > tY) tY++ else tY--
                        } else { // Two columns of difference
                            tY = hY
                            if (hX > tX) tX++ else tX--
                        }
                    }
                }

                // ropeMap[hY][hX] = HEAD_VISIT
                ropeMap[tY][tX] = TAIL_VISIT
                logLn(getRopeMapAsString())
                logLn()
                // println("Move: ${move.direction} ${move.times}")
            }
            maxSizeX = max(hX,maxSizeX)
            maxSizeY = max(hY,maxSizeY)

            println("Progress: $index / $movesSize (Current size = $maxSizeX x $maxSizeY)")
        }
        println(getRopeMapAsString())
        val tailVisits = countTailVisits()
        return "Total position Tail visited are $tailVisits"
    }

    override fun partTwo(): Any {
        return false
    }

    private fun countTailVisits() = ropeMap.sumOf {
        it.count { character -> character == TAIL_VISIT }
    }

    private fun isHeadAdjacentToTailInMap(head: Point, tail: Point): Boolean {
        return abs(head.x - tail.x) <= 1 && abs(head.y - tail.y) <= 1
    }

    private fun getRopeMapAsString(): String {
        val result = StringBuilder()
        ropeMap.forEach {
            it.forEach { character -> result.append(character.toString().padStart(2, ' ')) }
            result.append("\n")
        }
        return result.toString()
    }
}
