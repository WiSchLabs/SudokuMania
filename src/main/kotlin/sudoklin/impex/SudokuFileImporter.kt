package sudoklin.impex

import java.io.File

/**
 * Created by sebastian on 13.06.17.
 */
class SudokuFileImporter {

    fun import(filename: String): String {
        val fileContent = File(filename).readText()
        return fileContent
    }

}