import kotlinx.cli.*
import java.io.File
import java.io.FileNotFoundException

const val defaultCount = 10
const val exceptionFileAndText = "String cannot be used with a name on the same line"

fun main(args: Array<String>) {
    val (inputName, outputFile, countStringsAndSymbols) = parse(args)
    val (countStrings, countSymbols) = countStringsAndSymbols
    output(extract(inputName, countStrings, countSymbols), outputFile)
}

fun parse(args: Array<String>): Triple<List<String>, String?, Pair<Int?, Int?>> {
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

    return Triple(input, output, Pair(extractStrings, extractSymbols))
}

fun extract(inputFiles: List<String>, countStrings: Int?, countSymbols: Int?): MutableMap<String, MutableList<String>> {
    //исключения
    when {
        countSymbols != null && countStrings != null -> throw IllegalArgumentException("Can't use both -c and -n at the same time")
        countSymbols != null && countSymbols < 0 || countStrings != null && countStrings < 0 -> throw IllegalArgumentException(
            "Negative numbers cannot be used"
        )
    }

    val fileStrings = mutableMapOf<String, MutableList<String>>()
    val isFile = File(inputFiles[0]).exists()
    var cmdStringNumber = 0

    for (name in inputFiles) {
        if (File(name).exists()) {
            //исключения
            if (!isFile) throw IllegalArgumentException(exceptionFileAndText)
            if (name.length <= 4 || name.substring(name.length - 4, name.length) != ".txt")
                throw IllegalArgumentException("Invalid name format")

            fileStrings[name] = mutableListOf()
            val stringsOfFile = File(name).readLines() //разделение текста файла на строки
            extractSymbolOrString(stringsOfFile, fileStrings[name]!!, countSymbols, countStrings)
        } else {
            //исключения
            if (isFile) throw IllegalArgumentException(exceptionFileAndText)
            if (name.length > 4 && name.substring(name.length - 4, name.length) == ".txt")
                throw FileNotFoundException("Called name does not exist") //исключение, если файла не существует

            cmdStringNumber++

            fileStrings["cmd string $cmdStringNumber"] = mutableListOf()
            val lineSep = System.lineSeparator().toString().replace("\r", "\\r").replace("\n", "\\n")
            val stringsOfCommandLine = name.split(lineSep)

            extractSymbolOrString(
                stringsOfCommandLine,
                fileStrings["cmd string $cmdStringNumber"]!!,
                countSymbols,
                countStrings
            )

        }
    }
    return fileStrings
}

fun extractSymbolOrString(
    stringsOfFile: List<String>,
    futureTextOfFile: MutableList<String>,
    countSymbols: Int?,
    countStrings: Int?
) {
    if (countSymbols != null) countSymbols(stringsOfFile, futureTextOfFile, countSymbols)
    else {
        if (countStrings == null) countStrings(stringsOfFile, futureTextOfFile, defaultCount)
        else countStrings(stringsOfFile, futureTextOfFile, countStrings)
    }
}

fun countStrings(stringsOfFile: List<String>, futureTextOfFile: MutableList<String>, count: Int) {
    var countForFile = count
    if (count >= stringsOfFile.size) countForFile =
        stringsOfFile.size //сокращение выводимых строк до количества строк в файле
    for (line in (stringsOfFile.size - countForFile) until stringsOfFile.size) futureTextOfFile.add(stringsOfFile[line])
}

fun countSymbols(stringsOfFile: List<String>, futureTextOfFile: MutableList<String>, count: Int) {
    if (count != 0) {
        var string = ""
        for (line in stringsOfFile.reversed()) {
            string = line + string
            if (string.length >= count) {
                string = string.substring(string.length - count, string.length)
                break
            }
        }
        futureTextOfFile.add(string)
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