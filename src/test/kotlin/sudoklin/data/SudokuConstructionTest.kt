package sudoklin.data

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class SudokuConstructionTest : Spek({
    describe("A Cell in row and column") {
        val cell = SudokuCell(0, 0, IntArray(1, {5}))
        val row = SudokuRow(0, Array(1, {cell}))
        val column = SudokuColumn(0, Array(1, {cell}))

        it("should be the same after being updated") {
            assertEquals(5, row.cells[0].candidates[0])
            assertEquals(5, column.cells[0].candidates[0])

            cell.candidates[0] = 4

            assertEquals(4, row.cells[0].candidates[0])
            assertEquals(4, column.cells[0].candidates[0])
        }
    }
})

class NewSudokuTest : Spek({
    describe("A new sudoku") {
        val sudoku = NewShinySudoku()

        it("should contain all the row candidates") {
            for (row in sudoku.rows) {
                for (i in 0..8) {
                    val cell = row!!.cells[i]
                    assertEquals(9, cell.candidates.size)
                    assertEquals(cell.columnIndex, i)
                }
            }
        }

        it("should contain all the column candidates") {
            for (column in sudoku.columns) {
                for (i in 0..8) {
                    val cell = column!!.cells[i]
                    assertEquals(9, cell.candidates.size)
                    assertEquals(cell.rowIndex, i)
                }
            }
        }

        it("should contain all the group candidates") {
            for (group in sudoku.groups) {
                for (i in 0..8) {
                    val cell = group!!.cells[i]
                    assertEquals(9, cell.candidates.size)
                }
            }
        }

        it("should have one less number in the row/column/group if it was added") {
            sudoku.addSolvedNumber(0,0, 9)
            for (i in 1..8) {
                val rowCell = sudoku.rows[0]!!.cells[i]
                val columnCell = sudoku.columns[0]!!.cells[i]
                val groupCell = sudoku.groups[0]!!.cells[i]
                for (candidate in 1..8) {
                    assertTrue(rowCell.candidates.contains(candidate))
                    assertTrue(columnCell.candidates.contains(candidate))
                    assertTrue(groupCell.candidates.contains(candidate))
                }
                assertFalse(rowCell.candidates.contains(9))
                assertFalse(columnCell.candidates.contains(9))
                assertFalse(groupCell.candidates.contains(9))
            }
        }
    }
})