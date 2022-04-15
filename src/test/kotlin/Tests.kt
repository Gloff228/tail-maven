import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class Tests{

    @Test
    fun extract(){
        extract(listOf("src/test/kotlin/input/123.txt"), "src/test/kotlin/output/output.txt", null, 2)
        assertEquals(File("src/test/kotlin/output/expectedOutput1.txt").readLines(), File("src/test/kotlin/output/output.txt").readLines())
        File("src/test/kotlin/output/output.txt").delete()

        extract(listOf("src/test/kotlin/input/numLines.txt", "src/test/kotlin/input/poem.txt"), "src/test/kotlin/output/output.txt", 5, null)
        assertEquals(File("src/test/kotlin/output/expectedOutput2.txt").readLines(), File("src/test/kotlin/output/output.txt").readLines())
        File("src/test/kotlin/output/output.txt").delete()

        extract(listOf("src/test/kotlin/input/strLines.txt", "src/test/kotlin/input/123.txt", "src/test/kotlin/input/numLines.txt"), "src/test/kotlin/output/output.txt", null, 24)
        assertEquals(File("src/test/kotlin/output/expectedOutput3.txt").readLines(), File("src/test/kotlin/output/output.txt").readLines())
        File("src/test/kotlin/output/output.txt").delete()

        extract(listOf("src/test/kotlin/input/numString.txt", "src/test/kotlin/input/123.txt", "src/test/kotlin/input/poem.txt"), "src/test/kotlin/output/output.txt", 10, null)
        assertEquals(File("src/test/kotlin/output/expectedOutput4.txt").readLines(), File("src/test/kotlin/output/output.txt").readLines())
        File("src/test/kotlin/output/output.txt").delete()

        extract(listOf("src/test/kotlin/input/numString.txt", "src/test/kotlin/input/123.txt", "src/test/kotlin/input/poem.txt"), "src/test/kotlin/output/output.txt", null, 0)
        assertEquals(File("src/test/kotlin/output/expectedOutput5.txt").readLines(), File("src/test/kotlin/output/output.txt").readLines())
        File("src/test/kotlin/output/output.txt").delete()

        assertThrows(FileNotFoundException::class.java){ extract(listOf("src/test/kotlin/input/fileNotFound.txt"), "src/test/kotlin/output/outputNotFound.txt", null, 12345) }
        assertThrows(IllegalArgumentException::class.java){ extract(listOf("src/test/kotlin/input/word.docx"), "src/test/kotlin/output/outputNotFound.txt", null, 12345) }
    }
}

