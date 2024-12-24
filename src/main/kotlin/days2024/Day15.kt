package days2024

import Day
import days2024.Movement.*

fun main() {
    println(Day15.toString())
}

object Day15 : Day(2024, 15, "Warehouse Woes", false) {
    private const val ROBOT = '@'
    private const val BOX = 'O'
    private const val WALL = '#'
    private const val EMPTY = '.'

    private val inputData = inputString.split("\n\n")
    private val boxesMapInput: List<CharArray> = inputData[0].split("\n").map { row -> row.toCharArray() }
    private val boxesMap = Array(boxesMapInput.size) { Array(boxesMapInput.first().size) { '.' } }

    private val movesInput: List<Char> = inputData[1].filter { it != '\n' }.map { it }
    private val moves: List<Movement> = movesInput.map { Movement.fromSymbol(it) }

    override fun beforeParts() {
        for (j in boxesMapInput.indices) {
            for (i in boxesMapInput.first().indices) {
                boxesMap[i][j] = boxesMapInput[j][i]
            }
        }
    }

    override fun partOne(): Any {
        val map = boxesMap.map { it.copyOf() }.toTypedArray()

        moves.forEach { movement ->
            doMapMovement(map, Pair(ROBOT, getRobotPosition(map)), movement)
        }
        return getSumOfBoxesCoordinates(map)
    }

    override fun partTwo(): Any {
        return -2
    }

    private fun getRobotPosition(map: Array<Array<Char>>): Pair<Int, Int> {
        for (j in map.indices)
            for (i in map.first().indices) {
                if (map[i][j] == ROBOT) {
                    return Pair(i, j)
                }
            }
        throw NoSuchElementException("Robot not found")
    }

    private fun doMapMovement(
        map: Array<Array<Char>>,
        element: Pair<Char, Pair<Int, Int>>,
        movement: Movement
    ): Boolean {
        val x = element.second.first
        val y = element.second.second
        val char = element.first
        val nextElement = getNextElement(map, x, y, movement)

        logLn(getWarehouseMap(map))

        if (nextElement.first == WALL) {
            return false
        } else if (nextElement.first == EMPTY) {
            map[x][y] = EMPTY
            map[nextElement.second.first][nextElement.second.second] = char
            return true
        } else if (nextElement.first == BOX) {
            if (doMapMovement(map, nextElement, movement)) {
                doMapMovement(map, element, movement)
                return true
            }
        }
        return false
    }

    private fun getNextElement(
        map: Array<Array<Char>>,
        x: Int,
        y: Int,
        movement: Movement
    ): Pair<Char, Pair<Int, Int>> {
        fun getNextPosition(x: Int, y: Int): Pair<Int, Int> {
            val xCoerced = x.coerceIn(0 until map.first().size)
            val yCoerced = y.coerceIn(0 until map.size)
            return Pair(xCoerced, yCoerced)
        }

        val nextPosition = when (movement) {
            Up -> getNextPosition(x, y - 1)
            Down -> getNextPosition(x, y + 1)
            Left -> getNextPosition(x - 1, y)
            Right -> getNextPosition(x + 1, y)
        }

        return Pair(map[nextPosition.first][nextPosition.second], nextPosition)
    }


    private fun getWarehouseMap(map: Array<Array<Char>>): String {
        val mapStr = StringBuilder("\n")

        for (j in map.indices) {
            for (i in map.first().indices) {
                mapStr.append(map[i][j])
            }
            mapStr.append("\n")
        }
        return mapStr.toString()
    }

    private fun getSumOfBoxesCoordinates(map: Array<Array<Char>>): Long {
        var result = 0L
        for (j in map.indices)
            for (i in map.first().indices) {
                if (map[i][j] == BOX) {
                    result += (i + (j * 100L))
                }
            }
        return result
    }
}

private enum class Movement(val symbol: Char) {
    Up('^'), Down('v'), Left('<'), Right('>');

    companion object {
        fun fromSymbol(symbol: Char): Movement = values().first { it.symbol == symbol }
    }
}
