package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals

class SudokuSolverTest : Spek({
    describe("A sudoku") {
        val file_importer = SudokuFileImporter()

        describe("filled") {
            describe("valid") {
                it("should return no missing numbers for the row") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInRow = solver.getMissingNumbersForRow(sudoku, 1)
                    assertEquals(0, missingNumbersInRow.size)
                }

                it("should return no missing numbers for the column") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInColumn = solver.getMissingNumbersForColumn(sudoku, 1)
                    assertEquals(0, missingNumbersInColumn.size)
                }

                it("should return no missing numbers for the group") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersInGroup = solver.getMissingNumbersForGroup(sudoku, 1)
                    assertEquals(0, missingNumbersInGroup.size)
                }
            }
        }

        describe("unfilled") {
            describe("valid") {
                it("should return the number 2 for the first cell") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val candidatesForCell = solver.getCandidatesForCell(sudoku, 0, 0)
                    assertEquals(1, candidatesForCell.size)
                    assertEquals(2, candidatesForCell.first())
                }

                it("should return the number 2 for the first row") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersForRow = solver.getMissingNumbersForRow(sudoku, 0)
                    assertEquals(1, missingNumbersForRow.size)
                    assertEquals(2, missingNumbersForRow[0])
                }

                it("should return the number 2 for the first column") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersForColumn = solver.getMissingNumbersForColumn(sudoku, 0)
                    assertEquals(1, missingNumbersForColumn.size)
                    assertEquals(2, missingNumbersForColumn[0])
                }

                it("should return the number 2 for the first group") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver()
                    val missingNumbersForGroup = solver.getMissingNumbersForGroup(sudoku, 0)
                    assertEquals(1, missingNumbersForGroup.size)
                    assertEquals(2, missingNumbersForGroup[0])
                }
            }
        }
    }
})
