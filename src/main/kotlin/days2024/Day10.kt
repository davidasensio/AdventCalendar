package days2024

import Day

fun main() {
    println(Day10.toString())
}

object Day10 : Day(2024, 10, "Hoof It", true) {
    private val topographicMap = inputStringList.map { row -> row.toCharArray().map { it.digitToInt() } }

    private const val SUMMIT = 9
    private const val VALLEY = 0

    override fun beforeParts() {
        // logLn(topographicMap.toString())
    }

    override fun partOne(): Any {
        var result = 0
        if (checkAnyBottom()) {
            val bottomPositions = getBottomPositions()
            val trailheads = bottomPositions.map { it to trailHeads(position = it) }
            result = trailheads.sumOf { it.second }
        }

        return result
    }

    override fun partTwo(): Any {
        var result = 0
        if (checkAnyBottom()) {
            val bottomPositions = getBottomPositions()
            val trailheads = bottomPositions.map { it to trailHeadsPart2(position = it) }
            result = trailheads.sumOf { it.second }
        }

        return result
    }

    private fun checkAnyBottom(): Boolean {
        val bottoms = anyBottom(topographicMap)
        logLn("Total founded $VALLEY: $bottoms")
        return bottoms > 0
    }

    private fun anyBottom(map: List<List<Int>>): Int = map.sumOf { row -> row.count { it == VALLEY } }

    private fun getBottomPositions(): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()

        for (i in topographicMap.indices) {
            for (j in topographicMap[i].indices) {
                if (topographicMap[i][j] == VALLEY) {
                    positions.add(Pair(i, j))
                }
            }
        }
        return positions
    }

    private fun trailHeads(position: Pair<Int, Int>): Int {
        val reachableTops = mutableSetOf<Pair<Int, Int>>()
        countReachableTops(TopographicTreeNode(position), reachableTops)
        return reachableTops.size
    }

    private fun trailHeadsPart2(position: Pair<Int, Int>): Int {
        return countDistinctPathsFromLeafToTop(TopographicTreeNode(position))
    }

    private fun possibleContinuations(position: Pair<Int, Int>, startPoint: Int): List<Pair<Int, Int>> {

        fun isNeighborEqualToStartPoint(neighbor: Pair<Int, Int>): Boolean =
            topographicMap.getOrNull(neighbor.first)?.getOrNull(neighbor.second) == (startPoint + 1)

        val (row, col) = position
        return listOfNotNull(
            Pair(row - 1, col).takeIf { isNeighborEqualToStartPoint(it) },
            Pair(row + 1, col).takeIf { isNeighborEqualToStartPoint(it) },
            Pair(row, col - 1).takeIf { isNeighborEqualToStartPoint(it) },
            Pair(row, col + 1).takeIf { isNeighborEqualToStartPoint(it) }
        )
    }

    private fun countReachableTops(parentNode: TopographicTreeNode, tops: MutableSet<Pair<Int, Int>>) {
        val (row, col) = parentNode.position
        parentNode.children = possibleContinuations(parentNode.position, topographicMap[row][col])
            .map { TopographicTreeNode(it) }

        if (parentNode.children.isEmpty()) {
            if (topographicMap[row][col] == SUMMIT) {
                tops.add(parentNode.position)
            }
        }

        for (child in parentNode.children) {
            countReachableTops(child, tops)
        }
    }

    private fun countDistinctPathsFromLeafToTop(parentNode: TopographicTreeNode): Int {
        val (row, col) = parentNode.position
        parentNode.children = possibleContinuations(parentNode.position, topographicMap[row][col])
            .map { TopographicTreeNode(it) }

        if (parentNode.children.isEmpty()) {
            return if (topographicMap[row][col] == SUMMIT) 1 else 0
        }

        var count = 0
        for (child in parentNode.children) {
            val pathsFromChild = countDistinctPathsFromLeafToTop(child)
            if (pathsFromChild > 0) {
                count += (pathsFromChild)
            }
        }
        return count
    }

    class TopographicTreeNode(val position: Pair<Int, Int>, var children: List<TopographicTreeNode> = emptyList())
}
