package sudoklin.validator

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import sudoklin.impex.SudokuFileImporter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuValidatorTest : Spek({
    describe("A sudoku") {
        val fileImporter = SudokuFileImporter()

        describe("filled") {
            describe("valid") {
                it("should return that the second row is valid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val validator = SudokuValidator()
                    assertTrue(validator.validateRow(sudoku, 1))
                }

                it("should return that the first column is valid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val validator = SudokuValidator()
                    assertTrue(validator.validateColumn(sudoku, 0))
                }

                it("should return that the upper left corner group is valid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val validator = SudokuValidator()
                    assertTrue(validator.validateGroup(sudoku, 0))
                }

                it("should return that the whole sudoku is valid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_valid.sdk")

                    val validator = SudokuValidator()
                    assertTrue(validator.validate(sudoku))
                }
            }

            describe("invalid") {
                it("should return that the first row is invalid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_invalid.sdk")

                    val validator = SudokuValidator()
                    assertFalse(validator.validateRow(sudoku, 0))
                }

                it("should return that the last column is invalid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_invalid.sdk")

                    val validator = SudokuValidator()
                    assertFalse(validator.validateColumn(sudoku, 8))
                }

                it("should return that the upper right corner group is invalid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_invalid.sdk")

                    val validator = SudokuValidator()
                    assertFalse(validator.validateGroup(sudoku, 2))
                }

                it("should return that the whole sudoku is invalid") {
                    val sudoku = fileImporter.import("src/test/resources/filled_invalid.sdk")

                    val validator = SudokuValidator()
                    assertFalse(validator.validate(sudoku))
                }
            }
        }
    }
})
