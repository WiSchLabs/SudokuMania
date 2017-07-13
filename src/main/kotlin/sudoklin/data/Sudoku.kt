package sudoklin.data

class Sudoku constructor(puzzle: SudokuPuzzle){
    val puzzle: SudokuPuzzle = puzzle

    fun clone(): Sudoku {
        return Sudoku(SudokuPuzzle(puzzle.matrix))
    }
}

class SudokuPuzzle constructor(matrix: Array<Array<String>>) {
    val matrix: Array<Array<String>> = matrix

    fun getCell(rowIndex: Int, columnIndex: Int): String {
        return matrix[rowIndex][columnIndex]
    }

    fun getRow(rowIndex: Int): Array<String> {
        return matrix[rowIndex]
    }

    fun getColumn(columnIndex: Int): Array<String> {
        val column: Array<String> = Array<String>(9, { _ -> "" })
        for (i in 0..8) {
            column[i] = matrix[i][columnIndex]
        }
        return column
    }

    fun getGroup(groupIndex: Int): Array<String> {
        val group: MutableList<String> = mutableListOf<String>()
        for (i in 0..2) {
            val rowIndex = i + groupIndex - (groupIndex % 3)
            for (j in 0..2) {
                val columnIndex = j + (groupIndex % 3) * 3
                group.add(matrix[rowIndex][columnIndex])
            }
        }
        return group.toTypedArray()
    }

    fun getGroupIndexForCell(rowIndex: Int, columnIndex: Int): Int {
        var groupIndex = columnIndex / 3
        groupIndex += (rowIndex % 3) * 3
        return groupIndex
    }
}