fun main() {

    fun part1(input: List<String>): Int {
        val draws = getDraws(input)
        val boards = getBoards(input)
        val winningBoards = playGame(draws, boards)
        val winningBoard = winningBoards.first()
        return winningBoard.sum() * winningBoard.getWinningNumber()
    }

    fun part2(input: List<String>): Int {
        val draws = getDraws(input)
        val boards = getBoards(input)
        val winningBoards = playGame(draws, boards)
        val winningBoard = winningBoards.last()
        return winningBoard.sum() * winningBoard.getWinningNumber()
    }

    val day = "04"

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
    check(part1(input) == 27027)
    check(part2(input) == 36975)
}

fun getDraws(input: List<String>): List<Int> {
    return input[0].split(',').map { it.toInt() }
}

fun getBoards(input: List<String>): List<Board> {
    val boards = mutableListOf<Board>()

    var board: Board? = null
    for (idx in 1 until input.size) {
        val line = input[idx]
        if (line.isBlank()) {
            board?.let { boards.add(it) }
            board = Board()
        }
        else {
            board?.addRow(line)
        }
    }
    board?.let { boards.add(it) }

    return boards
}

fun playGame(draws: List<Int>, boards: List<Board>): List<Board> {
    val winningBoards = mutableListOf<Board>()
    draws.forEach { number ->
        boards.filterNot { it.isWinner() }.forEach { board ->
            if (board.play(number)) {
                winningBoards.add(board)
            }
        }
    }
    return winningBoards
}

class Board {

    private var numbers = mutableListOf<List<Cell>>()
    private var rows: Int = 0
    private var columns: Int = 0
    private var winner = false
    private var winningNumber: Int = 0

    class Cell(val number: Int) {
        var marked: Boolean = false

        override fun toString(): String {
            return "${number}${ if(marked) "*" else " " }"
        }
    }

    fun addRow(rowString: String) {
        val row = rowString.trim().split("\\s+".toRegex()).map { Cell(it.toInt()) }
        numbers.add(row)
        columns = row.size
        rows = numbers.size
    }

    fun play(number: Int): Boolean {
        numbers.forEach { row -> row.filter { it.number == number }.forEach { it.marked = true } }
        if (checkWinning()) {
            winner = true
            winningNumber = number
        }
        return winner
    }

    private fun getColumn(column: Int): List<Cell> {
        return numbers.map { row -> row[column] }.toList()
    }

    private fun getRow(row: Int): List<Cell> {
        return numbers[row]
    }

    private fun columnComplete(column: Int): Boolean {
        return getColumn(column).all { it.marked }
    }

    private fun rowComplete(row: Int): Boolean {
        return getRow(row).all { it.marked }
    }

    private fun checkWinning(): Boolean {
        for (column in 0 until columns) {
            if (columnComplete(column)) return true
        }
        for (row in 0 until rows) {
            if (rowComplete(row)) return true
        }
        return false
    }

    fun sum(): Int = numbers.sumOf { row -> row.filterNot { it.marked }.sumOf { it.number } }

    override fun toString(): String {
        return numbers.joinToString("\n") { row -> row.joinToString(" ") { if (it.marked) "*" else "${it.number}" } }
    }

    fun isWinner(): Boolean {
        return winner
    }

    fun getWinningNumber(): Int {
        return winningNumber
    }
}

//fun BoardTest()
//{
//    val board = Board()
//    board.addRow("1 2 3")
//    board.addRow("4 5 6")
//    board.addRow("7 8 9")
//
//    board.play(1)
//    check(!board.rowComplete(0))
//    board.play(2)
//    check(!board.rowComplete(0))
//    check(!board.checkWinning())
//
//    board.play(3)
//    check(board.rowComplete(0))
//    check(board.checkWinning())
//
//    check(!board.columnComplete(1))
//    board.play(5)
//    check(!board.columnComplete(1))
//    board.play(8)
//    check(!board.columnComplete(0))
//    check(board.columnComplete(1))
//    check(!board.columnComplete(2))
//
//    check(board.checkWinning())
//}
