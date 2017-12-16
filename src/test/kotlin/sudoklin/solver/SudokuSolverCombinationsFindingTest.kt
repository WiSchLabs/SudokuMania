package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuSolverCombinationsFindingTest : Spek({
    describe("A sudoku") {
        val fileImporter = SudokuFileImporter()

        describe("should eliminate all occurrences of found ") {
            it("pairs in row") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_row.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()

                assertTrue((2..8).all { columnIndex ->
                    !sudoku.getCell(0, columnIndex).candidates.contains(1) &&
                    !sudoku.getCell(0, columnIndex).candidates.contains(2)
                })
            }

            it("pairs in column") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_column.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()

                assertTrue((2..8).all { rowIndex ->
                    !sudoku.getCell(rowIndex, 0).candidates.contains(1) &&
                    !sudoku.getCell(rowIndex, 0).candidates.contains(2)
                })
            }

            it("triples in row") {
                val sudoku = fileImporter.import("src/test/resources/triple_candidate_elimination_for_row.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()

                assertTrue((3..8).all { rowIndex ->
                    !sudoku.getCell(0, rowIndex).candidates.contains(1) &&
                    !sudoku.getCell(0, rowIndex).candidates.contains(2) &&
                    !sudoku.getCell(0, rowIndex).candidates.contains(3)
                })
            }

            it("triples in column") {
                val sudoku = fileImporter.import("src/test/resources/triple_candidate_elimination_for_column.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()

                assertTrue((3..8).all { rowIndex ->
                    !sudoku.getCell(rowIndex, 0).candidates.contains(1) &&
                    !sudoku.getCell(rowIndex, 0).candidates.contains(2) &&
                    !sudoku.getCell(rowIndex, 0).candidates.contains(3)
                })
            }

            it("pairs in group") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_row.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()

                assertFalse(sudoku.getCell(0, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(0, 2).candidates.contains(2))

                assertFalse(sudoku.getCell(1, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 2).candidates.contains(2))

                assertFalse(sudoku.getCell(2, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 2).candidates.contains(2))
            }

            it("triples in group") {
                val sudoku = fileImporter.import("src/test/resources/triple_candidate_elimination_for_row.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCombinationsOfCandidates()
                assertFalse(sudoku.getCell(2, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 1).candidates.contains(2))
                assertFalse(sudoku.getCell(2, 1).candidates.contains(3))
                assertTrue(sudoku.getCell(2, 1).isSolvedWithNumber(9))
            }

        }

    }
})
