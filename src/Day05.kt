import kotlin.math.absoluteValue

fun main() {
    data class Line(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    ) {
        fun isHorizontal() = y1==y2
        fun isVertical() = x1==x2
        fun isDiagonal() = (x1-x2).absoluteValue == (y1-y2).absoluteValue
        fun maxCoordinate() = maxOf(maxOf(x1, x2), maxOf(y1, y2))
    }

    abstract class Floor {
        abstract fun mark(x: Int, y: Int)
        abstract fun getDangerousSpots(): Int
        abstract fun clear()

        fun drawLine(line: Line) {
            with(line) {
                if (isHorizontal()) {
                    val range = if(x1 < x2) x1..x2 else x2..x1
                    for(x in range) {
                        mark(x, y1)
                    }
                }
                else if (isVertical()) {
                    val range = if(y1 < y2) y1..y2 else y2..y1
                    for(y in range) {
                        mark(x1, y)
                    }
                }
                else if (isDiagonal()) {
                    val range = 0..(x1-x2).absoluteValue
                    val signX = if(x1 < x2) 1 else -1
                    val signY = if(y1 < y2) 1 else -1
                    for(offset in range) {
                        mark(x1 + signX * offset, y1 + signY * offset)
                    }
                }
            }
        }
    }

    data class Point(
        val x: Int,
        val y: Int
    )

    class FasterFloor(): Floor() {
        val points = mutableMapOf<Point, Int>().withDefault { 0 }

        override fun mark(x: Int, y: Int) {
            val point = Point(x,y)
            points[point] = points.getValue(point).inc()
        }

        override fun getDangerousSpots(): Int {
            return points.values.count { it > 1 }
        }

        override fun clear() {
            points.clear()
        }
    }

    data class SlowFloor(val size: Int): Floor() {
        val floor = Array<IntArray>(size) { IntArray(size) }

        override fun mark(x: Int, y: Int) {
            floor[x][y] += 1
        }

        override fun getDangerousSpots(): Int {
            return floor.sumOf { row -> row.filter { it > 1 }.size }
        }

        override fun clear() {
            for(y in 0 until size) {
                for (x in 0 until size) {
                    floor[x][y] = 0
                }
            }
        }

        override fun toString(): String {
            var result = ""
            for(y in 0 until size) {
                for(x in 0 until size) {
                    result += "${if(floor[x][y]==0) '.' else floor[x][y]} "
                }
                result += "\n"
            }
            return result
        }
    }

    /**
     * Parse a line like this "405,945 -> 780,945"
     */
    fun lineOf(str: String): Line {
        val (x1, y1, x2, y2) = str.split(" -> ", ",").map { it.toInt() }
        return Line(x1, y1, x2, y2)
    }

    fun part1(input: List<Line>): Int {
        //val size = input.maxOf { it.maxCoordinate() } + 1
        val oceanFloor = FasterFloor()
        input.filter { it.isHorizontal() || it.isVertical() }.forEach { oceanFloor.drawLine(it) }
        return oceanFloor.getDangerousSpots()
    }

    fun part2(input: List<Line>): Int {
        //val size = input.maxOf { it.maxCoordinate() } + 1
        val oceanFloor = FasterFloor()
        input.filter { it.isHorizontal() || it.isVertical() || it.isDiagonal() }.forEach {
            oceanFloor.drawLine(it)
        }
        return oceanFloor.getDangerousSpots()
    }

    val day = "05"

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test").map { lineOf(it) }
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day${day}").map { lineOf(it) }
    println(part1(input))
    println(part2(input))
    check(part1(input) == 7438)
    check(part2(input) == 21406)
}
