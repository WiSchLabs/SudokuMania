package sudoklin.solver

import sudoklin.data.Sudoku

class SudokuSolver {
    fun getMissingNumbersForRow(sudoku: Sudoku, rowIndex: Int): List<Int> {
        val row = sudoku.puzzle.getRow(rowIndex)
        return getMissingNumbersInList(row)
    }

    fun getMissingNumbersForColumn(sudoku: Sudoku, columnIndex: Int): List<Int> {
        val column = sudoku.puzzle.getColumn(columnIndex)
        return getMissingNumbersInList(column)
    }

    fun getMissingNumbersForGroup(sudoku: Sudoku, groupIndex: Int): List<Int> {
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

    fun getCandidatesForCell(sudoku: Sudoku, rowIndex: Int, columnIndex: Int): Set<Int> {
        var missingNumbersForRow = getMissingNumbersForRow(sudoku, rowIndex)
        var missingNumbersForColumn = getMissingNumbersForColumn(sudoku, columnIndex)
        var groupIndex = sudoku.puzzle.getGroupIndexForCell(rowIndex, columnIndex)
        var missingNumbersForGroup = getMissingNumbersForGroup(sudoku, groupIndex)

        return missingNumbersForRow.intersect(missingNumbersForColumn).intersect(missingNumbersForGroup)
    }

}