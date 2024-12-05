package days2024

import Day

fun main() {
    println(Day05.toString())
}

object Day05 : Day(2024, 5, "Print Queue") {
    private val data = inputString.split("\n\n")
    private val orderingRules = data[0].split("\n")
    private val printUpdates = data[1].split("\n")
    private val validPrintUpdates = mutableListOf<String>()
    private val fixedPrintUpdates = mutableListOf<String>()

    override fun beforeParts() {
        /*println(orderingRules)
        println(printUpdates)*/
    }

    override fun partOne(): Any {
        printUpdates.forEach { printUpdate ->
            if (isValidPrint(printUpdate)) {
                validPrintUpdates.add(printUpdate)
            }
        }

        return calcSumOfMiddlePage(validPrintUpdates)
    }

    override fun partTwo(): Any {
        val invalidPrintUpdates = printUpdates.minus(validPrintUpdates.toSet())

        invalidPrintUpdates.forEach {
            fixedPrintUpdates.add(fixPrintUpdateOrder(it))
        }

        return calcSumOfMiddlePage(fixedPrintUpdates)
    }

    private fun isValidPrint(printUpdate: String): Boolean {
        var isValidPrint = true

        orderingRules.forEach rules@{
            val pageLeft = it.split("|")[0]
            val pageRight = it.split("|")[1]
            val pageLeftIndex = printUpdate.indexOf(pageLeft)
            val pageRightIndex = printUpdate.indexOf(pageRight)
            if (pageLeftIndex != -1 && pageRightIndex != -1) {
                if (pageLeftIndex > pageRightIndex) {
                    isValidPrint = false
                    return@rules
                }
            }
        }
        return isValidPrint
    }

    private fun calcSumOfMiddlePage(pageList: List<String>) = pageList.sumOf {
        it.split(",").let { pages ->
            pages[pages.size / 2].toInt()
        }
    }

    private fun fixPrintUpdateOrder(printUpdate: String): String {
        val pages = printUpdate.split(",").toMutableList()
        var index = 0
        while (index < pages.size - 1) {
            val pageLeft = pages[index]
            val pageRight = pages[index + 1]
            if (orderingRules.none { it == "$pageLeft|$pageRight" }) {
                pages[index] = pageRight
                pages[index + 1] = pageLeft
                index = 0
            } else {
                index++
            }
        }
        return pages.joinToString(",")
    }
}
