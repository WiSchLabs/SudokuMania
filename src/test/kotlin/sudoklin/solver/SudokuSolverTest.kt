package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SudokuSolverTest : Spek({
    describe("A sudoku") {
        val fileImporter = SudokuFileImporter()

        describe("filled") {
            describe("valid") {
                it("should return no missing numbers for the row") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInRow = solver.getMissingNumbersForRow(1)
                    assertEquals(0, missingNumbersInRow.size)
                }

                it("should return no missing numbers for the column") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInColumn = solver.getMissingNumbersForColumn(1)
                    assertEquals(0, missingNumbersInColumn.size)
                }

                it("should return no missing numbers for the group") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val solver = SudokuSolver(sudoku)
                    val missingNumbersInGroup = solver.getMissingNumbersForGroup(1)
                    assertEquals(0, missingNumbersInGroup.size)
                }
            }
        }

        describe("unfilled") {

            describe("valid") {

                it("should be able to solve the puzzle if one number missing") {
                    val sudoku = fileImporter.import("src/test/resources/one_missing_number.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve the puzzle if two numbers missing in one row") {
                    val sudoku = fileImporter.import("src/test/resources/two_missing_numbers_in_row.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve the puzzle if two numbers missing in one row 2") {
                    val sudoku = fileImporter.import("src/test/resources/two_missing_numbers_in_row2.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve the puzzle if two numbers missing in one column") {
                    val sudoku = fileImporter.import("src/test/resources/two_missing_numbers_in_column.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve the puzzle if two numbers missing in one column 2") {
                    val sudoku = fileImporter.import("src/test/resources/two_missing_numbers_in_column2.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve the puzzle if three numbers missing") {
                    val sudoku = fileImporter.import("src/test/resources/three_missing_numbers.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertEquals(2, solvedSudoku.getCell(0, 0).candidates.first())
                    assertEquals(1, solvedSudoku.getCell(0, 0).candidates.size)
                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve an easy puzzle") {
                    val sudoku = fileImporter.import("src/test/resources/easy_sudoku.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve an medium puzzle") {
                    val sudoku = fileImporter.import("src/test/resources/medium_sudoku.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve an hard puzzle") {
                    val sudoku = fileImporter.import("src/test/resources/hard_sudoku.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }

                it("should be able to solve an extreme hard puzzle") {
                    val sudoku = fileImporter.import("src/test/resources/extreme_hard_sudoku.sdk")

                    val solver = SudokuSolver(sudoku)
                    val solvedSudoku = solver.solve()

                    assertTrue(solvedSudoku.isSolved())
                    assertTrue(solvedSudoku.isValid())
                }
            }
        }
    }
})
