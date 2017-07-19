package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.data.Sudoku
import sudoklin.impex.SudokuFileImporter
import sudoklin.validator.SudokuValidator
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SudokuSolverTest : Spek({
    describe("A sudoku") {
        val file_importer = SudokuFileImporter()

        describe("filled") {
            describe("valid") {
                it("should return no missing numbers for the row") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInRow = solver.getMissingNumbersForRow(1)
                    assertEquals(0, missingNumbersInRow.size)
                }

                it("should return no missing numbers for the column") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInColumn = solver.getMissingNumbersForColumn(1)
                    assertEquals(0, missingNumbersInColumn.size)
                }

                it("should return no missing numbers for the group") {
                    val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInGroup = solver.getMissingNumbersForGroup(1)
                    assertEquals(0, missingNumbersInGroup.size)
                }
            }
        }

        describe("unfilled") {
            val validator = SudokuValidator()

            describe("valid") {
                it("should return the number 2 as candidate for the first cell") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val candidatesForCell = solver.getCandidatesForCell(0, 0)
                    assertEquals(1, candidatesForCell.size)
                    assertEquals(2, candidatesForCell.first())
                }

                it("should return the number 2 and 3 as candidate for the first cell") {
                    val sudoku = file_importer.import("src/test/resources/three_missing_numbers.sdk")

                    val solver = SudokuSolver(sudoku)
                    val candidatesForCell = solver.getCandidatesForCell(0, 0)
                    assertEquals(2, candidatesForCell.size)
                    assertTrue(candidatesForCell.toList().containsAll(listOf(2, 3)))
                }

                it("should be able to solve the puzzle if one number missing") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve the puzzle if two numbers missing in one row") {
                    val sudoku = file_importer.import("src/test/resources/two_missing_numbers_in_row.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve the puzzle if two numbers missing in one row 2") {
                    val sudoku = file_importer.import("src/test/resources/two_missing_numbers_in_row2.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve the puzzle if two numbers missing in one column") {
                    val sudoku = file_importer.import("src/test/resources/two_missing_numbers_in_column.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve the puzzle if two numbers missing in one column 2") {
                    val sudoku = file_importer.import("src/test/resources/two_missing_numbers_in_column2.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve the puzzle if three numbers missing") {
                    val sudoku = file_importer.import("src/test/resources/three_missing_numbers.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertEquals("2", solvedSudoku.puzzle.getCell(0, 0))
                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should be able to solve an easy puzzle") {
                    val sudoku = file_importer.import("src/test/resources/easy_sudoku.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku: Sudoku = solver.solve()

                    assertTrue(solvedSudoku.puzzle.isSolved())
                    assertTrue(validator.validate(sudoku))
                }

                it("should return the number 2 for the first row") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersForRow = solver.getMissingNumbersForRow(0)
                    assertEquals(1, missingNumbersForRow.size)
                    assertEquals(2, missingNumbersForRow[0])
                }

                it("should return the number 2 for the first column") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersForColumn = solver.getMissingNumbersForColumn(0)
                    assertEquals(1, missingNumbersForColumn.size)
                    assertEquals(2, missingNumbersForColumn[0])
                }

                it("should return the number 2 for the first group") {
                    val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersForGroup = solver.getMissingNumbersForGroup(0)
                    assertEquals(1, missingNumbersForGroup.size)
                    assertEquals(2, missingNumbersForGroup[0])
                }
            }
        }
    }
})
