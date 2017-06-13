package sudoklin.impex

import sudoklin.data.Sudoku
import sudoklin.data.SudokuPuzzle
import java.io.File

/**
 * Created by sebastian on 13.06.17.
 */
class SudokuFileImporter {

    fun import(filename: String): Sudoku {
        val fileContent = File(filename).readText()
        val textLines = fileContent.split("\\r\\n|\\n|\\r")
        var matrix: MutableList<List<String>> = mutableListOf<List<String>>()
        for (textLine in textLines) {
            if (textLine[0] != '#') {
                val splittedLine = textLine.split("")
                matrix.add(splittedLine)
            }
        }
        return Sudoku(SudokuPuzzle(matrix))
    }

}