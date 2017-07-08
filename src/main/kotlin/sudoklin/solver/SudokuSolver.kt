package sudoklin.solver

import sudoklin.data.Sudoku

class SudokuSolver(initialSudoku :Sudoku) {
    val initialSudoku = initialSudoku
    val workingStack = ArrayList<Sudoku>()

    init {
        workingStack.add(initialSudoku.clone())
    }

    fun getMissingNumbersForRow(rowIndex: Int): List<Int> {
        val row = workingStack.last().puzzle.getRow(rowIndex)
        return getMissingNumbersInList(row)
    }

    fun getMissingNumbersForColumn(columnIndex: Int): List<Int> {
        val column = workingStack.last().puzzle.getColumn(columnIndex)
        return getMissingNumbersInList(column)
    }

    fun getMissingNumbersForGroup(groupIndex: Int): List<Int> {
        val group = workingStack.last().puzzle.getGroup(groupIndex)
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

    fun getCandidatesForCell(rowIndex: Int, columnIndex: Int): Set<Int> {
        var missingNumbersForRow = getMissingNumbersForRow(rowIndex)
        var missingNumbersForColumn = getMissingNumbersForColumn(columnIndex)
        var groupIndex = workingStack.last().puzzle.getGroupIndexForCell(rowIndex, columnIndex)
        var missingNumbersForGroup = getMissingNumbersForGroup(groupIndex)

        return missingNumbersForRow.intersect(missingNumbersForColumn).intersect(missingNumbersForGroup)
    }
}