package sudoklin.impex

import sudoklin.data.Sudoku
import sudoklin.data.SudokuPuzzle
import java.io.File

class SudokuFileImporter {
    fun import(filename: String): Sudoku {
        var matrix: Array<Array<String>> = Array<Array<String>>(9, { size -> Array<String>(size, { _ -> "" }) })

        var i: Int = 0
        File(filename).forEachLine {
            line ->
                if (line[0] != '#') {
                    val splittedLine = line.trim().split("").filter { it != "" }
                    matrix[i++] = splittedLine.toTypedArray()
                }
        }

        return Sudoku(SudokuPuzzle(matrix))
    }
}