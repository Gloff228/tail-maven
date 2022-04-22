import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths

class ResourceReader {
    fun pathResource(fileName: String): String {
        val uri = this.javaClass.getResource("/$fileName").toURI()
        return Paths.get(uri).toString()
    }
}

fun getPath(fileName: String): String {
    val reader = ResourceReader()
    return reader.pathResource(fileName)
}

val file123 = getPath("input/123.txt")
val filePoem = getPath("input/poem.txt")
val fileWord = getPath("input/word.docx")
val fileNumString = getPath("input/numString.txt")
val expectedOutput1 = getPath("output/expectedOutput1.txt")
val expectedOutput2 = getPath("output/expectedOutput2.txt")

class Tests {
    @Test
    fun extract() {
        assertEquals(
            mapOf(file123 to listOf("23")),
            extract(listOf(file123), null, 2)
        )
    }

    @Test
    fun outputSymbols() {
        output(
            extract(listOf(file123), null, 2),
            "src/test/resources/output/output.txt"
        )
        assertEquals(
            File(expectedOutput1).readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun outputStrings() {
        output(
            extract(
                listOf(filePoem),
                5,
                null
            ),
            "src/test/resources/output/output.txt"
        )
        assertEquals(
            File(expectedOutput2).readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }

    @Test
    fun zeroCountOfSymbols() {
        assertEquals(
            mapOf(filePoem to listOf(), file123 to listOf(), fileNumString to listOf<String>()),
            extract(
                listOf(
                    fileNumString,
                    file123,
                    filePoem
                ), null, 0
            )
        )
    }

    @Test
    fun zeroCountOfString() {
        assertEquals(
            mapOf(filePoem to listOf(), file123 to listOf(), fileNumString to listOf<String>()),
            extract(
                listOf(
                    fileNumString,
                    file123,
                    filePoem
                ), 0, null
            )
        )
    }

    @Test
    fun fileNotFound() {
        assertThrows(FileNotFoundException::class.java) {
            extract(
                listOf("src/test/resources/input/fileNotFound.txt"),
                null,
                12345
            )
        }
    }

    @Test
    fun invalidFileFormat() {
        assertThrows(IllegalArgumentException::class.java) {
            extract(
                listOf(fileWord),
                null,
                12345
            )
        }
    }

    @Test
    fun negativeCount() {
        assertThrows(IllegalArgumentException::class.java) { main(arrayOf("-n", "-12345", "Negative Count")) }
    }

    @Test
    fun twoOptions() {
        assertThrows(IllegalArgumentException::class.java) { main(arrayOf("-c", "3", "-n", "4", "qwerty")) }
    }

    @Test
    fun textFromCommandLine() {
        output(
            extract(
                listOf(
                    "123456789",
                    "qwerty",
                    "qazqazqazqaz1"
                ), null, 7
            ), "src/test/resources/output/output.txt"
        )
        assertEquals(
            File("src/test/resources/output/expectedOutput6.txt").readLines(),
            File("src/test/resources/output/output.txt").readLines()
        )
        File("src/test/resources/output/output.txt").delete()
    }
}

