package sudoklin.data

class Sudoku (val puzzle: SudokuPuzzle){
    fun clone(): Sudoku {
        return Sudoku(SudokuPuzzle(puzzle.matrix))
    }
}

class SudokuPuzzle (val matrix: Array<Array<IntArray>>) {
    fun getCell(rowIndex: Int, columnIndex: Int): IntArray {
        return matrix[rowIndex][columnIndex]
    }

    fun fixedNumberIsInCell(rowIndex: Int, columnIndex: Int, number: Int): Boolean {
        val cell = getCell(rowIndex, columnIndex)
        return cellIsFixed(rowIndex, columnIndex) && cell[0] == number
    }


    fun cellIsFixed(rowIndex: Int, columnIndex: Int): Boolean {
        val cell = getCell(rowIndex, columnIndex)
        return cell.size == 1
    }

    fun fixedNumberIsInColumn(columnIndex: Int, number: Int): Boolean {
        for (rowIndex in 0..8) {
            if (fixedNumberIsInCell(rowIndex, columnIndex, number))
                return true
        }
        return false
    }

    fun fixedNumberIsInRow(rowIndex: Int, number: Int): Boolean {
        for (columnIndex in 0..8) {
            if (fixedNumberIsInCell(rowIndex, columnIndex, number))
                return true
        }
        return false
    }

    fun fixedNumberIsInGroup(groupIndex: Int, number: Int): Boolean {
        val group = getGroup(groupIndex)
        for (cell in group) {
            if (cell.size == 1 && cell[0] == number) {
                return true
            }
        }
        return false
    }


    fun getRow(rowIndex: Int): Array<IntArray> {
        return matrix[rowIndex]
    }

    fun getColumn(columnIndex: Int): Array<IntArray> {
        val column: Array<IntArray> = Array<IntArray>(9, { IntArray(9) })
        for (rowIndex in 0..8) {
            column[rowIndex] = matrix[rowIndex][columnIndex]
        }
        return column
    }

    fun getGroup(groupIndex: Int): Array<IntArray> {
        val group: MutableList<IntArray> = mutableListOf<IntArray>()
        for (i in 0..2) {
            val rowIndex = i + ((groupIndex / 3) * 3)
            for (j in 0..2) {
                val columnIndex = j + ((groupIndex % 3) * 3)
                group.add(matrix[rowIndex][columnIndex])
            }
        }
        return group.toTypedArray()
    }

    fun getGroupIndexForCell(rowIndex: Int, columnIndex: Int): Int {
        var groupIndex = columnIndex / 3
        groupIndex += (rowIndex / 3) * 3
        return groupIndex
    }

    fun isSolved(): Boolean {
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                if (matrix[rowIndex][columnIndex].size > 1) {
                    return false
                }
            }
        }
        return true
    }

    override fun toString(): String {
        var s = "\r\n# # # # # # # # # # # # #\r\n"
        for (rowIndex in 0..8) {
            s += "#"
            for (columnIndex in 0..8) {
                s += " "
                s += getCell(rowIndex, columnIndex).toString()
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

class NewShinySudoku() {
    val rows: Array<SudokuRow?> = Array(9, {_ -> null})
    val columns: Array<SudokuColumn?> = Array(9, {_ -> null})
    val groups: Array<SudokuGroup?> = Array(9, {_ -> null})

    init {
        val cells: MutableList<SudokuCell> = ArrayList()
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                cells.add(SudokuCell(rowIndex, columnIndex))
            }
        }
        for (index in 0..8) {
            rows[index] = SudokuRow(index, cells.filter({ cell -> cell.rowIndex == index }).toTypedArray() )
            columns[index] = SudokuColumn(index, cells.filter({ cell -> cell.columnIndex == index }).toTypedArray() )
            groups[index] = SudokuGroup(index, cells.filter({ cell -> cell.groupIndex == index }).toTypedArray() )
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

open class SudokuList constructor(val index: Int, val cells: Array<SudokuCell>) {
    fun containsSolvedNumber(number: Int): Boolean {
        for (cell in cells) {
            if (cell.isSolvedWithNumber(number))
                return true
        }
        return false
    }

    fun isSolved() : Boolean {
        for (cell in cells) {
            if (!cell.isSolved())
                return false
        }
        return true
    }

    fun purgeCandidateNumberFromUnsolvedCells(number: Int) {
        for (cell in cells) {
            if (!cell.isSolved())
                cell.candidates.remove(number)
        }
    }
}

class SudokuRow(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}
class SudokuColumn(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}
class SudokuGroup(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}

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