package days2024

import Day

fun main() {
    println(Day03.toString())
}

object Day03 : Day(2024, 3, "Mull It Over") {
    private val instructions: String = inputStringList.joinToString { it } // Removes line breaks
    private val multiplyRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
    private val disabledInstructionsRegex = "don't\\(\\).*?(do\\(\\)|$)".toRegex()

    override fun partOne(): Any {
        return multiply(instructions)
    }

    /**
     * Now, do() instruction enables operation don't() instruction disables operation
     */
    override fun partTwo(): Any {
        return multiply(removeDontBlocks(instructions))
    }

    private fun removeDontBlocks(instructions: String): String {
        return if (instructions.contains("don't()")) {
            removeDontBlocks(
                instructions.replace(regex = disabledInstructionsRegex, "")
            )
        } else {
            instructions
        }
    }

    private fun multiply(instructions: String): Int {
        var result = 0
        multiplyRegex.findAll(instructions).forEach {
            /*println(it.value)
            println(it.groups[1]?.value)
            println(it.groups[2]?.value)*/
            val number1 = it.groups[1]?.value?.toInt() ?: 0
            val number2 = it.groups[2]?.value?.toInt() ?: 0
            result += number1 * number2
        }
        return result
    }
}
