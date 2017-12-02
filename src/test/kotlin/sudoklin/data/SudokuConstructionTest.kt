package sudoklin.data

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

class SudokuFileImporterTest : Spek({
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