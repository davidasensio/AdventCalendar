import java.io.File

abstract class Day(val number: Int, val title: String) {
    protected val inputString by lazy { InputReader.readAsString(number) }
    protected val inputStringList by lazy { InputReader.readAsList(number) }

    abstract fun partOne() : Any
    abstract fun partTwo() : Any

    override fun toString(): String {
        return """
            Part One: ${partOne()}
            Part Two: ${partTwo()}
        """.trimIndent()
    }
}

private object InputReader {
    fun readAsString(number: Int) = fileFromResources(number).readText()
    fun readAsList(number: Int) = fileFromResources(number).readLines()

    private fun fileFromResources(number: Int): File {
        val formattedNumber = number.toString().padStart(2, '0')
        val filename = "Day$formattedNumber.txt"
        return File(javaClass.getResource(filename)?.toURI() ?: error("No input file found for day $number!"))

    }
}
