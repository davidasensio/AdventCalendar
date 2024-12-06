package days2024

import Day

fun main() {
    println(Day06.toString())
}

enum class Direction {
    Up, Down, Left, Right;

    fun turn(): Direction {
        return when (this) {
            Up -> Right
            Right -> Down
            Down -> Left
            Left -> Up
        }
    }
}

object Day06 : Day(2024, 6, "Guard Gallivant") {
    // Transpose the map to have X and Y coordinates as horizontal and vertical
    private val map = inputStringList.map { it.toCharArray() }.transpose()
    private val initialPosition = getInitialPosition(map)

    private const val OBSTACLE = '#'
    private const val GUARD = '^'

    override fun beforeParts() {
        println(initialPosition)
    }

    override fun partOne(): Any {
        var currentDirection = Direction.Up
        var currentPosition = initialPosition
        val distinctPositions = mutableSetOf<Pair<Int, Int>>()

        distinctPositions.add(currentPosition)

        while (!isExitingMap(currentPosition, map)) {
            val isInFrontOfObstacle = isInFrontOfObstacle(currentPosition, currentDirection, map)

            if (isInFrontOfObstacle) {
                // Turn 90º
                currentDirection = currentDirection.turn()
            } else {
                // Move forward
                currentPosition = moveForward(currentPosition, currentDirection)
                distinctPositions.add(currentPosition)
            }
        }
        return distinctPositions.size
    }

    override fun partTwo(): Any {
        val mapArea = getMapArea()
        val stuckPositions = mutableSetOf<Pair<Int, Int>>()

        // Iterate over all the map and place an obstacle in each position and see if it gets stuck
        for (i in map.indices)
            for (j in map.first().indices) {
                var counter = 0
                var currentDirection = Direction.Up
                var currentPosition = initialPosition
                val mapWithObstacle = map.map { it.copyOf() }
                mapWithObstacle[i][j] = OBSTACLE

                while (!isExitingMap(currentPosition, mapWithObstacle) && counter <= mapArea) {
                    val isInFrontOfObstacle =
                        isInFrontOfObstacle(currentPosition, currentDirection, mapWithObstacle)

                    if (isInFrontOfObstacle) {
                        // Turn 90º
                        currentDirection = currentDirection.turn()
                    } else {
                        // Move forward
                        if (++counter < mapArea) {
                            currentPosition = moveForward(currentPosition, currentDirection)
                        } else {
                            stuckPositions.add(Pair(i, j)) // Counter went too far, so it's stuck
                            break
                        }
                    }
                }
            }

        return stuckPositions.size
    }

    private fun getInitialPosition(map: List<CharArray>): Pair<Int, Int> {
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == GUARD) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1)
    }

    private fun isExitingMap(position: Pair<Int, Int>, map: List<CharArray>): Boolean {
        return position.first == 0 || position.first == map.size - 1 || position.second == 0 || position.second == map[position.first].size - 1
    }

    private fun isInFrontOfObstacle(
        currentPosition: Pair<Int, Int>,
        currentDirection: Direction,
        map: List<CharArray>
    ): Boolean {
        val x = currentPosition.first
        val y = currentPosition.second

        return when (currentDirection) {
            Direction.Up -> map[x][y - 1] == OBSTACLE
            Direction.Down -> map[x][y + 1] == OBSTACLE
            Direction.Left -> map[x - 1][y] == OBSTACLE
            Direction.Right -> map[x + 1][y] == OBSTACLE
        }
    }

    private fun moveForward(currentPosition: Pair<Int, Int>, currentDirection: Direction): Pair<Int, Int> {
        val x = currentPosition.first
        val y = currentPosition.second

        return when (currentDirection) {
            Direction.Up -> Pair(x, y - 1)
            Direction.Down -> Pair(x, y + 1)
            Direction.Left -> Pair(x - 1, y)
            Direction.Right -> Pair(x + 1, y)
        }
    }

    private fun List<CharArray>.transpose(): List<CharArray> {
        val transposed = MutableList(this[0].size) { CharArray(this.size) }
        for (i in this.indices) {
            for (j in this[i].indices) {
                transposed[j][i] = this[i][j]
            }
        }
        return transposed
    }

    private fun getMapArea() = map.size * map[0].size
}
