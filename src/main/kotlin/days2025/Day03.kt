package days2025

import Day

fun main() {
    println(Day03.toString())
}

object Day03 : Day(2025, 3, "Lobby", debug = true) {

    override fun beforeParts() {}

    override fun partOne(): Any {
        var sumJolts = 0L

        inputStringList.forEach { battery ->
            val maxFirst = battery.take(battery.length - 1).max()
            val indexOfMaxFirst = battery.take(battery.length - 1).indexOf(maxFirst)
            val maxSecond = battery.substring(indexOfMaxFirst + 1).max()

            val result = "$maxFirst$maxSecond"
            sumJolts += result.toInt()
        }

        return sumJolts
    }

    override fun partTwo(): Any {
        val maxDigits = 12
        var sumJolts = 0L

        inputStringList.forEach { battery ->
            val batteryLength = battery.length
            var maxJumps = batteryLength - maxDigits


            val maxFirst = battery.take(battery.length - (maxDigits - 1)).max()
            val indexOfMaxFirst = battery.take(battery.length - (maxDigits - 1)).indexOf(maxFirst)
            var remaining = battery.substring(indexOfMaxFirst)
            val numbersToPick = remaining.length - maxDigits
            for (i in 0..<numbersToPick) {
                val minOfRemaining = remaining.min().toString()
                remaining = remaining.replaceFirst(minOfRemaining, "")
            }

            val result = remaining.toLong()
            logLn(result)
            sumJolts += result
        }
        return sumJolts //167821353500203 too low
    }
}