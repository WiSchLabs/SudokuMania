package sudoklin.impex

import sudoklin.data.Sudoku
import sudoklin.data.SudokuPuzzle
import java.io.File

class SudokuFileImporter {
    fun import(filename: String): Sudoku {
        val matrix: Array<Array<IntArray>> = Array<Array<IntArray>>(9, {
                _ -> Array<IntArray>(9, {
                    IntArray(9, { it * 1 })
                })
            })

        var rowCounter: Int = 0
        var columnCounter: Int = 0
        File(filename).forEachLine {
            line ->
            if (line[0] != '#') {
                val splittedLine = line.trim().split("").filter { it != "" }
                val input: Array<String>
                input = splittedLine.toTypedArray()
                for (literal in input) {
                    if (literal != ".") {
                        matrix[rowCounter][columnCounter] = kotlin.IntArray(1, { _ -> Integer.parseInt(literal) })
                    }
                    columnCounter++
                }
                rowCounter++
                columnCounter = 0
            }
        }

        return Sudoku(SudokuPuzzle(matrix))
    }
}