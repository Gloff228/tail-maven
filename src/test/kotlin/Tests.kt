import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class Tests {
    @Test
    fun extractSymbols() {
        extract(listOf("src/test/kotlin/input/123.txt"), "src/test/kotlin/output/output.txt", null, 2)
        assertEquals(
            File("src/test/kotlin/output/expectedOutput1.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()

        extract(
            listOf(
                "src/test/kotlin/input/strLines.txt",
                "src/test/kotlin/input/123.txt",
                "src/test/kotlin/input/numLines.txt"
            ), "src/test/kotlin/output/output.txt", null, 24
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput3.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()
    }

    @Test
    fun extractStrings() {
        extract(
            listOf("src/test/kotlin/input/numLines.txt", "src/test/kotlin/input/poem.txt"),
            "src/test/kotlin/output/output.txt",
            5,
            null
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput2.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()

        extract(
            listOf(
                "src/test/kotlin/input/numString.txt",
                "src/test/kotlin/input/123.txt",
                "src/test/kotlin/input/poem.txt"
            ), "src/test/kotlin/output/output.txt", 10, null
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput4.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()
    }

    @Test
    fun zeroCountOfSymbols() {
        extract(
            listOf(
                "src/test/kotlin/input/numString.txt",
                "src/test/kotlin/input/123.txt",
                "src/test/kotlin/input/poem.txt"
            ), "src/test/kotlin/output/output.txt", null, 0
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput5.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()
    }

    @Test
    fun zeroCountOfString() {
        extract(
            listOf(
                "src/test/kotlin/input/numString.txt",
                "src/test/kotlin/input/123.txt",
                "src/test/kotlin/input/poem.txt"
            ), "src/test/kotlin/output/output.txt", 0, null
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput5.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()
    }

    @Test
    fun fileNotFound() {
        assertThrows(FileNotFoundException::class.java) {
            extract(
                listOf("src/test/kotlin/input/fileNotFound.txt"),
                "src/test/kotlin/output/outputNotFound.txt",
                null,
                12345
            )
        }
    }

    @Test
    fun invalidFileFormat() {
        assertThrows(IllegalArgumentException::class.java) {
            extract(
                listOf("src/test/kotlin/input/word.docx"),
                "src/test/kotlin/output/outputNotFound.txt",
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
            ), "src/test/kotlin/output/output.txt", null, 7
        )
        assertEquals(
            File("src/test/kotlin/output/expectedOutput6.txt").readLines(),
            File("src/test/kotlin/output/output.txt").readLines()
        )
        File("src/test/kotlin/output/output.txt").delete()
    }
}

