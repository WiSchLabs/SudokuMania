package sudoklin.impex

import sudoklin.data.Sudoku
import java.io.File

class SudokuFileImporter {
    fun import(filename: String): Sudoku {
        val sudoku = Sudoku()

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
                        val number = Integer.parseInt(literal)
                        sudoku.addSolvedNumber(rowCounter, columnCounter, number)
                    }
                    columnCounter++
                }
                rowCounter++
                columnCounter = 0
            }
        }
        return sudoku
    }
}
