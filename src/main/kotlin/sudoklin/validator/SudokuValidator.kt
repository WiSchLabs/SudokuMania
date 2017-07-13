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

    private fun validateList(list: Array<String>): Boolean {
        var valid = true
        for (i in 1..9) {
            val filteredList = list.toList().filter { it == i.toString() }
            valid = valid && (filteredList.size <= 1)
        }
        return valid
    }
}