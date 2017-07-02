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
}