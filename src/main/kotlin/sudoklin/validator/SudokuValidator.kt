package sudoklin.validator

import sudoklin.data.Sudoku

class SudokuValidator {
    fun validateRow(sudoku: Sudoku, rowIndex: Int): Boolean {
        val row = sudoku.puzzle.getRow(rowIndex)
        return validateList(row)
    }

    fun validateColumn(sudoku: Sudoku, columnIndex: Int): Boolean {
        val column = sudoku.puzzle.getColumn(columnIndex)
        return validateList(column)
    }

    fun validateGroup(sudoku: Sudoku, groupIndex: Int): Boolean {
        val group = sudoku.puzzle.getGroup(groupIndex)
        return validateList(group)
    }

    fun validateList(list: Array<IntArray>): Boolean {
        var isValid = true
        for (i in 1..9) {
            val filteredList = list.filter { it.size == 1 && it[0] == i }
            isValid = isValid && (filteredList.size <= 1)
        }
        return isValid
    }

    fun validate(sudoku: Sudoku): Boolean {
        var isValid = true
        for (i in 0..8) {
            isValid = isValid && validateRow(sudoku, i)
            isValid = isValid && validateColumn(sudoku, i)
            isValid = isValid && validateGroup(sudoku, i)
        }
        return isValid
    }
}