package days2024

import Day
import java.io.BufferedWriter
import java.io.FileWriter
import java.lang.StringBuilder

fun main() {
    println(Day14.toString())
}


private const val WIDTH = 101
private const val HEIGHT = 103

object Day14 : Day(2024, 14, "Restroom Redoubt", true) {
    private const val ROBOT_REGEX = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)"
    private const val SECONDS = 100

    private val robots = inputStringList.map {
        with(Regex(ROBOT_REGEX).matchEntire(it)!!.destructured.toList().map { values -> values.toInt() }) {
            Robot(
                position = Pair(this[0], this[1]),
                vX = this[2],
                vY = this[3]
            )
        }
    }

    override fun beforeParts() {
        logLn(robots)
    }

    override fun partOne(): Any {
        // Step 1 - Elapse the time seconds
        val robotsLater = getRobotsAfterSeconds(SECONDS)
        println(getMapOfRobots(robotsLater))

        // Step 2 - Count the number of robots per quadrant (Remove middle row and column)
        val robotsByQuadrant = Quadrant.values().map { getRobotsByQuadrant(robotsLater, it) }.toList()
        return robotsByQuadrant.reduce { acc, count -> acc * count }
    }

    override fun partTwo(): Any {
        // Step 0 - Write to file the maps of robots
        val fileWriter = FileWriter("./output.txt")
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write("Maps of Robots!\n")

        // Step 1 - Elapse the time second by second until find a map in a Christmas tree shape
        val secondsToTry = 10000

        repeat(secondsToTry) {
            val robotsLater = getRobotsAfterSeconds(it)
            if (hasMapOfRobotsSeveralRobotsTogether(robotsLater)) {
                bufferedWriter.write(getMapOfRobots(robotsLater))
                bufferedWriter.write("That was iteration number ${it}")

                println(getMapOfRobots(robotsLater))
                println("That was iteration number $it")
            }
        }

        bufferedWriter.close()

        return -2
    }

    private fun getRobotsByQuadrant(robots: List<Robot>, quadrant: Quadrant): Long {
        return robots.count {
            it.position.first >= quadrant.limit.first.first &&
                    it.position.first <= quadrant.limit.second.first &&
                    it.position.second >= quadrant.limit.first.second &&
                    it.position.second <= quadrant.limit.second.second
        }.toLong()
    }

    private fun getRobotsAfterSeconds(seconds: Int) = robots.map { robot ->
        val coordX = (robot.position.first + robot.vX * seconds) % WIDTH
        val coordY = (robot.position.second + robot.vY * seconds) % HEIGHT
        robot.copy(
            position = Pair(
                if (coordX >= 0) coordX else WIDTH + coordX,
                if (coordY >= 0) coordY else HEIGHT + coordY,
            )
        )
    }

    private fun getMapOfRobots(robotsLater: List<Robot>): String {
        val mapOfRobots = StringBuilder("\n")

        for (j in 0 until HEIGHT) {
            for (i in 0 until WIDTH) {
                if (j == (HEIGHT / 2) || i == (WIDTH / 2)) {
                    mapOfRobots.append(" ")
                } else if (robotsLater.any { it.position == Pair(i, j) }) {
                    mapOfRobots.append(robotsLater.count { it.position == Pair(i, j) })
                } else {
                    mapOfRobots.append(".")
                }
            }
            mapOfRobots.append("\n")
        }
        return mapOfRobots.toString()
    }

    private fun hasMapOfRobotsSeveralRobotsTogether(robots: List<Robot>): Boolean {
        return robots.filter { it.position.first ==50 }.groupBy { it.position.second }.map { it.value.size }.sortedDescending().size > 23
    }
}

data class Robot(val position: Pair<Int, Int>, val vX: Int, val vY: Int)

enum class Quadrant(val limit: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
    ONE(
        limit = Pair(
            Pair(0, 0),
            Pair((WIDTH / 2) - 1, (HEIGHT / 2) - 1)
        )
    ),
    TWO(
        limit = Pair(
            Pair((WIDTH / 2) + 1, 0),
            Pair(WIDTH, (HEIGHT / 2) - 1)
        )
    ),
    THREE(
        limit = Pair(
            Pair(0, (HEIGHT / 2) + 1),
            Pair((WIDTH / 2) - 1, HEIGHT)
        )
    ),
    FOUR(
        limit = Pair(
            Pair((WIDTH / 2) + 1, (HEIGHT / 2) + 1),
            Pair(WIDTH, HEIGHT)
        )
    )
}
