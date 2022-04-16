import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class Tests {
    @Test
    fun extractSymbols() {
        extract(listOf("src/test/resources/input/123.txt"), "src/test/resources/output/output.txt", null, 2)
        assertEquals(
            File("src/test/resources/output/expectedOutput1.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()

        extract(
            listOf(
                "src/test/resources/input/strLines.txt",
                "src/test/resources/input/123.txt",
                "src/test/resources/input/numLines.txt"
            ), "src/test/resources/output/output.txt", null, 24
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput3.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun extractStrings() {
        extract(
            listOf("src/test/resources/input/numLines.txt", "src/test/resources/input/poem.txt"),
            "src/test/resources/output/output.txt",
            5,
            null
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput2.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()

        extract(
            listOf(
                "src/test/resources/input/numString.txt",
                "src/test/resources/input/123.txt",
                "src/test/resources/input/poem.txt"
            ), "src/test/resources/output/output.txt", 10, null
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput4.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun zeroCountOfSymbols() {
        extract(
            listOf(
                "src/test/resources/input/numString.txt",
                "src/test/resources/input/123.txt",
                "src/test/resources/input/poem.txt"
            ), "src/test/resources/output/output.txt", null, 0
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput5.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun zeroCountOfString() {
        extract(
            listOf(
                "src/test/resources/input/numString.txt",
                "src/test/resources/input/123.txt",
                "src/test/resources/input/poem.txt"
            ), "src/test/resources/output/output.txt", 0, null
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput5.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun fileNotFound() {
        assertThrows(FileNotFoundException::class.java) {
            extract(
                listOf("src/test/resources/input/fileNotFound.txt"),
                "src/test/resources/output/outputNotFound.txt",
                null,
                12345
            )
        }
    }

    @Test
    fun invalidFileFormat() {
        assertThrows(IllegalArgumentException::class.java) {
            extract(
                listOf("src/test/resources/input/word.docx"),
                "src/test/resources/output/outputNotFound.txt",
                null,
                12345
            )
        }
    }

    @Test
    fun negativeCount() {
        assertThrows(IllegalArgumentException::class.java) { main(arrayOf("-c", "-3", "qwerty")) }
        assertThrows(IllegalArgumentException::class.java) { main(arrayOf("-n", "-12345", "Negative Count")) }
    }

    @Test
    fun twoOptions() {
        assertThrows(IllegalArgumentException::class.java) { main(arrayOf("-c", "3", "-n", "4", "qwerty")) }
    }

    @Test
    fun outputToCommandLine() {
        main(arrayOf("-c", "15", "12345"))
    }

    @Test
    fun textFromCommandLine() {
        extract(
            listOf(
                "123456789",
                "qwerty",
                "qazqazqazqaz1"
            ), "src/test/resources/output/output.txt", null, 7
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput6.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }
}

