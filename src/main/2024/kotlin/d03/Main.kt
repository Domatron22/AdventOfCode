package d03

import java.io.File

/**
 * Applies the given regex to the input to extract the valid commands and the numbers associated with that command
 */
fun correctCorruptInstructions(regexInput: String): Unit{
    var result: Int = 0
    val mulInstructions = findAllCorruptInstructions(regexInput)

    mulInstructions.forEach{
        result += it.first * it.second
    }

    print("The product is %s\n".format(result))
}

fun correctCorruptInstructionsSupplemental(regexInput: String): Unit{
    var supplementedResult: Int = 0
    val supplementedInstructions = checkParsedInstructions(regexInput)


    supplementedInstructions.forEach{
        supplementedResult += it.first * it.second
    }
    print("After parsing supplemental data, the product is actually %s\n".format(supplementedResult))
}

fun checkParsedInstructions(regexInput: String): List<Pair<Int, Int>>{
    var lastInstruction: String? = null
    val results = mutableListOf<Pair<Int, Int>>()
    val regex = regexInput.toRegex()

    val filename = "src/main/2024/resources/03/input.txt"

    return try{
        val fileContents = File(filename).readText()
        var lastDo: Int
        var lastDont: Int

        val matches = regex.findAll(fileContents).toList()
        matches.forEach { matchResult ->
            val (prefix, x, y) = matchResult.destructured

            lastDo = if ( prefix.lastIndexOf("do()") != -1 ) prefix.lastIndexOf("do()") else -1
            lastDont = if ( prefix.lastIndexOf("don't()") != -1 ) prefix.lastIndexOf("don't()") else -1

            if(lastDont == -1 || lastDont < lastDo){
                results.add(Pair(x.toInt(),y.toInt()))
            }
        }
        results
    }catch (e: Exception) {
        println("Error reading file: ${e.message}")
        emptyList()
    }
}

fun findAllCorruptInstructions(regexInput: String): List<Pair<Int, Int>>{
    val regex = regexInput.toRegex()
    val filename = "src/main/2024/resources/03/input.txt"

    return try {
        File(filename).useLines { lines ->
            lines.flatMap { line ->
                regex.findAll(line).map { matchResult ->
                    val (x, y) = matchResult.destructured
                    x.toInt() to y.toInt()
                }
            }.toList()
        }
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        emptyList()
    }
}

fun main(): Unit{
    correctCorruptInstructions(""".*?mul\((\d+),(\d+)\)""")
    correctCorruptInstructionsSupplemental("""(.*?)mul\((\d+),(\d+)\)""")
}