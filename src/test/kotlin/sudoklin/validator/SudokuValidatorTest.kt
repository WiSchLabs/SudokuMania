package sudoklin.validator

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuValidatorTest : Spek({
    given("a filled sudoku") {
        val file_importer = SudokuFileImporter()

        on("a valid sudoku") {
            it("the validator should return that the second row is valid") {
                val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                val validator = SudokuValidator()
                assertTrue(validator.validateRow(sudoku, 1))
            }

            it("the validator should return that the first column is valid") {
                val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                val validator = SudokuValidator()
                assertTrue(validator.validateColumn(sudoku, 0))
            }

            it("the validator should return that the upper left corner group is valid") {
                val sudoku = file_importer.import("src/test/resources/filled_valid.sdk")

                val validator = SudokuValidator()
                assertTrue(validator.validateGroup(sudoku, 0))
            }
        }

        on("an invalid sudoku") {
            it("the validator should return that the first row invalid") {
                val sudoku = file_importer.import("src/test/resources/filled_invalid.sdk")

                val validator = SudokuValidator()
                assertFalse(validator.validateRow(sudoku, 0))
            }

            it("the validator should return that the last column invalid") {
                val sudoku = file_importer.import("src/test/resources/filled_invalid.sdk")

                val validator = SudokuValidator()
                assertFalse(validator.validateColumn(sudoku, 8))
            }

            it("the validator should return that the upper right corner group is invalid") {
                val sudoku = file_importer.import("src/test/resources/filled_invalid.sdk")

                val validator = SudokuValidator()
                assertFalse(validator.validateGroup(sudoku, 2))
            }
        }
    }

    given("an unfilled sudoku") {
        val file_importer = SudokuFileImporter()

        on("one missing number validation") {
            it("should return that missing number is two") {
                val sudoku = file_importer.import("src/test/resources/one_missing_number.sdk")
                assertEquals(2, sudoku.puzzle.valid_numbers(0,0))
            }
        }
    }
})
