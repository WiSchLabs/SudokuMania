package sudoklin.solver

import sudoklin.data.NewShinySudoku
import sudoklin.data.Sudoku
import sudoklin.data.SudokuList
import java.io.File

class SudokuSolver(initialSudoku: Sudoku, val log: Boolean = false) {
    val workingStack = ArrayList<Sudoku>()
    val timestamp: String = (System.currentTimeMillis() / (60 * 1000)).toString()

    init {
        workingStack.add(initialSudoku.clone())
    }

    fun getMissingNumbersForRow(rowIndex: Int): List<Int> {
        val puzzle = workingStack.last().puzzle
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!puzzle.fixedNumberIsInRow(rowIndex, number)) {
                missingNumbers.add(number)
            }
        }
        return missingNumbers
    }

    fun getMissingNumbersForColumn(columnIndex: Int): List<Int> {
        val puzzle = workingStack.last().puzzle
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!puzzle.fixedNumberIsInColumn(columnIndex, number)) {
                missingNumbers.add(number)
            }
        }
        return missingNumbers
    }

    fun getMissingNumbersForGroup(groupIndex: Int): List<Int> {
        val puzzle = workingStack.last().puzzle
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!puzzle.fixedNumberIsInGroup(groupIndex, number)) {
                missingNumbers.add(number)
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
        val alreadyInRow = workingPuzzle.fixedNumberIsInRow(rowIndex, number)

        if (log) File("./$timestamp.log").appendText("$number in row $rowIndex: " + row.joinToString() + "   $alreadyInRow")

        if (!alreadyInRow) {
            for (columnIndex in 0..8) {
                if (!workingPuzzle.cellIsFixed(rowIndex, columnIndex)) {
                    val alreadyInColumn = workingPuzzle.fixedNumberIsInColumn(columnIndex, number)

                    val groupIndex = workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex)
                    val alreadyInGoup = workingPuzzle.fixedNumberIsInGroup(groupIndex, number)

                    if (! (alreadyInColumn || alreadyInGoup)) {
                        possiblePositions.add(columnIndex)
                    }
                }
            }
        }

        if (log) File("./$timestamp.log").appendText("   $possiblePositions \r\n")

        return possiblePositions
    }

    fun getPossiblePositionsForNumberInColumn(number: Int, columnIndex: Int): List<Int> {
        val workingPuzzle = workingStack.last().puzzle
        val column = workingPuzzle.getColumn(columnIndex)
        val possiblePositions: MutableList<Int> = mutableListOf<Int>()
        val alreadyInColumn = workingPuzzle.fixedNumberIsInColumn(columnIndex, number)

        if (log) File("./$timestamp.log").appendText("$number in column $columnIndex: " + column.joinToString() + "   $alreadyInColumn")

        if (!alreadyInColumn) {
            for (rowIndex in 0..8) {
                if (!workingPuzzle.cellIsFixed(rowIndex, columnIndex)) {
                    val alreadyInRow = workingPuzzle.fixedNumberIsInRow(rowIndex, number)

                    val groupIndex = workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex)
                    val alreadyInGoup = workingPuzzle.fixedNumberIsInGroup(groupIndex, number)

                    if (! (alreadyInRow || alreadyInGoup)) {
                        possiblePositions.add(rowIndex)
                    }
                }
            }
        }

        if (log) File("./$timestamp.log").appendText("   $possiblePositions \r\n")

        return possiblePositions
    }

    fun getPossiblePositionsForNumberInGroup(number: Int, groupIndex: Int): List<Pair<Int, Int>> {
        val workingPuzzle = workingStack.last().puzzle
        val group = workingPuzzle.getGroup(groupIndex)
        val possiblePositions: MutableList<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
        val alreadyInGoup = workingPuzzle.fixedNumberIsInGroup(groupIndex, number)

        if (log) File("./$timestamp.log").appendText("$number in group $groupIndex: " + group.joinToString() + "   $alreadyInGoup")

        if (!alreadyInGoup) {
            for (rowIndex in 0..8) {
                for (columnIndex in 0..8) {
                    if (workingPuzzle.getGroupIndexForCell(rowIndex, columnIndex) == groupIndex) {
                        if (!workingPuzzle.cellIsFixed(rowIndex, columnIndex)) {
                            val alreadyInRow = workingPuzzle.fixedNumberIsInRow(rowIndex, number)
                            val alreadyInColumn = workingPuzzle.fixedNumberIsInColumn(columnIndex, number)

                            if (! (alreadyInRow || alreadyInColumn)) {
                                possiblePositions.add(Pair(rowIndex, columnIndex))
                            }
                        }
                    }
                }
            }
        }

        if (log) File("./$timestamp.log").appendText("   $possiblePositions \r\n")

        return possiblePositions
    }

    fun solve(): Sudoku {
        var iteration = 0
        if (log) File("./$timestamp.log").appendText("INITIAL" + workingStack.last().puzzle.toString())
        while(! workingStack.last().puzzle.isSolved()) {
            // TODO refactor:
            // use simple method until no more progress,
            // then use next more complicated method,
            // anytime a number is found, start over from step 1 again

//            /*1*/ fillOnlyPossibleValueForCells()
//            if (log) File("./$timestamp.log").appendText("Step $iteration a:" + workingStack.last().puzzle.toString())

            /*2*/ fillNumbersToTheirOnlyPossibleCell()
            if (log) File("./$timestamp.log").appendText("Step $iteration b:" + workingStack.last().puzzle.toString())
//            /*3*/ findPairsInCandidatesAndEliminateOthers()
//            if (log) File("./$timestamp.log").appendText("Step $iteration c:" + workingStack.last().puzzle.toString())
            iteration++
        }
        return workingStack.last()
    }

    private fun fillNumbersToTheirOnlyPossibleCell() {
        for (number in 1..9) {
            for (i in 0..8) {
                val possiblePositionsInRow = getPossiblePositionsForNumberInRow(number, i)
                if (possiblePositionsInRow.size == 1) {
                    workingStack.last().puzzle.matrix[i][possiblePositionsInRow[0]] = IntArray(1, {number})
                }
                if (log) File("./$timestamp.log").appendText(" I: $number in row" + workingStack.last().puzzle.toString())

                val possiblePositionsInColumn = getPossiblePositionsForNumberInColumn(number, i)
                if (possiblePositionsInColumn.size == 1) {
                    workingStack.last().puzzle.matrix[possiblePositionsInColumn[0]][i] = IntArray(1, {number})
                }
                if (log) File("./$timestamp.log").appendText(" II: $number in column" + workingStack.last().puzzle.toString())

                val possiblePositionsInGroup = getPossiblePositionsForNumberInGroup(number, i)
                if (possiblePositionsInGroup.size == 1) {
                    workingStack.last().puzzle.matrix[possiblePositionsInGroup[0].first][possiblePositionsInGroup[0].second] = IntArray(1, {number})
                }
                if (log) File("./$timestamp.log").appendText(" III: $number in group" + workingStack.last().puzzle.toString())
            }
        }
    }
}

class NewSudokuSolver(val sudoku: NewShinySudoku, private val log: Boolean = false) {
    private val timestamp: String = (System.currentTimeMillis() / (60 * 1000)).toString()

    fun getMissingNumbersForRow(rowIndex: Int): List<Int> {
        val puzzle = sudoku
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!puzzle.rows[rowIndex]!!.containsSolvedNumber(number)) {
                missingNumbers.add(number)
            }
        }
        return missingNumbers
    }

    fun getMissingNumbersForColumn(columnIndex: Int): List<Int> {
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!sudoku.columns[columnIndex]!!.containsSolvedNumber(number)) {
                missingNumbers.add(number)
            }
        }
        return missingNumbers
    }

    fun getMissingNumbersForGroup(groupIndex: Int): List<Int> {
        val missingNumbers: MutableList<Int> = mutableListOf<Int>()
        for (number in 1..9) {
            if (!sudoku.groups[groupIndex]!!.containsSolvedNumber(number)) {
                missingNumbers.add(number)
            }
        }
        return missingNumbers
    }

    fun getPossiblePositionsForNumberInRow(number: Int, rowIndex: Int): List<Int> {
        val row = sudoku.rows[rowIndex]

        val alreadyInRow = sudoku.rows[rowIndex]!!.containsSolvedNumber(number)
        if (log) File("./$timestamp.log").appendText("$number in row $rowIndex: " + row.toString() + "   $alreadyInRow")

        if (!alreadyInRow) {
            return sudoku.rows[rowIndex]!!.cells.filter { it.candidates.contains(number) }.map { it.columnIndex }
        }

        return listOf()
    }

    fun getPossiblePositionsForNumberInColumn(number: Int, columnIndex: Int): List<Int> {
        val column = sudoku.columns[columnIndex]

        val alreadyInColumn = sudoku.columns[columnIndex]!!.containsSolvedNumber(number)
        if (log) File("./$timestamp.log").appendText("$number in column $columnIndex: " + column.toString() + "   $alreadyInColumn")

        if (!alreadyInColumn) {
            return sudoku.columns[columnIndex]!!.cells.filter { it.candidates.contains(number) }.map { it.rowIndex }
        }

        return listOf()
    }

    fun getPossiblePositionsForNumberInGroup(number: Int, groupIndex: Int): List<Pair<Int, Int>> {
        val group = sudoku.groups[groupIndex]

        val alreadyInGroup = sudoku.groups[groupIndex]!!.containsSolvedNumber(number)
        if (log) File("./$timestamp.log").appendText("$number in group $groupIndex: " + group.toString() + "   $alreadyInGroup")

        if (!alreadyInGroup) {
            return sudoku.groups[groupIndex]!!.cells.filter { it.candidates.contains(number) }.map { Pair(it.rowIndex, it.columnIndex) }
        }

        return listOf()
    }

    fun solve(): NewShinySudoku {
        var iteration = 0
        if (log) File("./$timestamp.log").appendText("INITIAL" + sudoku.toString())
        while(!sudoku.isSolved()) {
            // TODO refactor:
            // use simple method until no more progress,
            // then use next more complicated method,
            // anytime a number is found, start over from step 1 again

            /*2*/
            var changed = fillNumbersToTheirOnlyPossibleCell()
            if (log) File("./$timestamp.log").appendText("Step $iteration b:" + sudoku.toString())
            /*3*/ changed = changed || findPairsOfCandidatesAndEliminateOthers()
            if (log) File("./$timestamp.log").appendText("Step $iteration c:" + sudoku.toString())
            iteration++
            if (!changed)
                return sudoku
        }
        return sudoku
    }

    private fun fillNumbersToTheirOnlyPossibleCell(): Boolean {
        var changed = false
        for (number in 1..9) {
            for (index in 0..8) {
                val possiblePositionsInRow = getPossiblePositionsForNumberInRow(number, index)
                if (possiblePositionsInRow.size == 1 && !sudoku.getCell(index, possiblePositionsInRow[0]).isSolved()) {

                    sudoku.addSolvedNumber(index, possiblePositionsInRow[0], number)
                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" I: $number in row" + sudoku.toString())

                val possiblePositionsInColumn = getPossiblePositionsForNumberInColumn(number, index)
                if (possiblePositionsInColumn.size == 1 && !sudoku.getCell(possiblePositionsInColumn[0], index).isSolved()) {

                    sudoku.addSolvedNumber(possiblePositionsInColumn[0], index, number)

                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" II: $number in column" + sudoku.toString())

                val possiblePositionsInGroup = getPossiblePositionsForNumberInGroup(number, index)
                if (possiblePositionsInGroup.size == 1 && !sudoku.getCell(possiblePositionsInGroup[0].first, possiblePositionsInGroup[0].second).isSolved()) {
                    val c = sudoku.getCell(possiblePositionsInGroup[0].first,
                            possiblePositionsInGroup[0].second)

                    sudoku.addSolvedNumber(c.rowIndex, c.columnIndex, number)

                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" III: $number in group" + sudoku.toString())
            }
        }
        return changed
    }

    private fun findPairsOfCandidatesAndEliminateOthers(): Boolean {
        var changed = false
        for (row in sudoku.rows) {
            changed = changed || findPairsOfCandidatesInList(row)
        }
        for (column in sudoku.columns) {
            changed = changed || findPairsOfCandidatesInList(column)
        }
        for (group in sudoku.groups) {
            changed = changed || findPairsOfCandidatesInList(group)
        }
        return changed
    }

    private fun findPairsOfCandidatesInList(list: SudokuList?): Boolean {
        var changed = false
        val twoItemCellsInList = list!!.cells.filter { it.candidates.size == 2 }
        for (cell in twoItemCellsInList) {
            val cellsWithEqualCandidates = twoItemCellsInList.filterNot { it == cell }.filter { it.candidates.containsAll(cell.candidates) }
            if (cellsWithEqualCandidates.size == 1) { // if there are more, the puzzle is not solvable
                list.cells.filterNot { it == cell || it == cellsWithEqualCandidates[0] }.forEach { it.candidates.removeAll(cell.candidates) }
                if (log) File("./$timestamp.log").appendText("Found Pair ${cell.candidates} in ${cell.rowIndex}-${cell.columnIndex} and ${cellsWithEqualCandidates[0].rowIndex}-${cellsWithEqualCandidates[0].columnIndex}\n")
                changed = true
            }
        }
        return changed
    }
}