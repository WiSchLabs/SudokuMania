package sudoklin.validator

import sudoklin.data.Sudoku

class SudokuValidator {
    fun validateRow(sudoku: Sudoku, rowNumber: Int): Boolean {
        val row = sudoku.puzzle.getRow(rowNumber)
        var valid = true
        for (i in 1..9) {
            val filteredRow = row.filter { it == i.toString() }
            valid = valid && (filteredRow.size <= 1)
        }
        return valid
    }

    fun validateColumn(sudoku: Sudoku, columnNumber: Int): Boolean {
        val column = sudoku.puzzle.getColumn(columnNumber)
        var valid = true
        for (i in 1..9) {
            val filteredColumn = column.filter { it == i.toString() }
            valid = valid && (filteredColumn.size <= 1)
        }
        return valid
    }

    fun validateGroup(sudoku: Sudoku, groupIndex: Int): Boolean {
        val column = sudoku.puzzle.getGroup(groupIndex)
        var valid = true
        for (i in 1..9) {
            val filteredColumn = column.filter { it == i.toString() }
            valid = valid && (filteredColumn.size <= 1)
        }
        return valid
    }
}