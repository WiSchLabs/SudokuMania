package sudoklin.solver

import sudoklin.data.Sudoku

class SudokuSolver(initialSudoku: Sudoku) {
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

    fun getPossiblePositionsForNumberInRow(number: Int, rowIndex: Int): List<Int> {
        val workingPuzzle = workingStack.last().puzzle
        val row = workingPuzzle.getRow(rowIndex)
        val possiblePositions: MutableList<Int> = mutableListOf<Int>()
        val alreadyInRow = row.toList().contains(number.toString())

        if (!alreadyInRow) {
            for (columnIndex in 0..8) {
                val cellIsUnsolved = row[columnIndex] == "."
                if (cellIsUnsolved) {
                    val alreadyInColumn = workingPuzzle.getColumn(columnIndex).toList().contains(number.toString())

                    val groupIndex = workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex)
                    val alreadyInGoup = workingPuzzle.getGroup(groupIndex).toList().contains(number.toString())

                    if (! (alreadyInColumn || alreadyInGoup)) {
                        possiblePositions.add(columnIndex)
                    }
                }
            }
        }

        return possiblePositions
    }

    fun getPossiblePositionsForNumberInColumn(number: Int, columnIndex: Int): List<Int> {
        val workingPuzzle = workingStack.last().puzzle
        val column = workingPuzzle.getColumn(columnIndex)
        val possiblePositions: MutableList<Int> = mutableListOf<Int>()
        val alreadyInColumn = column.toList().contains(number.toString())

        if (!alreadyInColumn) {
            for (rowIndex in 0..8) {
                val cellIsUnsolved = column[rowIndex] == "."
                if (cellIsUnsolved) {
                    val alreadyInRow = workingPuzzle.getRow(rowIndex).toList().contains(number.toString())

                    val groupIndex = workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex)
                    val alreadyInGoup = workingPuzzle.getGroup(groupIndex).toList().contains(number.toString())

                    if (! (alreadyInRow || alreadyInGoup)) {
                        possiblePositions.add(rowIndex)
                    }
                }
            }
        }

        return possiblePositions
    }

    fun getPossiblePositionsForNumberInGroup(number: Int, groupIndex: Int): List<Pair<Int, Int>> {
        val workingPuzzle = workingStack.last().puzzle
        val group = workingPuzzle.getGroup(groupIndex)
        val possiblePositions: MutableList<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
        val alreadyInGoup = group.toList().contains(number.toString())

        if (!alreadyInGoup) {
            for (rowIndex in 0..8) {
                for (columnIndex in 0..8) {
                    if (workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex) == groupIndex) {
                        val cellIsUnsolved = workingPuzzle.getCell(rowIndex, columnIndex) == "."
                        if (cellIsUnsolved) {
                            val alreadyInRow = workingPuzzle.getRow(rowIndex).toList().contains(number.toString())
                            val alreadyInColumn = workingPuzzle.getColumn(columnIndex).toList().contains(number.toString())

                            if (! (alreadyInRow || alreadyInColumn)) {
                                possiblePositions.add(Pair(rowIndex, columnIndex))
                            }
                        }
                    }
                }
            }
        }

        return possiblePositions
    }

    fun solve(): Sudoku {
        while(! workingStack.last().puzzle.isSolved()) {
            // TODO refactor:
            // use simple method until no more progress,
            // then use next more complicated method,
            // anytime a number is found, start over from step 1 again

            /*1*/ fillOnlyPossibleValueForCells()
            /*2*/ fillNumbersToTheirOnlyPossibleCell()
//            /*3*/ findPairsInCandidatesAndEliminateOthers()
        }
        return workingStack.last()
    }

    private fun fillOnlyPossibleValueForCells() {
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                val cellIsUnsolved = workingStack.last().puzzle.matrix[rowIndex][columnIndex] == "."
                if (cellIsUnsolved) {
                    val candidates = getCandidatesForCell(rowIndex, columnIndex)
                    if (candidates.size == 1) {
                        workingStack.last().puzzle.matrix[rowIndex][columnIndex] = candidates[0].toString()
                    }
                }
            }
        }
    }

    private fun fillNumbersToTheirOnlyPossibleCell() {
        for (number in 1..9) {
            for (i in 0..8) {
                val possiblePositionsInRow = getPossiblePositionsForNumberInRow(number, i)
                if (possiblePositionsInRow.size == 1) {
                    workingStack.last().puzzle.matrix[i][possiblePositionsInRow[0]] = number.toString()
                }
                val possiblePositionsInColumn = getPossiblePositionsForNumberInColumn(number, i)
                if (possiblePositionsInColumn.size == 1) {
                    workingStack.last().puzzle.matrix[i][possiblePositionsInColumn[0]] = number.toString()
                }
                val possiblePositionsInGroup = getPossiblePositionsForNumberInGroup(number, i)
                if (possiblePositionsInGroup.size == 1) {
                    workingStack.last().puzzle.matrix[possiblePositionsInGroup[0].first][possiblePositionsInGroup[0].second] = number.toString()
                }
            }
        }
    }
}