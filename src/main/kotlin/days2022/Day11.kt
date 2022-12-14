package days2022

import Day

fun main() {
    println(Day11.toString())
}

object Day11 : Day(11, "Monkey in the Middle", true) {
    private val monkeys: List<Monkey>

    init {
        monkeys = inputString.split("\n\n").map {
            val lines = it.split("\n")
            Monkey(
                items = "Starting items: (.*)".regexFind(lines[1]).split(", ").map { it.toInt() }.toMutableList(),
                operation = "Operation: new = old (.*)".regexFind(lines[2]),
                divider = "Test: divisible by (.*)".regexFind(lines[3]).toInt(),
                monkeyTrueRedirect = "If true: throw to monkey (.*)".regexFind(lines[4]).toInt(),
                monkeyFalseRedirect = "If false: throw to monkey (.*)".regexFind(lines[5]).toInt()
            )
        }
    }

    override fun partOne(): Any {
        val rounds = 20

        repeat(rounds) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val (operator, factor2Str) = monkey.operation.split(" ")
                    val factor2 = if (factor2Str == "old") item else factor2Str.toInt()
                    val worryLevel = when (operator) {
                        "-" -> item - factor2
                        "+" -> item + factor2
                        "*" -> item * factor2
                        else -> item / factor2
                    }
                    val worryLevelDivided = worryLevel / 3
                    val isDivisible = worryLevelDivided % monkey.divider == 0
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
    }

    private fun String.regexFind(text: String) =
        this.toRegex().find(text)?.groups?.last()?.value ?: ""

    data class Monkey(
        val items: MutableList<Int>,
        val operation: String,
        val divider: Int,
        val monkeyTrueRedirect: Int,
        val monkeyFalseRedirect: Int,
        var counter: Int = 0
    )
}

