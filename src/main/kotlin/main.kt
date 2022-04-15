import kotlinx.cli.*
import java.io.File
import java.io.FileNotFoundException

fun main(args: Array<String>) {
    parse(args)
}

fun parse(args: Array<String>) {
    val parser = ArgParser("tail")
    val extractSymbols by parser.option(
        ArgType.Int,
        shortName = "c",
        description = "Number of symbols for output. Can't be used with -n"
    )
    val extractStrings by parser.option(
        ArgType.Int,
        shortName = "n",
        description = "Number of strings for output. Can't be used with -c"
    )
    val output by parser.option(ArgType.String, shortName = "o", fullName = "out", description = "Output file")
    val input by parser.argument(ArgType.String, description = "Input file").vararg()

    parser.parse(args)

    when {
        extractSymbols != null && extractStrings != null -> throw IllegalArgumentException("Can't use both -c and -n at the same time")
        extractSymbols == null && extractStrings == null -> extract(input, output, 10, null)
        extractSymbols != null && extractSymbols!! < 0 || extractStrings != null && extractStrings!! < 0 -> throw IllegalArgumentException(
            "Negative numbers cannot be used"
        )
        else -> extract(input, output, extractStrings, extractSymbols)
    }
}

fun extract(inputFiles: List<String>, outputFile: String?, countStrings: Int?, countSymbols: Int?) {
    val fileStrings = mutableMapOf<String, MutableList<String>>()
    val isFile = File(inputFiles[0]).exists()
    var cmdStringNumber = 0

    for (file in inputFiles) {
        if (File(file).exists()) {
            //исключения
            if (!isFile) throw IllegalArgumentException("String cannot be used with a file on the same line")
            if (file.substring(file.length - 4, file.length) != ".txt")
                throw IllegalArgumentException("Invalid file format")

            fileStrings[file] = mutableListOf()
            val stringsOfFile = File(file).readLines() //разделение текста файла на строки
            extractSymbolOrString(stringsOfFile, fileStrings[file]!!, countSymbols, countStrings)
        } else {
            //исключения
            if (isFile) throw IllegalArgumentException("String cannot be used with a file on the same line")
            if (file.length > 4 && file.substring(file.length - 4, file.length) == ".txt")
                throw FileNotFoundException("Called file does not exist") //исключение, если файла не существует

            cmdStringNumber++

            fileStrings["cmd string $cmdStringNumber"] = mutableListOf()
            val lineSep = System.lineSeparator().toString().replace("\r", "\\r").replace("\n", "\\n")
            val stringsOfCommandLine = file.split(lineSep)

            extractSymbolOrString(
                stringsOfCommandLine,
                fileStrings["cmd string $cmdStringNumber"]!!,
                countSymbols,
                countStrings
            )

        }
    }
    output(fileStrings, outputFile)
}

fun extractSymbolOrString(
    stringsOfFile: List<String>,
    futureTextOfFile: MutableList<String>,
    countSymbols: Int?,
    countStrings: Int?
) {
    if (countStrings != null) extractStrings(stringsOfFile, futureTextOfFile, countStrings)
    else extractSymbols(stringsOfFile, futureTextOfFile, countSymbols!!)
}

fun extractStrings(stringsOfFile: List<String>, textOfFile: MutableList<String>, count: Int) {
    var countForFile = count
    if (count >= stringsOfFile.size) countForFile =
        stringsOfFile.size //сокращение выводимых строк до количества строк в файле
    for (line in (stringsOfFile.size - countForFile) until stringsOfFile.size) textOfFile.add(stringsOfFile[line])
}

fun extractSymbols(stringsOfFile: List<String>, textOfFile: MutableList<String>, count: Int) {
    if (count != 0) {
        var string = ""
        for (line in stringsOfFile.reversed()) {
            string = line + string
            if (string.length >= count) {
                string = string.substring(string.length - count, string.length)
                break
            }
        }
        textOfFile.add(string)
    }
}

fun output(fileToStrings: MutableMap<String, MutableList<String>>, outputFile: String?) {
    if (outputFile == null) {
        for ((file, strings) in fileToStrings) {
            if (fileToStrings.keys.size > 1) println("---$file---")
            for (line in strings) {
                println(line)
            }
        }
    } else {
        File(outputFile).bufferedWriter().use {
            for ((file, strings) in fileToStrings) {
                if (fileToStrings.keys.size > 1) {
                    it.write("---$file---")
                    it.newLine()
                }
                for (line in strings) {
                    it.write(line)
                    it.newLine()
                }
            }
        }
    }
}