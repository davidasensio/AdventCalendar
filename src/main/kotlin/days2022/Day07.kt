package days2022

import Day

fun main() {
    println(Day07.toString())
}

private data class File(val name: String, val size: Int)

private class Directory(val name: String, val contents: MutableSet<Any>) {
    fun totalSize(): Int = this.contents.sumOf {
        when (it) {
            is File -> it.size
            is Directory -> it.totalSize()
            else -> 0
        }
    }

    fun dirs(): List<Directory> = this.contents
        .filterIsInstance<Directory>()
        .flatMap { it.dirs() }
        .plus(this)

    override fun toString(): String {
        return this.name + " - " + this.totalSize()
    }
}

object Day07 : Day(2022, 7, "No Space Left On Device") {
    private const val SIZE_LIMIT = 100000
    private const val SIZE_AVAILABLE = 70000000
    private const val SIZE_REQUIRED = 30000000


    private val commandCd = "\\$ cd (.*)".toRegex()
    private val commandLs = "\\$ ls".toRegex()
    private val directory = "dir (.*)".toRegex()
    private val file = "(\\d+) (.*)".toRegex()

    private val root = Directory("/", mutableSetOf())
    private var current = root

    init {

        inputStringList.forEach { command ->
            when {
                command.matches(commandCd) -> {
                    val targetDirectory = commandCd.find(command)!!.groupValues.last()
                    current = when (targetDirectory) {
                        "/" -> root
                        ".." -> root.dirs().first { it.contents.contains(current) }
                        else -> current.contents
                            .filterIsInstance<Directory>()
                            .first { it.name == targetDirectory }
                    }
                }

                command.matches(commandLs) -> {}
                command.matches(directory) -> {
                    val fileName = directory.find(command)!!.groupValues.last()
                    current.contents.add(Directory(fileName, mutableSetOf()))
                }

                command.matches(file) -> {
                    val (fileSize, fileName) = file.find(command)!!.destructured
                    current.contents.add(File(fileName, fileSize.toInt()))
                }

                else -> {}
            }
        }
    }

    override fun partOne(): Any {
        val totalSize = root.dirs()
            .filter { it.totalSize() < SIZE_LIMIT }
            .sumOf { it.totalSize() }

        return "Total size of targeted directories is $totalSize"
    }

    override fun partTwo(): Any {
        val validDirs = mutableListOf<Directory>()
        root.dirs().forEach {
            // println(it.name + " - " + it.totalSize())
            if (root.totalSize() - it.totalSize() <= 40000000 ) {
                validDirs.add(it)
            }
        }
        validDirs.sortBy { it.totalSize() }

        return validDirs.minOf { it.totalSize() }
    }
}


/*
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
*/


// 1005501 too low
// 1255144 nop
// 1359292 too high

