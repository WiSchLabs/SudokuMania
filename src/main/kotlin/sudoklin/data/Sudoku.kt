package sudoklin.data

/**
 * Created by sebastian on 13.06.17.
 */
class Sudoku constructor(puzzle: SudokuPuzzle){
    val puzzle: SudokuPuzzle = puzzle
}

class SudokuPuzzle constructor(matrix: List<List<String>>) {
    val matrix: List<List<String>> = matrix

    fun getCell(x: Int, y: Int): String {
        return matrix.get(x).get(y)
    }
}