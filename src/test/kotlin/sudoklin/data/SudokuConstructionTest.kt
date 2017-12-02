package sudoklin.data

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

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
    }
})