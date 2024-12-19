package days2024

import Day
import kotlin.math.roundToLong

fun main() {
    println(Day13.toString())
}

object Day13 : Day(2024, 13, "Claw Contraption", true) {
    private val movesInput = inputString.split("\n\n")
    private val moves = movesInput.map { Move.parse(it) }
    private val movesWithOffset = movesInput.map { Move.parse(it, addOffset = true) }

    private const val MAX_ITERATIONS = 100
    private const val A_BUTTON_COST_TOKENS = 3
    private const val B_BUTTON_COST_TOKENS = 1

    override fun beforeParts() {
        logLn("Total moves: ${moves.size} -> " + moves.toString())
    }

    override fun partOne(): Any {
        // Step 1 - Just take the valid moves
        val validMoves = moves.filter { it.isPrizeReachable }

        // Step 2 - Calculate different combinations (Up to 100) for a and b and look for the cheaper one.
        validMoves.forEach { move ->
            findCheapestMovesCombinationWithIterations(move)
        }

        logLn("Part 1: " + validMoves.filter { it.combinations.isNotEmpty() }.map { it.combinations.toString() })
        // Step 3 - Sum all the cheapest combinations
        return validMoves.filter { it.combinations.isNotEmpty() }.sumOf { it.getCheapestCombinationCost() }
    }

    override fun partTwo(): Any {
        // Step 1 - Just take the valid moves
        val validMoves = movesWithOffset.filter { it.isPrizeReachable }

        // Step 2 - Calculate different combinations (Up to 100) for a and b and look for the cheaper one.
        validMoves.forEach { move ->
            findCheapestMovesCombinationWithMaths(move)
        }

        logLn("Part 2: " + validMoves.filter { it.combinations.isNotEmpty() }.map { it.combinations.toString() })

        // Step 3 - Sum all the cheapest combinations
        return validMoves.filter { it.combinations.isNotEmpty() }.sumOf { it.getCheapestCombinationCost() }
    }


    private fun findCheapestMovesCombinationWithIterations(move: Move, iterations: Int = MAX_ITERATIONS) {
        repeat(iterations) { iteration ->
            val timesButtonA = iteration.toLong()
            val timesButtonB = (move.prizeX - (timesButtonA * move.aX)) / move.bX
            // 5400 = a*34 + b*67 -> b = (8400 − (a * 94)) / 22 -> b= (prizeX - (a * incX)) / incB

            val unitsA = (timesButtonA * move.aX) + (timesButtonB * move.bX)
            val unitsB = (timesButtonA * move.aY) + (timesButtonB * move.bY)

            if (timesButtonB >= 0) {
                if (unitsA == move.prizeX && unitsB == move.prizeY) {
                    val cost = getCost(timesButtonA, timesButtonB)

                    move.combinations.add("A: $timesButtonA, B: $timesButtonB" to cost)
                }
            }
        }
    }

    private fun findCheapestMovesCombinationWithMaths(move: Move) {
        val ax = move.aX
        val ay = move.aY
        val bx = move.bX
        val by = move.bY
        val px = move.prizeX
        val py = move.prizeY

        /**
         *  a * ax + b * bx == px
         *  a == (px - b * bx) / ax
         *  a * ay + b * by == py
         *  ((px - b * bx) / ax) * ay + b * by == py
         *  b == (py - (px*ay/ax)) / (by - bx*ay/ax)
         */

        val b = ((py - ((px * ay) / ax.toDouble())) / (by - (bx * ay) / ax.toDouble())).roundToLong()
        val a = ((px - b * bx) / ax.toDouble()).roundToLong()

        if ((a * ax + b * bx == px) && (a * ay + b * by == py)) {
            move.combinations.add("A: $a, B: $b" to getCost(a, b))
        }
    }

    private fun getCost(timesButtonA: Long, timesButtonB: Long): Long =
        (timesButtonA * A_BUTTON_COST_TOKENS) + (timesButtonB * B_BUTTON_COST_TOKENS)
}

data class Move(
    val aX: Long,
    val aY: Long,
    val bX: Long,
    val bY: Long,
    val prizeX: Long,
    val prizeY: Long,
    val prizeValue: Long = 0,
    var isPrizeReachable: Boolean = true
) {

    var combinations = mutableListOf<Pair<String, Long>>()

    init {
        isPrizeReachable = isThePrizeReachable()
    }

    fun getCheapestCombinationCost(): Long {
        return combinations.first().second
    }

    private fun isThePrizeReachable(): Boolean {
        return isPrizeCoordXReachable() && isPrizeCoordYReachable()
    }

    private fun isPrizeCoordXReachable(): Boolean {
        return isCoordReachable(prizeX, aX, bX)
    }

    private fun isPrizeCoordYReachable(): Boolean {
        return isCoordReachable(prizeY, aY, bY)
    }

    private fun isCoordReachable(value: Long, coordIncA: Long, coordIncB: Long): Boolean {
        return value % gcd(coordIncA, coordIncB) == 0L
    }

    // Max common divisor by the Euclidean algorithm
    fun gcd(a: Long, b: Long): Long {
        if (b == 0L) {
            return a
        }
        return gcd(b, a % b)
    }

    companion object {
        private const val OFFSET: Long = 10000000000000
        fun parse(instructions: String, addOffset: Boolean = false): Move {
            val lines = instructions.split("\n")
            val buttonA = lines[0].split(": ")[1]
            val buttonB = lines[1].split(": ")[1]
            val prize = lines[2].split(": ")[1]
            val offset = if (addOffset) OFFSET else 0

            return Move(
                buttonA.split(",")[0].replace("X+", "").trim().toLong(),
                buttonA.split(",")[1].replace("Y+", "").trim().toLong(),
                buttonB.split(",")[0].replace("X+", "").trim().toLong(),
                buttonB.split(",")[1].replace("Y+", "").trim().toLong(),
                (prize.split(",")[0].replace("X=", "").trim().toLong() + offset),
                (prize.split(",")[1].replace("Y=", "").trim().toLong() + offset),
            )
        }
    }
}
