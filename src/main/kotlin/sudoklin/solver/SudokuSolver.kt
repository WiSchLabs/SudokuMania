package sudoklin.solver

import sudoklin.data.Sudoku

class SudokuSolver {
    fun getMissingNumbersInRow(sudoku: Sudoku, rowIndex: Int): List<Int> {
        val row = sudoku.puzzle.getRow(rowIndex)
        return getMissingNumbersInList(row)
    }

    fun getMissingNumbersInColumn(sudoku: Sudoku, columnIndex: Int): List<Int> {
        val column = sudoku.puzzle.getColumn(columnIndex)
        return getMissingNumbersInList(column)
    }

    fun getMissingNumbersInGroup(sudoku: Sudoku, groupIndex: Int): List<Int> {
        val group = sudoku.puzzle.getGroup(groupIndex)
        return getMissingNumbersInList(group)
    }

    private fun getMissingNumbersInList(list: List<String>): MutableList<Int> {
        var missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (i in 1..9) {
            var number: String = "" + i
            if (!list.contains(number)) {
                missingNumbers.add(i)
            }
        }
        return missingNumbers
    }
}