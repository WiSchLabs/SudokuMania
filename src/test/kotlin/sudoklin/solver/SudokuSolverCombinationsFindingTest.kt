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

        describe("should eliminate all other occurrences of found combination") {

            describe("pairs") {
                it("in row") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_tuple_in_row.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()

                    assertTrue((2..8).all { columnIndex ->
                        !sudoku.getCell(0, columnIndex).candidates.contains(1) &&
                        !sudoku.getCell(0, columnIndex).candidates.contains(2)
                    })
                }

                it("in column") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_tuple_in_column.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()

                    assertTrue((2..8).all { rowIndex ->
                        !sudoku.getCell(rowIndex, 0).candidates.contains(1) &&
                        !sudoku.getCell(rowIndex, 0).candidates.contains(2)
                    })
                }

                it("in group") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_tuple_in_row.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()

                    assertFalse(sudoku.getCell(0, 2).candidates.contains(1))
                    assertFalse(sudoku.getCell(0, 2).candidates.contains(2))

                    assertFalse(sudoku.getCell(1, 2).candidates.contains(1))
                    assertFalse(sudoku.getCell(1, 2).candidates.contains(2))

                    assertFalse(sudoku.getCell(2, 2).candidates.contains(1))
                    assertFalse(sudoku.getCell(2, 2).candidates.contains(2))
                }
            }

            describe("triples") {
                it("in row") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_triple_in_row.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()

                    assertTrue((3..8).all { rowIndex ->
                        !sudoku.getCell(0, rowIndex).candidates.contains(1) &&
                        !sudoku.getCell(0, rowIndex).candidates.contains(2) &&
                        !sudoku.getCell(0, rowIndex).candidates.contains(3)
                    })
                }

                it("in column") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_triple_in_column.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()

                    assertTrue((3..8).all { rowIndex ->
                        !sudoku.getCell(rowIndex, 0).candidates.contains(1) &&
                        !sudoku.getCell(rowIndex, 0).candidates.contains(2) &&
                        !sudoku.getCell(rowIndex, 0).candidates.contains(3)
                    })
                }

                it("in group") {
                    val sudoku = fileImporter.import("src/test/resources/candidate_elimination_for_triple_in_group.sdk")
                    val solver = SudokuSolver(sudoku)
                    solver.cleanCombinationsOfCandidates()
                    assertFalse(sudoku.getCell(2, 1).candidates.contains(1))
                    assertFalse(sudoku.getCell(2, 1).candidates.contains(2))
                    assertFalse(sudoku.getCell(2, 1).candidates.contains(3))
                    assertTrue(sudoku.getCell(2, 1).isSolvedWithNumber(9))
                }
            }

        }

        describe("should eliminate all other occurrences of found localed candidate constraints") {
            it("in row") {
                val sudoku = fileImporter.import("src/test/resources/candidate_elimination_if_in_row_and_group.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCandidateConstraintsInOtherGroups()
                for (columnIndex in 3..8) {
                    assertFalse(sudoku.getCell(8, columnIndex).candidates.contains(1))
                }
            }
            it("in column") {
                val sudoku = fileImporter.import("src/test/resources/candidate_elimination_if_in_column_and_group.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCandidateConstraintsInOtherGroups()
                for (rowIndex in 3..8) {
                    assertFalse(sudoku.getCell(rowIndex, 8).candidates.contains(1))
                }
            }
            it("in group by row") {
                val sudoku = fileImporter.import("src/test/resources/candidate_elimination_if_row_candidates_limitied_to_single_group.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCandidateConstraintsInsideGroup()
                assertFalse(sudoku.getCell(1, 0).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 0).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 2).candidates.contains(1))
            }
            it("in group by column") {
                val sudoku = fileImporter.import("src/test/resources/candidate_elimination_if_column_candidates_limited_to_single_group.sdk")
                val solver = SudokuSolver(sudoku)
                solver.cleanCandidateConstraintsInsideGroup()
                assertFalse(sudoku.getCell(0, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(0, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 1).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 2).candidates.contains(1))
            }
        }

    }
})
