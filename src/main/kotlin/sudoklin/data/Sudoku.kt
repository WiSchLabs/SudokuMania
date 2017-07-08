package sudoklin.data

class Sudoku constructor(puzzle: SudokuPuzzle){
    val puzzle: SudokuPuzzle = puzzle

    fun clone(): Sudoku {
        return Sudoku(SudokuPuzzle(puzzle.matrix.toMutableList()))
    }
}

class SudokuPuzzle constructor(matrix: List<List<String>>) {
    val matrix: List<List<String>> = matrix

    fun getCell(rowIndex: Int, columnIndex: Int): String {
        return matrix[rowIndex][columnIndex]
    }

    fun getRow(rowIndex: Int): List<String> {
        return matrix[rowIndex]
    }

    fun getColumn(columnIndex: Int): List<String> {
        var column: MutableList<String> = mutableListOf<String>()
        for (i in 0..8) {
            column.add(matrix.get(i).get(columnIndex))
        }
        return column
    }

    fun getGroup(groupIndex: Int): List<String> {
        var group: MutableList<String> = mutableListOf<String>()
        for (i in 0..2) {
            val rowIndex = i + groupIndex - (groupIndex % 3)
            for (j in 0..2) {
                val columnIndex = j + (groupIndex % 3) * 3
                group.add(matrix[rowIndex][columnIndex])
            }
        }
        return group
    }

    fun getGroupIndexForCell(rowIndex: Int, columnIndex: Int): Int {
        var groupIndex = columnIndex / 3
        groupIndex += (rowIndex % 3) * 3
        return groupIndex
    }
}