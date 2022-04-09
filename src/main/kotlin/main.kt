import kotlinx.cli.*
import java.io.File
import java.io.FileNotFoundException

fun main(args: Array<String>) {
    parsing(args)
}

fun parsing(args: Array<String>) {
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
        .default("console")
    val input by parser.argument(ArgType.String, description = "Input file").vararg()

    parser.parse(args)

    when {
        extractSymbols != null && extractStrings != null -> throw IllegalArgumentException("Can't use both -c and -n at the same time")
        extractSymbols == null && extractStrings == null -> extracting(input, output, 10, null)
        else -> extracting(input, output, extractStrings, extractSymbols)
    }
}

fun extracting(inputFile: List<String>, outputFile: String, countStrings: Int?, countSymbols: Int?) {
    val list = mutableListOf<String>() //собирает строки, которые будут выводиться в output файл

    // проверка, является ли 1 элемент текстовым файлом (если нет, то последующие входные данные принимаются за простой текст)
    if (inputFile[0].length < 4 || inputFile[0].substring(inputFile[0].length - 4, inputFile[0].length) != ".txt") {
        val lines = inputFile.joinToString(separator = " ").split("\\n") //разделение текста cmd на строки
        extractingSymbolOrString(lines, list, countSymbols, countStrings)
    } else
        for (file in inputFile) {
            if (!File(file).exists()) throw FileNotFoundException("Called file does not exist") //исключение, если файла не существует
            val lines = File(file).readLines() //разделение текста файла на строки
            if (inputFile.size > 1) list.add("---$file---") //добавление названия файла при выводе, если файлов несколько
            extractingSymbolOrString(lines, list, countSymbols, countStrings)
        }
    output(list, outputFile)
}

fun extractingSymbolOrString(lines: List<String>, list: MutableList<String>, countSymbols: Int?, countStrings: Int?){
    if (countStrings != null) extractingStrings(lines, list, countStrings)
    else extractingSymbols(lines, list, countSymbols!!)
}

fun extractingStrings(lines: List<String>, list: MutableList<String>, count: Int) {
    var countForFile = count
    if (count >= lines.size) countForFile = lines.size //сокращение выводимых строк до количества строк в файле
    for (line in (lines.size - countForFile) until lines.size) list.add(lines[line])
}

fun extractingSymbols(lines: List<String>, list: MutableList<String>, count: Int) {
    var string = ""
    for (line in lines.reversed()) {
        string = line + string
        if (string.length >= count) {
            string = string.substring(string.length - count, string.length)
            break
        }
    }
    list.add(string)
}

fun output(list: List<String>, outputFile: String) {
    if (outputFile == "console") {
        for (line in list) println(line)
    } else {
        val writer = File(outputFile).bufferedWriter()
        for (line in list.indices) {
            writer.write(list[line])
            if (line != list.size - 1) writer.newLine()
        }
        writer.close()
    }
}