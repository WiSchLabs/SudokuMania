package sudoklin.solver

import sudoklin.data.Sudoku

class SudokuSolver(val initialSudoku: Sudoku) {
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

    private fun getMissingNumbersInList(list: Array<String>): List<Int> {
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (i in 1..9) {
            val number: String = "" + i
            if (!list.contains(number)) {
                missingNumbers.add(i)
            }
        }
        return missingNumbers
    }

    fun getCandidatesForCell(rowIndex: Int, columnIndex: Int): Array<Int> {
        val missingNumbersForRow = getMissingNumbersForRow(rowIndex)
        val missingNumbersForColumn = getMissingNumbersForColumn(columnIndex)
        val groupIndex = workingStack.last().puzzle.getGroupIndexForCell(rowIndex, columnIndex)
        val missingNumbersForGroup = getMissingNumbersForGroup(groupIndex)

        return missingNumbersForRow.intersect(missingNumbersForColumn).intersect(missingNumbersForGroup).toTypedArray()
    }

    fun solve(sudoku: Sudoku): Sudoku {
        val workingSudokuCopy = sudoku.clone()
        while(!workingSudokuCopy.puzzle.isSolved()) {
            for (rowIndex in 0..8) {
                for (columnIndex in 0..8) {
                    val candidates = getCandidatesForCell(rowIndex, columnIndex)
                    if (candidates.size == 1) {
                        workingSudokuCopy.puzzle.matrix[rowIndex][columnIndex] = candidates[0].toString()
                    }
                }
            }
        }
        return workingSudokuCopy
    }
}