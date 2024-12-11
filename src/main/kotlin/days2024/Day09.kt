package days2024

import Day
import java.math.BigInteger

fun main() {
    println(Day09.toString())
}

object Day09 : Day(2024, 9, "Disk Fragmenter", true) {
    private val diskData = inputString
    private const val SPACE = "."

    //.(.) for Odd numbers might not work if last number is odd because there wouldn't be any number on the right.
    // To take into account this case we can use a non-capturing group that matches either character or end of string.
    private val ODD_NUMBERS_REGEX = Regex("(.)(?:.|\\z)")
    private val EVEN_NUMBERS_REGEX = Regex(".(.)")

    private val files by lazy {
        ODD_NUMBERS_REGEX.findAll(diskData).map { it.groups.last()?.value }.joinToString("") { it.toString() }
    }
    private val filesAvailableSpace by lazy {
        EVEN_NUMBERS_REGEX.findAll(diskData).map { it.groups.last()?.value }.joinToString("") { it.toString() }
    }

    override fun beforeParts() {
        logLn(ODD_NUMBERS_REGEX.findAll(diskData).map { it.groups.last()?.value }.joinToString("") { it.toString() })
        logLn(EVEN_NUMBERS_REGEX.findAll(diskData).map { it.groups.last()?.value }.joinToString("") { it.toString() })
    }

    override fun partOne(): Any {
//        return 1
        val blocks = mutableListOf<String>()
        val filesChunks = mutableListOf<String>()

        processBlocks(blocks, filesChunks)
        compactBlocks(blocks, filesChunks)

        return getChecksum(blocks)
    }

    override fun partTwo(): Any {
//        return 2 //Not working

        val blocks = mutableListOf<String>() /* Now each block is a file */
        val filesChunks = mutableListOf<Pair<Int, String>>()

        processBlocksBySingleFiles(blocks, filesChunks)
        compactBlocksBySingleFiles(blocks, filesChunks)
        println("Otro")
        val myChecksum = getChecksum(blocks)
        return myChecksum
    }

    private fun processBlocks(
        blocks: MutableList<String>,
        filesChunks: MutableList<String>
    ) {
        files.forEachIndexed { index, file ->
            repeat(file.digitToInt()) {
                filesChunks.add(0, "$index")
                blocks.add("$index")
            }
            if (index < filesAvailableSpace.length) {
                repeat(filesAvailableSpace[index].digitToInt()) {
                    blocks.add(SPACE)
                }
            }
        }
        logLn(filesChunks.joinToString(",") { it })
        logLn(blocks.joinToString("") { it })
    }

    private fun compactBlocks(
        blocks: MutableList<String>,
        filesChunks: MutableList<String>
    ) {
        while (filesChunks.size > 1) {
            val chunk = filesChunks.removeFirst()
            val lastDigit = blocks.lastIndexOf(chunk)
            val firstSpace = blocks.indexOf(SPACE)
            if (lastDigit > firstSpace) {
                val digit = blocks[lastDigit]
                blocks[firstSpace] = digit
                blocks[lastDigit] = SPACE
            }
        }
        logLn(blocks.joinToString("") { it })
    }

    private fun processBlocksBySingleFiles(
        blocks: MutableList<String>,
        filesChunks: MutableList<Pair<Int, String>>,
    ) {
        files.forEachIndexed { index, file ->
            val fileValue = file.digitToInt()
            val block = "$index".repeat(fileValue)
            filesChunks.add(0, Pair(fileValue, block))
            blocks.add(block)
//            repeat(file.digitToInt()) {
//                blocks.add("$index")
//            }

            if (index < filesAvailableSpace.length) {
//                repeat(filesAvailableSpace[index].digitToInt()) {
//                    blocks.add(SPACE)
//                }
                blocks.add(SPACE.repeat(filesAvailableSpace[index].digitToInt()))
            }
        }
        logLn("File chunks: " + filesChunks.joinToString(",") { it.second })
        logLn(blocks.joinToString("") { it })
    }

    private fun compactBlocksBySingleFiles(
        blocks: MutableList<String>,
        filesChunks: MutableList<Pair<Int, String>>
    ) {
        while (filesChunks.size > 1) {
            val chunk = filesChunks.removeFirst()
            val chunkLastIndex = blocks.lastIndexOf(chunk.second)
            val spaceAvailableIndex = blocks.indexOfFirst {
                it.startsWith(".".repeat(chunk.first))
            }

            if (spaceAvailableIndex != -1) {
                if (chunkLastIndex > spaceAvailableIndex) {
                    val spaceLength = blocks[spaceAvailableIndex].length
                    val remainingSpaces = spaceLength - chunk.first
                    repeat(chunk.first) {
                        blocks.removeAt(spaceAvailableIndex + it)
                        blocks.add(spaceAvailableIndex + it, chunk.second.take(chunk.second.length / chunk.first))
                    }
                    blocks[chunkLastIndex] = SPACE.repeat(chunk.first)
                    if (remainingSpaces > 0) {
                        blocks.add(spaceAvailableIndex + 1, SPACE.repeat(remainingSpaces))
                    }
                }
            }
        }
        println("---" + blocks)
        logLn(blocks.joinToString("") { it })
    }

    private fun getChecksum(blocks: MutableList<String>): BigInteger {
//        val checksum: BigInteger = blocks.mapIndexed { index, digit ->
//            (index * (digit.toIntOrNull() ?: 0)).toBigInteger()
//        }.sumOf { it }
//        return checksum
        var checksum = BigInteger.ZERO
        blocks.forEachIndexed { index, digit ->
            checksum += (index * (digit.toIntOrNull() ?: 0)).toBigInteger()
        }
        return checksum
    }


}
