package days2022

import Day

fun main() {
    println(Day08.toString())
}

object Day08 : Day(8, "Treetop Tree House") {
    private val x: Int = inputStringList.first().length
    private val y: Int = inputStringList.size
    private val mapOfTrees = Array(y) { IntArray(x) }
    private val mapOfTreesTransposed by lazy { transpose(mapOfTrees) }

    init {
        // Read Tree Map
        inputStringList.forEachIndexed { index, treeRow ->
            mapOfTrees[index] = treeRow.toCharArray().map { it.digitToInt() }.toIntArray()
        }
    }

    override fun partOne(): Any {
        var visibleTrees = 0

        mapOfTrees.forEachIndexed { indexRow, treeRow ->
            treeRow.forEachIndexed { indexColumn, treeHeight ->
                val isTopOrBottom = indexRow == 0 || indexRow == y - 1
                val isLeftOrRight = indexColumn == 0 || indexColumn == x - 1
                if (isTopOrBottom || isLeftOrRight) {
                    visibleTrees += 1
                } else {
                    val leftSlice = mapOfTrees[indexRow].slice(0 until indexColumn)
                    // println("Number is $treeHeight and left slice = $leftSlice")
                    val rightSlice = mapOfTrees[indexRow].slice(indexColumn + 1 until x)
                    // println("Number is $treeHeight and right slice = $rightSlice")
                    val topSlice = mapOfTreesTransposed[indexColumn].slice(0 until indexRow)
                    // println("Number is $treeHeight and top slice = $topSlice")
                    val bottomSlice = mapOfTreesTransposed[indexColumn].slice(indexRow + 1 until x)
                    // println("Number is $treeHeight and bottom slice = $bottomSlice")

                    if (leftSlice.all { it < treeHeight }) {
                        visibleTrees += 1
                    } else if (rightSlice.all { it < treeHeight }) {
                        visibleTrees += 1
                    } else if (topSlice.all { it < treeHeight }) {
                        visibleTrees += 1
                    } else if (bottomSlice.all { it < treeHeight }) {
                        visibleTrees += 1
                    }
                }
            }
        }
        return "Size is $x x $y and Total is $visibleTrees"
    }

    override fun partTwo(): Any {
        val scenicScoreList = mutableListOf<Int>()

        mapOfTrees.forEachIndexed { indexRow, treeRow ->
            treeRow.forEachIndexed { indexColumn, treeHeight ->
                var scenicScoreLeft = 0
                var scenicScoreRight = 0
                var scenicScoreTop = 0
                var scenicScoreBottom = 0
                val leftSlice = mapOfTrees[indexRow].slice(0 until indexColumn)
                val rightSlice = mapOfTrees[indexRow].slice(indexColumn + 1 until x)
                val topSlice = mapOfTreesTransposed[indexColumn].slice(0 until indexRow)
                val bottomSlice = mapOfTreesTransposed[indexColumn].slice(indexRow + 1 until x)

                run left@{
                    leftSlice.reversed().forEach {
                        scenicScoreLeft++
                        if (it >= treeHeight) return@left
                    }
                }
                run right@{
                    rightSlice.forEach {
                        scenicScoreRight++
                        if (it >= treeHeight) return@right
                    }
                }
                run top@{
                    topSlice.reversed().forEach {
                        scenicScoreTop++
                        if (it >= treeHeight) return@top
                    }
                }
                run bottom@{
                    bottomSlice.forEach {
                        scenicScoreBottom++
                        if (it >= treeHeight) return@bottom
                    }
                }

                val scenicScore = (scenicScoreLeft * scenicScoreRight * scenicScoreTop * scenicScoreBottom)
                scenicScoreList.add(scenicScore)
            }
        }
        return " Max score is ${scenicScoreList.max()}"
    }

    private fun transpose(array: Array<IntArray>): Array<IntArray> {
        val cols = array.first().size
        val rows = array.size
        val transposedArray = Array(cols) { IntArray(rows) }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposedArray[j][i] = array[i][j]
            }
        }
        return transposedArray
    }
}
