package sudoklin.impex

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.data.Sudoku
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SudokuFileImporterTest : Spek({
    describe("An importer") {
        val fileImporter = SudokuFileImporter()

        it("should return a Sudoku object") {
            val sudoku = fileImporter.import("src/test/resources/example.sdk")
            assertTrue(sudoku is Sudoku)
        }

        it("should return a Sudoku object with first cell filled correctly") {
            val sudoku = fileImporter.import("src/test/resources/example.sdk")
            assertTrue(sudoku.getCell(0, 0).isSolvedWithNumber(2))
        }

        it("should return a Sudoku object with correct candidates for an unsolved cell") {
            val sudoku = fileImporter.import("src/test/resources/example.sdk")
            assertTrue(sudoku.getCell(0, 1).candidates.containsAll(listOf(6, 7, 9)))
            assertEquals(3, sudoku.getCell(0, 1).candidates.size)
        }
    }
})
