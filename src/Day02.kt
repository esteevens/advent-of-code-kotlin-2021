fun main() {
    class Command {
        val command: String
        val value: Int

        constructor(string: String) {
            val list = string.split(' ')
            this.command = list[0]
            this.value = list[1].toInt()
        }

        operator fun component1(): String = command
        operator fun component2(): Int = value
    }

    fun part1(input: List<Command>): Int {
        var horizontalPosition: Int = 0
        var depth: Int = 0
        for((command, value) in input) {
            when(command) {
                "forward" -> horizontalPosition += value
                "down" -> depth += value
                "up" -> depth -= value
            }
        }

        return horizontalPosition * depth
    }

    fun part2(input: List<Command>): Int {
        var aim: Int = 0
        var horizontalPosition: Int = 0
        var depth: Int = 0
        for((command, value) in input) {
            when(command) {
                "forward" -> {
                    horizontalPosition += value
                    depth += aim * value
                }
                "down" -> aim += value
                "up" -> aim -= value
            }
        }

        return horizontalPosition * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test").map { Command(it) }
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02").map { Command(it) }
    println(part1(input))
    println(part2(input))
}
