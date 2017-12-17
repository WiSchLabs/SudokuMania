package sudoklin.solver

import sudoklin.data.Sudoku
import sudoklin.data.SudokuGroup
import sudoklin.data.SudokuList
import java.io.File

class SudokuSolver(val sudoku: Sudoku, private val log: Boolean = false) {
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

    fun solve(): Sudoku {
        var iteration = 0
        if (log) File("./$timestamp.log").appendText("INITIAL" + sudoku.toString())
        while (!sudoku.isSolved()) {
            // TODO refactor:
            // use simple method until no more progress,
            // then use next more complicated method,
            // anytime a number is found, start over from step 1 again

            /*2*/
            var changed = fillNumbersToTheirOnlyPossibleCell()
            if (log) File("./$timestamp.log").appendText("Step $iteration b:" + sudoku.toString())
            /*3*/ changed = changed || cleanCombinationsOfCandidates()
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
                    val cell = sudoku.getCell(index, possiblePositionsInRow[0])

                    sudoku.addSolvedNumber(cell, number)

                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" I: $number in row" + sudoku.toString())

                val possiblePositionsInColumn = getPossiblePositionsForNumberInColumn(number, index)
                if (possiblePositionsInColumn.size == 1 && !sudoku.getCell(possiblePositionsInColumn[0], index).isSolved()) {
                    val cell = sudoku.getCell(possiblePositionsInColumn[0], index)

                    sudoku.addSolvedNumber(cell, number)

                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" II: $number in column" + sudoku.toString())

                val possiblePositionsInGroup = getPossiblePositionsForNumberInGroup(number, index)
                if (possiblePositionsInGroup.size == 1 && !sudoku.getCell(possiblePositionsInGroup[0].first, possiblePositionsInGroup[0].second).isSolved()) {
                    val cell = sudoku.getCell(possiblePositionsInGroup[0].first, possiblePositionsInGroup[0].second)

                    sudoku.addSolvedNumber(cell, number)

                    changed = true
                }
                if (log) File("./$timestamp.log").appendText(" III: $number in group" + sudoku.toString())
            }
        }
        return changed
    }

    fun cleanCombinationsOfCandidates(): Boolean {
        var changed = false
        for (row in sudoku.rows) {
            changed = cleanCombinationsOfCandidatesInList(row) || changed
        }
        for (column in sudoku.columns) {
            changed = cleanCombinationsOfCandidatesInList(column) || changed
        }
        for (group in sudoku.groups) {
            changed = cleanCombinationsOfCandidatesInList(group) || changed
        }
        return changed
    }

    private fun cleanCombinationsOfCandidatesInList(list: SudokuList?): Boolean {
        val sumOfCandidatesBefore = list!!.cells.sumBy { it.candidates.size }
        for (combinationsSize in 2..8) {
            val cellsWithCombinationSize = list.cells.filter { it.candidates.size == combinationsSize }
            for (cell in cellsWithCombinationSize) {
                val cellsWithEqualCandidates = cellsWithCombinationSize.filterNot { it == cell }
                                                                       .filter { it.candidates.containsAll(cell.candidates) }

                if (cellsWithEqualCandidates.size > combinationsSize - 1) {
                    throw Exception("This Sudoku is unsolvable!!! " +
                            "${cellsWithEqualCandidates.size} > ${combinationsSize - 1} " +
                            "in ${list.javaClass.name} [${cell.rowIndex},${cell.columnIndex}]")
                }

                if (cellsWithEqualCandidates.size == combinationsSize - 1) {
                    for (number in cell.candidates) {
                        for (cellToAdjust in list.cells.filterNot { it == cell || it in cellsWithEqualCandidates }) {
                            sudoku.removeCandidateFromCell(cellToAdjust, number)
                        }
                    }

                    if (log) File("./$timestamp.log").appendText(
                            "Found Pair ${cell.candidates} in ${cell.rowIndex}-${cell.columnIndex}"
                    )
                }
            }
        }
        val sumOfCandidatesAfter = list.cells.sumBy { it.candidates.size }
        return sumOfCandidatesBefore != sumOfCandidatesAfter
    }

    fun cleanCandidateConstraintsInOtherGroups() {
        for (number in 1..9) {
            for (group in sudoku.groups) {
                group!!
                val rowIndexesOfGroupCellsContainingGivenNumber = getRowIndexesOfGroupCellsContainingGivenNumber(group, number)
                if (rowIndexesOfGroupCellsContainingGivenNumber.size == 1) {
                    val row = sudoku.rows[rowIndexesOfGroupCellsContainingGivenNumber.first()]
                    row!!.cells.filterNot { cell -> cell in group.cells }
                               .forEach { cell -> sudoku.removeCandidateFromCell(cell, number) }
                }
                val columnIndexesOfGroupCellsContainingGivenNumber = getColumnIndexesOfGroupCellsContainingGivenNumber(group, number)
                if (columnIndexesOfGroupCellsContainingGivenNumber.size == 1) {
                    val column = sudoku.columns[columnIndexesOfGroupCellsContainingGivenNumber.first()]
                    column!!.cells.filterNot { cell -> cell in group.cells }
                                  .forEach { cell -> sudoku.removeCandidateFromCell(cell, number) }
                }
            }
        }
    }

    private fun getRowIndexesOfGroupCellsContainingGivenNumber(group: SudokuGroup, number: Int): Set<Int> {
        val groupCellsContainingGivenNumber = group.cells.filter { cell -> cell.candidates.contains(number) }
        return groupCellsContainingGivenNumber.groupBy { cell -> cell.rowIndex }.keys
    }

    private fun getColumnIndexesOfGroupCellsContainingGivenNumber(group: SudokuGroup, number: Int): Set<Int> {
        val groupCellsContainingGivenNumber = group.cells.filter { cell -> cell.candidates.contains(number) }
        return groupCellsContainingGivenNumber.groupBy { cell -> cell.columnIndex }.keys
    }
}
