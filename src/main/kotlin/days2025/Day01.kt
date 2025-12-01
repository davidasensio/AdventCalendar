package days2025

import Day
import kotlin.math.abs

fun main() {
    println(Day01.toString())
}

object Day01 : Day(2025, 1, "Secret Entrance") {
    private val list1 = mutableListOf<Int>()

    override fun beforeParts() {
        inputStringList.forEach { line ->
            list1.add(line.replace("L", "-").replace("R", "+").toInt())
        }
    }

    override fun partOne(): Any {
        var dialValue = 50
        var zeros = 0

        try {
            list1.toList().forEach {
                val inc = it % 100
                val sum = dialValue + inc

                dialValue = if (sum >= 100) sum - 100
                else if (sum < 0) 100 + sum
                else sum
                if (dialValue == 0) {
                    zeros++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return zeros
    }

    override fun partTwo(): Any {
        var dialValue = 50
        var zerosClicks = 0

        try {
            list1.toList().forEach {
                val inc = it % 100
                val sum = dialValue + inc

                zerosClicks += abs(it / 100)

                dialValue = if (sum >= 100) {
                    if (dialValue != 0) zerosClicks++
                    sum - 100
                } else if (sum < 0) {
                    if (dialValue != 0) zerosClicks++
                    100 + sum
                } else {
                    if (sum == 0) zerosClicks++
                    sum
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return zerosClicks
    }
}


