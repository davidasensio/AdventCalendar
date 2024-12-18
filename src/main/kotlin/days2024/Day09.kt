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
        return 1
        val blocks = mutableListOf<String>()
        val filesChunks = mutableListOf<String>()

        processBlocks(blocks, filesChunks)
        compactBlocks(blocks, filesChunks)

        return getChecksum(blocks)
    }

    override fun partTwo(): Any {
//        return 2
        // 108642932503
        // 6239783302560
        // 84231550994

        val blocks = mutableListOf<String>() /* Now each block is a file */
        val filesChunks = mutableListOf<Pair<Int, String>>()

        processBlocksBySingleFiles(blocks, filesChunks)
        compactBlocksBySingleFiles(blocks, filesChunks)
        println("Otro")
//        println(r)
        val finalList = blocks //.joinToString("") { it }.map { it.toString() }.toMutableList()
        val r = p2(diskData.map { it.toString().toInt() }, finalList)
        println("Other - " + calculateCheckSumP2(finalList.map { it.toIntOrNull() ?: 0 }))
        val myChecksum = getChecksum(finalList)
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
                        blocks.add(spaceAvailableIndex + it, chunk.second.take(chunk.second.length / chunk.first))
                        blocks.removeAt(spaceAvailableIndex + it)
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


    private fun p1(diskMap: List<Int>): BigInteger {
        val (file, _) = generateFileWithPointers(diskMap)
        val compactedFile = compactFileP1(file)
        val checksum = calculateCheckSum(compactedFile)
        return checksum
    }

    private fun p2(diskMap: List<Int>, finalList: MutableList<String>? = null): BigInteger {
        val (file, pointers) = generateFileWithPointers(diskMap)
        println(file.joinToString("") { it?.toString() ?: "." })
        val compactedFile = compactFileP2(file, pointers)
        println(compactedFile.joinToString("") { it?.toString() ?: "." })
        val checksum1 = calculateCheckSumP2(compactedFile)
        val checksum = getChecksum(compactedFile.map { it?.toString() ?: "." }.toMutableList())
        println(finalList == compactedFile.map { it?.toString() ?: "." })
        println(finalList?.map { it?.toString() ?: "." })
        println(compactedFile.map { it?.toString() ?: "." })

        //00000000011111110996..11111...2222222288888883333333..444444444..5555555557777...................................................
        //00000000011111110996..11111...2222222288888883333333..444444444..5555555557777...................................................
        return checksum + 2.toBigInteger()
    }

    private fun calculateCheckSum(compactedFile: List<Int?>): BigInteger {
        var result: BigInteger = 0.toBigInteger()
        var idx = 0
        while (idx < compactedFile.size && compactedFile[idx] != null) {
            val value = compactedFile[idx] ?: 0
            result += value.toBigInteger() * idx.toBigInteger()
//            println(result)
            idx++
        }
        return result
    }

    private fun calculateCheckSumP2(compactedFile: List<Int?>): BigInteger {
        var result: BigInteger = 0.toBigInteger()
        var idx = 0
        while (idx < compactedFile.size) {
            val value = compactedFile[idx] ?: 0
            result += value.toBigInteger() * idx.toBigInteger()
//            println(result)
            idx++
        }
        return result
    }

    private fun compactFileP1(file: List<Int?>): List<Int?> {
        var pointer1: Int? = 0
        var pointer2: Int? = file.size - 1
        var result = file
        pointer1 = nextSpace(result, pointer1!!)
        pointer2 = previousNumber(result, pointer2!!)
        while (pointer1 != null && pointer2 != null && pointer1 < pointer2) {
            result = moveNumber(result, pointer1, pointer2)
            pointer1 = nextSpace(result, pointer1)
            pointer2 = previousNumber(result, pointer2)
        }
        return result
    }

    private fun compactFileP2(file: List<Int?>, pointers: List<Pair<Int, Int>>): List<Int?> {
        val result = pointers.fold(file) { acc, pointer ->
            val size = pointer.second
            val positionToMove = acc.findConsecutiveNulls(size)

            moveNumbers(acc, positionToMove, pointer.first, size)
        }
        return result
    }

    private fun findFirstPositionToMove(acc: List<Int?>, size: Int): Int? {
        val result = acc.findConsecutiveNulls(size)
        return result
    }

    fun List<Int?>.findConsecutiveNulls(count: Int): Int? {
        return this
            .windowed(count) // Create sliding windows of size `count`
            .indexOfFirst { window -> window.all { it == null } } // Find the first window with all `null`s
            .takeIf { it != -1 } // Return `null` if no such window is found
    }

    private fun moveNumbers(file: List<Int?>, pointer1: Int?, pointer2: Int, size: Int): List<Int?> {
        return if (pointer1 == null || pointer2 < pointer1) {
            file
        } else {
            (pointer1..(pointer1 + size - 1)).foldIndexed(file) { idx, result, pointer ->
                moveNumber(result, pointer, pointer2 + idx)
            }
        }
    }

    private fun moveNumber(file: List<Int?>, pointer1: Int, pointer2: Int): List<Int?> {
        val number = file[pointer2]
        val result = file.replaceAt(pointer2, null).replaceAt(pointer1, number)
        return result
    }


    private fun List<Int?>.replaceAt(offset: Int, value: Int?): List<Int?> =
        this.take(offset) + value + drop(offset + 1)

    private fun previousNumber(file: List<Int?>, pointer: Int): Int? {
        var idx = pointer
        while (idx > 0 && file[idx] == null) {
            idx--
        }
        if (file[idx] != null) {
            return idx
        }
        return null
    }

    private fun nextSpace(file: List<Int?>, pointer: Int): Int? {
        var idx = pointer
        while (idx < file.size - 1 && file[idx] != null) {
            idx++
        }
        if (file[idx] == null) {
            return idx
        }
        return null

    }

    private fun generateFileWithPointers(diskMap: List<Int>): Pair<List<Int?>, List<Pair<Int, Int>>> {
        var id = 0
        var idx = 0
        val pointers = mutableListOf<Pair<Int, Int>>()
        val result = mutableListOf<Int?>()
        while (idx < diskMap.size) {
            val fileSize = diskMap[idx]
            val spaceSize = if (idx + 1 < diskMap.size) {
                diskMap[idx + 1]
            } else 0
            pointers.add(result.size to fileSize)
            result.addAll(List(fileSize) { id })
            result.addAll(List(spaceSize) { null })
            idx += 2
            id++
        }
        return result to pointers.toList().reversed()
    }


}
