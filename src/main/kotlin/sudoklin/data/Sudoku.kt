package sudoklin.data

class Sudoku(val rows: Array<SudokuRow?> = Array(9, { _ -> null}),
             val columns: Array<SudokuColumn?> = Array(9, {_ -> null}),
             val groups: Array<SudokuGroup?> = Array(9, {_ -> null})) {

    fun clone(): Sudoku {
        return Sudoku(rows, columns, groups)
    }

    init {
        val cells: MutableList<SudokuCell> = ArrayList()
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                cells.add(SudokuCell(rowIndex, columnIndex))
            }
        }
        for (index in 0..8) {
            rows[index] = SudokuRow(cells.filter({ cell -> cell.rowIndex == index }).toTypedArray() )
            columns[index] = SudokuColumn(cells.filter({ cell -> cell.columnIndex == index }).toTypedArray() )
            groups[index] = SudokuGroup(cells.filter({ cell -> cell.groupIndex == index }).toTypedArray() )
        }
    }

    fun addSolvedNumber(rowIndex: Int, columnIndex: Int, number: Int) {
        val sudokuCell = rows[rowIndex]!!.cells[columnIndex]
        val groupIndex = sudokuCell.groupIndex

        sudokuCell.candidates.clear()
        sudokuCell.candidates.add(number)

        rows[rowIndex]!!.purgeCandidateNumberFromUnsolvedCells(number)
        columns[columnIndex]!!.purgeCandidateNumberFromUnsolvedCells(number)
        groups[groupIndex]!!.purgeCandidateNumberFromUnsolvedCells(number)
    }

    fun getCell(rowIndex: Int, columnIndex: Int): SudokuCell {
        return rows[rowIndex]!!.cells[columnIndex]
    }

    fun isSolved(): Boolean {
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                if (!getCell(rowIndex, columnIndex).isSolved())
                    return false
            }
        }
        return true
    }

    fun isValid(): Boolean {
        for (index in 0..8) {
            if (!rows[index]!!.isValid())
                return false
            if (!columns[index]!!.isValid())
                return false
            if (!groups[index]!!.isValid())
                return false
        }
        return true
    }

    override fun toString(): String {
        var s = "\r\n# # # # # # # # # # # # #\r\n"
        for (rowIndex in 0..8) {
            s += "#"
            for (columnIndex in 0..8) {
                s += " "
                s += getCell(rowIndex, columnIndex)
                if (columnIndex == 2 || columnIndex == 5) {
                    s += " |"
                }
            }
            s += " #\r\n"

            if (rowIndex == 2 || rowIndex == 5) {
                s += "# - - - + - - - + - - - #\r\n"
            }
        }
        s += "# # # # # # # # # # # # #\r\n"
        return s
    }
}

open class SudokuList constructor(val cells: Array<SudokuCell>) {
    fun containsSolvedNumber(number: Int): Boolean {
        cells.forEach { cell ->
            if (cell.isSolvedWithNumber(number))
                return true
        }
        return false
    }

    fun isSolved(): Boolean {
        return cells.all { cell -> cell.isSolved() }
    }

    fun isValid(): Boolean {
        val aggregatedNumbers: MutableSet<Int> = mutableSetOf()
        cells.forEach { cell ->
            if (cell.isSolved()) {
                if (aggregatedNumbers.contains(cell.candidates.first())) {
                    return false
                } else {
                    aggregatedNumbers.add(cell.candidates.first())
                }
            }
        }
        return true
    }

    fun purgeCandidateNumberFromUnsolvedCells(number: Int) {
        cells.filterNot { cell -> cell.isSolved() }
             .forEach { cell -> cell.candidates.remove(number) }
    }
}

class SudokuRow(cells: Array<SudokuCell>) : SudokuList(cells)
class SudokuColumn(cells: Array<SudokuCell>) : SudokuList(cells)
class SudokuGroup(cells: Array<SudokuCell>) : SudokuList(cells)

class SudokuCell constructor(val rowIndex: Int, val columnIndex: Int,
                             var candidates: MutableSet<Int> = mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) {
    val groupIndex: Int = getGroupIndexForCell(rowIndex, columnIndex)

    fun isSolvedWithNumber(number: Int): Boolean {
        return isSolved() && candidates.contains(number)
    }

    fun isSolved(): Boolean {
        return candidates.size == 1
    }

    private fun getGroupIndexForCell(rowIndex: Int, columnIndex: Int): Int {
        var groupIndex = columnIndex / 3
        groupIndex += (rowIndex / 3) * 3
        return groupIndex
    }

    override fun toString(): String {
        return if (isSolved())
            candidates.first().toString()
        else
            "."
    }
}
