package sudoklin.validator

import sudoklin.data.Sudoku

/**
 * Created by sebastian on 26.06.17.
 */
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
}