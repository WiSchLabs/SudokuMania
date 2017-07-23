package sudoklin.data

class Sudoku (val puzzle: SudokuPuzzle){
    fun clone(): Sudoku {
        return Sudoku(SudokuPuzzle(puzzle.matrix))
    }
}

class SudokuPuzzle (val matrix: Array<Array<String>>) {
    fun getCell(rowIndex: Int, columnIndex: Int): String {
        return matrix[rowIndex][columnIndex]
    }

    fun getRow(rowIndex: Int): Array<String> {
        return matrix[rowIndex]
    }

    fun getColumn(columnIndex: Int): Array<String> {
        val column: Array<String> = Array<String>(9, { _ -> "" })
        for (rowIndex in 0..8) {
            column[rowIndex] = matrix[rowIndex][columnIndex]
        }
        return column
    }

    fun getGroup(groupIndex: Int): Array<String> {
        val group: MutableList<String> = mutableListOf<String>()
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
            if (matrix[rowIndex].toList().contains(".")) {
                return false
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