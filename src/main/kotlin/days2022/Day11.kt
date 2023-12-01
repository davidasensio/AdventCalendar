package days2022

import Day

fun main() {
    println(Day11.toString())
}

object Day11 : Day(2022, 11, "Monkey in the Middle", true) {
    private val monkeys: List<Monkey>
    val rounds = 20

    init {
        monkeys = inputString.split("\n\n").map {
            val lines = it.split("\n")
            Monkey(
                items = "Starting items: (.*)".regexFind(lines[1]).split(", ").map { it.toLong() }.toMutableList(),
                operation = "Operation: new = old (.*)".regexFind(lines[2]),
                divider = "Test: divisible by (.*)".regexFind(lines[3]).toInt(),
                monkeyTrueRedirect = "If true: throw to monkey (.*)".regexFind(lines[4]).toInt(),
                monkeyFalseRedirect = "If false: throw to monkey (.*)".regexFind(lines[5]).toInt()
            )
        }
    }

    override fun partOne(): Any {
        repeat(rounds) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val (operator, factor2Str) = monkey.operation.split(" ")
                    val factor2 = if (factor2Str == "old") item else factor2Str.toLong()
                    val worryLevel = when (operator) {
                        "-" -> item - factor2
                        "+" -> item + factor2
                        "*" -> item * factor2
                        else -> item / factor2
                    }
                    val worryLevelDivided = worryLevel / 3
                    val isDivisible = worryLevelDivided % monkey.divider == 0L
                    val receiver = if (isDivisible) monkey.monkeyTrueRedirect else monkey.monkeyFalseRedirect
                    monkeys[receiver].items.add(worryLevelDivided)
                    monkey.counter++
                }
            }
        }
        logLn(monkeys.map { it.counter }.toString())
        val sortedMonkeys = monkeys.sortedByDescending { it.counter }
        val monkeyBusinessLevel = sortedMonkeys[0].counter * sortedMonkeys[1].counter
        return "Monkey Business Level is $monkeyBusinessLevel"
    }

    override fun partTwo(): Any {
        return false
        repeat(rounds) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val (operator, factor2Str) = monkey.operation.split(" ")
                    val factor2 = if (factor2Str == "old") item else factor2Str.toLong()
                    val worryLevel = when (operator) {
                        "-" -> item - factor2
                        "+" -> item + factor2
                        "*" -> item * factor2
                        else -> item / factor2
                    }
                    val worryLevelDivided = worryLevel % monkey.divider
                    val isDivisible = worryLevelDivided % monkey.divider == 0L
                    val receiver = if (isDivisible) monkey.monkeyTrueRedirect else monkey.monkeyFalseRedirect
                    monkey.counter++
                    monkeys[receiver].items.add(worryLevelDivided)
                }
            }
        }
        logLn(monkeys.map { it.counter }.toString())
        val sortedMonkeys = monkeys.sortedByDescending { it.counter }
        val monkeyBusinessLevel = sortedMonkeys[0].counter * sortedMonkeys[1].counter
        return "Monkey Business Level is $monkeyBusinessLevel"
    }

    private fun String.regexFind(text: String) =
        this.toRegex().find(text)?.groups?.last()?.value ?: ""

    data class Monkey(
        val items: MutableList<Long>,
        val operation: String,
        val divider: Int,
        val monkeyTrueRedirect: Int,
        val monkeyFalseRedirect: Int,
        var counter: Long = 0
    )
}

