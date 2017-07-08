package sudoklin.data

class Sudoku constructor(puzzle: SudokuPuzzle){
    val puzzle: SudokuPuzzle = puzzle
}

class SudokuPuzzle constructor(matrix: List<List<String>>) {
    val matrix: List<List<String>> = matrix

    fun getCell(x: Int, y: Int): String {
        return matrix.get(x).get(y)
    }

    fun getRow(rowNumber: Int): List<String> {
        return matrix[rowNumber]
    }

    fun getColumn(columnNumber: Int): List<String> {
        var column: MutableList<String> = mutableListOf<String>()
        for (i in 0..8) {
            column.add(matrix.get(i).get(columnNumber))
        }
        return column
    }

    fun getGroup(groupIndex: Int): List<String> {
        var group: MutableList<String> = mutableListOf<String>()
        for (i in 0..2) {
            val rowIndex = i + groupIndex - (groupIndex % 3)
            for (j in 0..2) {
                val columnIndex = j + (groupIndex % 3) * 3
                group.add(matrix.get(rowIndex).get(columnIndex))
            }
        }
        return group
    }

    fun  valid_numbers(x: Int, y: Int): Int {
        return -1;
    }
}