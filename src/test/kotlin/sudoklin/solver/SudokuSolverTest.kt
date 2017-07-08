package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SudokuSolverTest : Spek({
    describe("A sudoku") {
        val file_importer = SudokuFileImporter()

        describe("filled") {
            describe("valid") {
                it("should return no missing numbers for the row") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInRow = solver.getMissingNumbersInRow(sudoku, 1)
                    assertEquals(0, missingNumbersInRow.size)
                }

                it("should return no missing numbers for the column") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInColumn = solver.getMissingNumbersInColumn(sudoku, 1)
                    assertEquals(0, missingNumbersInColumn.size)
                }

                it("should return no missing numbers for the group") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInGroup = solver.getMissingNumbersInGroup(sudoku, 1)
                    assertEquals(0, missingNumbersInGroup.size)
                }
            }
        }

        describe("unfilled") {
            describe("valid") {
                it("should return the number 2 for the first row") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInRow = solver.getMissingNumbersInRow(sudoku, 0)
                    assertEquals(1, missingNumbersInRow.size)
                    assertEquals(2, missingNumbersInRow[0])
                }

                it("should return the number 2 for the first column") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInColumn = solver.getMissingNumbersInColumn(sudoku, 0)
                    assertEquals(1, missingNumbersInColumn.size)
                    assertEquals(2, missingNumbersInColumn[0])
                }

                it("should return the number 2 for the first group") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInGroup = solver.getMissingNumbersInGroup(sudoku, 0)
                    assertEquals(1, missingNumbersInGroup.size)
                    assertEquals(2, missingNumbersInGroup[0])
                }
            }
        }
    }
})
