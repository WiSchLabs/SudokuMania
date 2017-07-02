package sudoklin.validator

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.data.Sudoku
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertTrue

class SudokuValidatorTest : Spek({
    describe("Given a correctly filled row") {
        val file_importer = SudokuFileImporter()
        val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

        it("the validator should return that it is valid") {
            val validator = SudokuValidator()
            assertTrue(validator.validateRow(sudoku, 1))
        }
    }
})
