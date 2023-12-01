import days2022.*

fun main(args: Array<String>) {
    println("Hello Advent Calendar!\n")

    val days = listOf(
        Day01,
        Day02,
        Day03,
        Day04,
        Day05,
        Day06,
    )

    days.forEach { printDay(it) }

    println(Day04.title)
}

private fun printDay(day: Day) {
    val header = "--- Day ${day.day.toString().padStart(2, '0')}: ${day.title} ---"
    val footer = "-".repeat(header.length)

    println(header)
    println("|> Part 1: ${day.partOne()}")
    println("|> Part 2: ${day.partTwo()}")
    println(footer)
    println()
}
