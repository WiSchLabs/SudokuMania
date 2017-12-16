package sudoklin.solver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuSolverCombinationsFindingTest : Spek({
    describe("A sudoku") {
        val fileImporter = SudokuFileImporter()

        it("should eliminate all occurrences of found pairs") {
            it("in row") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_row.sdk")
                assertTrue((2..8).all { columnIndex ->
                    !sudoku.getCell(0, columnIndex).candidates.contains(1) &&
                    !sudoku.getCell(0, columnIndex).candidates.contains(2)
                })
            }

            it("in column") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_column.sdk")
                assertTrue((2..8).all { rowIndex ->
                    !sudoku.getCell(rowIndex, 0).candidates.contains(1) &&
                    !sudoku.getCell(rowIndex, 0).candidates.contains(2)
                })
            }

            it("in group") {
                val sudoku = fileImporter.import("src/test/resources/tuple_candidate_elimination_for_row.sdk")
                assertFalse(sudoku.getCell(0, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(0, 2).candidates.contains(2))

                assertFalse(sudoku.getCell(1, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(1, 2).candidates.contains(2))

                assertFalse(sudoku.getCell(2, 2).candidates.contains(1))
                assertFalse(sudoku.getCell(2, 2).candidates.contains(2))
            }

        }

    }
})
