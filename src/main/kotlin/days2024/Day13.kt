package days2024

import Day

fun main() {
    println(Day13.toString())
}

object Day13 : Day(2024, 13, "Claw Contraption", true) {
    private val movesInput = inputString.split("\n\n")
    private val moves = movesInput.map { Move.parse(it) }

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
            findCheapestMovesCombinationToGetThePrize(move)
        }

        println(validMoves)
        println(validMoves.filter { it.combinations.isEmpty() }.size)

        // Step 3 - Sum all the cheapest combinations
        return validMoves.filter { it.combinations.isNotEmpty() }.sumOf { it.getCheapestCombinationCost() }
    }

    // 5400 = a*34 + b*67 -> b = (8400 − (a * 94)) / 22 -> b= (prizeX - (a * incX)) / incB
    private fun findCheapestMovesCombinationToGetThePrize(move: Move) {
        repeat(MAX_ITERATIONS) { iteration ->
            val timesButtonA = iteration
            val timesButtonB = (move.prizeX - (timesButtonA * move.aX)) / move.bX

            val unitsA = (timesButtonA * move.aX) + (timesButtonB * move.bX)
            val unitsB = (timesButtonA * move.aY) + (timesButtonB * move.bY)

            if (timesButtonB >= 0) {
                if (unitsA == move.prizeX && unitsB == move.prizeY) {
                    val cost = getCost(timesButtonA, timesButtonB)

                    move.combinations.add("\"A: $timesButtonA, B: $timesButtonB" to cost)
                    println("A: $timesButtonA, B: $timesButtonB - Will cost ${timesButtonA * 3 + timesButtonB * 1} - ")
                    println(move.combinations)
                }
            }
        }
    }


    override fun partTwo(): Any {
        return 2
    }

    private fun getCost(timesButtonA: Int, timesButtonB: Int): Int =
        (timesButtonA * A_BUTTON_COST_TOKENS) + (timesButtonB * B_BUTTON_COST_TOKENS)
}

data class Move(
    val aX: Int,
    val aY: Int,
    val bX: Int,
    val bY: Int,
    val prizeX: Int,
    val prizeY: Int,
    val prizeValue: Int = 0,
    var isPrizeReachable: Boolean = true
) {
    var combinations = mutableListOf<Pair<String, Int>>()

    init {
        isPrizeReachable = isThePrizeReachable()
    }

    fun getCheapestCombinationCost(): Int {
        return combinations.first().second //.minOf { it.second }
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

    private fun isCoordReachable(value: Int, xInc: Int, yInc: Int): Boolean {
        return value % gcd(xInc, yInc) == 0
    }

    // Max common divisor by the Euclidean algorithm
    fun gcd(a: Int, b: Int): Int {
        if (b == 0) {
            return a
        }
        return gcd(b, a % b)
    }

    companion object {
        fun parse(instructions: String): Move {
            val lines = instructions.split("\n")
            val buttonA = lines[0].split(": ")[1]
            val buttonB = lines[1].split(": ")[1]
            val prize = lines[2].split(": ")[1]

            return Move(
                buttonA.split(",")[0].replace("X+", "").trim().toInt(),
                buttonA.split(",")[1].replace("Y+", "").trim().toInt(),
                buttonB.split(",")[0].replace("X+", "").trim().toInt(),
                buttonB.split(",")[1].replace("Y+", "").trim().toInt(),
                prize.split(",")[0].replace("X=", "").trim().toInt(),
                prize.split(",")[1].replace("Y=", "").trim().toInt(),
            )
        }
    }
}
