package sudoklin.impex

import sudoklin.data.Sudoku
import sudoklin.data.SudokuPuzzle
import java.io.File

class SudokuFileImporter {
    fun import(filename: String): Sudoku {
        var matrix: MutableList<List<String>> = mutableListOf<List<String>>()

        File(filename).forEachLine {
            line ->
                if (line[0] != '#') {
                    val splittedLine = line.trim().split("").filter { it != "" }
                    matrix.add(splittedLine)
                }
        }

        return Sudoku(SudokuPuzzle(matrix))
    }
}