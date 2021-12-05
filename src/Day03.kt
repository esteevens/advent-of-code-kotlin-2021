fun main() {
    fun dec(input: String): Int {
        var result = 0
        for(bit in input) {
            result = result shl 1 or (if (bit == '1') 1 else 0)
        }
        return result
    }

    /**
     * Find the most common [bit] value in the [input].
     */
    fun mostCommon(input: List<String>, bit: Int): Int {
        val zeros = input.count { it[bit] == '0' }
        return if (zeros > input.size / 2) 0 else 1
    }

    fun leastCommon(input: List<String>, bit: Int): Int {
        return if (mostCommon(input, bit) == 0) 1 else 0
    }

    fun filterInput(input: List<String>, method: (List<String>, Int) -> Int): Int {
        val bits = input[0].length

        var result = input
        for(bit in 0 until bits) {
            val bitToCompareWith = method(result, bit).digitToChar()
            result = result.filter { it[bit] == bitToCompareWith }
            if (result.size == 1)
            {
                return dec(result[0])
            }
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        val bits = input[0].length

        var gamma = 0
        var epsilon = 0
        for(bit in 0 until bits) {
            gamma = gamma shl 1 or mostCommon(input, bit)
            epsilon = epsilon shl 1 or leastCommon(input, bit)
        }

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = filterInput(input) { a, b -> mostCommon(a, b) }
        val co2ScrubberRating = filterInput(input) { a, b -> leastCommon(a, b) }

        return oxygenGeneratorRating * co2ScrubberRating
    }

    val day = "03"

    val testList = listOf("11110", "11100", "11000", "10000")
    check(mostCommon(testList, 0) == 1)
    check(mostCommon(testList, 1) == 1)
    check(mostCommon(testList, 2) == 1)
    check(mostCommon(testList, 3) == 0)
    check(mostCommon(testList, 4) == 0)
    check(leastCommon(testList, 0) == 0)
    check(leastCommon(testList, 1) == 0)
    check(leastCommon(testList, 2) == 0)
    check(leastCommon(testList, 3) == 1)
    check(leastCommon(testList, 4) == 1)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
    check(part1(input) == 3901196)
    check(part2(input) == 4412188)
}
