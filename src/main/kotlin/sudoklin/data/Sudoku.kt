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
}