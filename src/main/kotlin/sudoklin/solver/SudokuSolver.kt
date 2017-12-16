package sudoklin.solver

import sudoklin.data.Sudoku
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

    fun findPairsOfCandidatesAndEliminateOthers(): Boolean {
        var changed = false
        for (row in sudoku.rows) {
            changed = findPairsOfCandidatesInList(row) || changed
        }
        for (column in sudoku.columns) {
            changed = findPairsOfCandidatesInList(column) || changed
        }
        for (group in sudoku.groups) {
            changed = findPairsOfCandidatesInList(group) || changed
        }
        return changed
    }

    private fun findPairsOfCandidatesInList(list: SudokuList?): Boolean {
        val sumOfCandidatesBefore = list!!.cells.sumBy { it.candidates.size }
        val twoItemCellsInList = list.cells.filter { it.candidates.size == 2 }

        for (cell in twoItemCellsInList) {
            val cellsWithEqualCandidates = twoItemCellsInList.filterNot { it == cell }
                                                             .filter { it.candidates.containsAll(cell.candidates) }

            if (cellsWithEqualCandidates.size > 1)
                throw Exception("This Sudoku is unsolvable!!!")

            if (cellsWithEqualCandidates.size == 1) {
                list.cells.filterNot { it == cell || it == cellsWithEqualCandidates[0] }
                          .forEach { it.candidates.removeAll(cell.candidates) }

                if (log) File("./$timestamp.log").appendText("Found Pair ${cell.candidates} in ${cell.rowIndex}-${cell.columnIndex} and ${cellsWithEqualCandidates[0].rowIndex}-${cellsWithEqualCandidates[0].columnIndex}\n")
            }
        }
        val sumOfCandidatesAfter = list.cells.sumBy { it.candidates.size }
        return sumOfCandidatesBefore != sumOfCandidatesAfter
    }
}