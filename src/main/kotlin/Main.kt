import days2022.Day01
import days2022.Day04

fun main(args: Array<String>) {
    println("Hello Advent Calendar 2022!\n")

    val days = listOf(
        Day01
    )

    days.forEach { printDay(it) }

    println(Day04.title)
}

private fun printDay(day: Day) {
    val header = "--- Day ${day.number.toString().padStart(2, '0')}: ${day.title} ---"
    val footer = "-".repeat(header.length)

    println(header)
    println("|> Part 1: ${day.partOne()}")
    println("|> Part 2: ${day.partTwo()}")
    println(footer)
    println()
}
