package sudoklin.impex

/**
 * Created by sebastian on 13.06.17.
 */
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertTrue

class SudokuFileImprterTest : Spek({
    describe("An importer") {
        val file_importer = SudokuFileImporter()

        it("should return a text containing some string") {
            val fileContent = file_importer.import("src/test/resources/example.sdk")
            assertTrue(fileContent.contains("SudoCue"))
        }

    }
})
