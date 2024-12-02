package days2024

import Day
import kotlin.math.abs

fun main() {
    println(Day02.toString())
}

object Day02 : Day(2024, 2, "Red-Nosed Reports") {
    private val redNosedReports = mutableListOf<List<Int>>()

    override fun beforeParts() {
        inputStringList.forEach { line ->
            val numberLevels = line.split(regex = "\\s+".toRegex())
            redNosedReports.add(numberLevels.map { it.toInt() })
        }
    }

    override fun partOne(): Any {
        var safeReports = 0

        redNosedReports.forEach { report ->
            if ((isASortedReport(report)) && isValidReport(report)) {
                safeReports++
            }
        }
        return safeReports
    }

    override fun partTwo(): Any {
        var safeReports = 0

        redNosedReports.forEach { report ->
            if ((isASortedReport(report)) && isValidReport(report)) {
                safeReports++
            } else {
                for (index in report.indices) {
                    val reportWithoutSingleLevel = report.toMutableList()
                    reportWithoutSingleLevel.removeAt(index)
                    if (isASortedReport(reportWithoutSingleLevel) && isValidReport(reportWithoutSingleLevel)) {
                        safeReports++
                        break
                    }
                }
            }
        }
        return safeReports
    }

    private fun isASortedReport(report: List<Int>) =
        report == report.sorted() || report == report.sortedDescending()

    private fun isValidReport(report: List<Int>): Boolean {
        for (i in 0 until report.size - 1) {
            val diff = abs(report[i] - report[i + 1])
            if (diff > 3 || diff == 0) {
                return false
            }
        }
        return true
    }
}
