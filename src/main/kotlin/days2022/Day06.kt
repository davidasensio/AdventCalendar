package days2022

import Day

fun main() {
    println(Day06.toString())
}

object Day06 : Day(2022, 6, "Tuning Trouble") {
    private const val START_PACKET_LENGTH = 4
    private const val START_MESSAGE_LENGTH = 14

    override fun partOne(): Any {
        var startOfPacket = -1
        var firstStartOfPacketFound = false

        inputString.forEachIndexed { index, _ ->
            if (index > (START_PACKET_LENGTH - 1)) {
                val chunk = inputString.substring(index - START_PACKET_LENGTH, index)
                // println(chunk)
                val startOfPacketFound = chunk.toCharArray().toSet().size == START_PACKET_LENGTH
                if (startOfPacketFound && !firstStartOfPacketFound) {
                    startOfPacket = index
                    firstStartOfPacketFound = true
                }
            }
        }
        return startOfPacket
    }

    override fun partTwo(): Any {
        var startOfPacket = -1
        var firstStartOfPacketFound = false

        inputString.forEachIndexed { index, _ ->
            if (index > (START_MESSAGE_LENGTH - 1)) {
                val chunk = inputString.substring(index - START_MESSAGE_LENGTH, index)
                // println(chunk)
                val startOfPacketFound = chunk.toCharArray().toSet().size == START_MESSAGE_LENGTH
                if (startOfPacketFound && !firstStartOfPacketFound) {
                    startOfPacket = index
                    firstStartOfPacketFound = true
                }
            }
        }
        return startOfPacket
    }
}
