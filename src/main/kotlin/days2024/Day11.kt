package days2024

import Day
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentLinkedQueue

fun main() {
    println(Day11.toString())
}

object Day11 : Day(2024, 11, "Plutonian Pebbles", true) {
    private val stones = inputStringList.first().split(" ")

    private const val ITERATIONS_PART_1 = 25
    private const val ITERATIONS_PART_2 = 35 //Got stuck above 40 :(

    override fun partOne(): Any {
        return getTransformedStones(ITERATIONS_PART_1, stones).size
    }


    override fun partTwo(): Any = runBlocking {
        val result = mutableListOf<ConcurrentLinkedQueue<Pair<Int, List<String>>>>() // Thread-safe data structure

        val startTime = System.currentTimeMillis()
        var stonesIt = stones.toMutableList()
        repeat(5) {
            result.add(ConcurrentLinkedQueue<Pair<Int, List<String>>>())
            stonesIt.mapIndexed { index, stone ->
                async(Dispatchers.Default) { // Specify a dispatcher for parallelism
                    println("Processing stone $index")
                    val transformedStones = getTransformedStones(1, listOf(stone))
                    result[it].add(index to transformedStones) // Add results as pairs
                }
            }.awaitAll()
            stonesIt = result[it].flatMap { it.second }.toMutableList()
        }

        println("Total time elapsed: ${(System.currentTimeMillis() - startTime) / 1000} segs")

//        result.last().flatMap {  it.second }.forEach {
//            repeat(1) {
//                result.add(ConcurrentLinkedQueue<Pair<Int, List<String>>>())
//                stonesIt.mapIndexed { index, stone ->
//                    async(Dispatchers.Default) { // Specify a dispatcher for parallelism
//                        println("Processing stone $index")
//                        val transformedStones = getTransformedStones(5, listOf(stone))
//                        result[it].add(index to transformedStones) // Add results as pairs
//                    }
//                }.awaitAll()
//                stonesIt = result[it].flatMap { it.second }.toMutableList()
//            }
//        }
        println(result.last().flatMap { it.second }.size)
    }

    private fun partTwoBasic(): Int {
        val result = mutableListOf<List<String>>()
        stones.forEachIndexed { index, stone ->
            result.add(index, getTransformedStones(ITERATIONS_PART_2, listOf(stone)))
        }

        return result.flatMap { it }.size
    }

    private fun getTransformedStones(repetitions: Int, stones: List<String>): List<String> {
        var result = stones.toMutableList()
        repeat(repetitions) {
            val stoneTransformations = mutableListOf<String>()
            result.forEach { stone ->
                transformStone(stone, stoneTransformations)
            }
            result = stoneTransformations
        }
        return result.toList()
    }

    private fun transformStone(stone: String, items: MutableList<String>) {
        if (stone == "0") {
            items.add("1")
        } else if (stone.length % 2 == 0) {
            val half = stone.length / 2
            val left = stone.substring(0, half)
            val right = stone.substring(half)
            items.add(left)
            items.add(right.toBigInteger().toString())
        } else {
            items.add((stone.toBigInteger() * "2024".toBigInteger()).toString())
        }
    }
}
