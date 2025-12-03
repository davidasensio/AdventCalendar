package days2025

import Day

fun main() {
    println(Day02.toString())
}

object Day02 : Day(2025, 2, "Gift Shop") {
    lateinit var listOfIdRanges: List<LongRange>

    override fun beforeParts() {
        listOfIdRanges = inputString.split(",").map {
            LongRange(
                start = it.split("-")[0].toLong(),
                endInclusive = it.split("-")[1].toLong()
            )
        }
    }

    override fun partOne(): Any {
        var accumulator = 0L

        logLn(listOfIdRanges)
        listOfIdRanges.forEach { range ->
            for (i in range) {
                val strNumber = i.toString()
                val length = strNumber.length
                if (length % 2 == 0) {  // Only even size numbers
                    val half = length / 2
                    val first = strNumber.take(half)
                    val last = strNumber.substring(half)
                    logLn("$first-$last")
                    if (first == last) {
                        accumulator += i
                    }
                }
            }
        }

        return accumulator
    }

    override fun partTwo(): Any {
        var accumulator = 0L
        listOfIdRanges.forEach { range ->
            loopNumber@ for (number in range) {
                val strNumber = number.toString()
                val length = strNumber.length
                for (takes in 1..<length) {
                    for (times in 1..length) {
                        if (strNumber.take(takes).repeat(times) == strNumber) {
                            accumulator += number
                            logLn(number)
                            continue@loopNumber
                        }
                    }
                }
            }
        }
        return accumulator
    }
}