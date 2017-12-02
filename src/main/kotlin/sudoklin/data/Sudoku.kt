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

    fun cellIsInGroup(rowIndex: Int, columnIndex: Int, groupIndex: Int): Boolean {
        return getGroupIndexForCell(rowIndex, columnIndex) == groupIndex
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

class SudokuRow(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}
class SudokuColumn(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}
class SudokuGroup(index: Int, cells: Array<SudokuCell>) : SudokuList(index, cells) {}
open class SudokuList constructor(val index: Int, val cells: Array<SudokuCell>) {}

class SudokuCell constructor(val rowIndex: Int, val columnIndex: Int, val candidates: IntArray = IntArray(9, { it * 1 })) {
    val groupIndex: Int = getGroupIndexForCell(rowIndex, columnIndex)

    private fun getGroupIndexForCell(rowIndex: Int, columnIndex: Int): Int {
        var groupIndex = columnIndex / 3
        groupIndex += (rowIndex / 3) * 3
        return groupIndex
    }
}