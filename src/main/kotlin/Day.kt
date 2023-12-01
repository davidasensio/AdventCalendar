import java.io.File

abstract class Day(val year: Int, val day: Int, val title: String, val debug: Boolean = false) {
    protected val inputString by lazy { InputReader.readAsString(year, day) }
    protected val inputStringList by lazy { InputReader.readAsList(year, day) }

    abstract fun partOne(): Any
    abstract fun partTwo(): Any

    fun logLn(message: String = "") {
        if (debug) {
            println(message)
        }
    }

    override fun toString(): String {
        return """
            Part One: ${partOne()}
            Part Two: ${partTwo()}
        """.trimIndent()
    }
}

private object InputReader {
    fun readAsString(year: Int, number: Int) = fileFromResources(year, number).readText()
    fun readAsList(year: Int, number: Int) = fileFromResources(year, number).readLines()

    private fun fileFromResources(year: Int, number: Int): File {
        val formattedNumber = number.toString().padStart(2, '0')
        val filename = "$year/Day$formattedNumber.txt"
        return File(javaClass.getResource(filename)?.toURI() ?: error("No input file found for day $number!"))

    }
}
