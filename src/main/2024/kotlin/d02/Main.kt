package d02

import java.io.File

fun parseListsFromFile(filename: String): List<List<Int>> {
    return try {
        val lines = File(filename)
            .readLines()
            .filter { it.trim().isNotEmpty() }

        val lineList = mutableListOf<List<Int>>()

        lines.forEach { line ->
            val lineItems = mutableListOf<Int>()
            val parts = line.trim().split(Regex("\\s+"))
            parts.forEach {
                it.toIntOrNull()?.let { lineItems.add(it) }
            }
            lineList.add(lineItems)
        }

        lineList
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        emptyList<List<Int>>()
    }
}

/**
 * curve:
 *  1 - increase
 *  0 - decrease
 */

fun safetyAlgorithmWithDampeners(reports: List<Int>): Boolean {
    if (reports.size < 2) return true

    // Try removing each number and check if remaining sequence is valid
    return isValidSequence(reports) || reports.indices.any { skipIndex ->
        isValidSequence(reports.filterIndexed { index, _ -> index != skipIndex })
    }
}

private fun isValidSequence(reports: List<Int>): Boolean {
    if (reports.size < 2) return true

    val firstDiff = reports[0] - reports[1]
    val direction = when {
        firstDiff > 0 -> 1
        firstDiff < 0 -> 0
        else -> return false
    }

    return reports.zipWithNext()
        .all { (current, next) ->
            val diff = current - next
            kotlin.math.abs(diff) in 1..3 &&
            when {
                diff > 0 -> 1
                diff < 0 -> 0
                else -> direction
            } == direction
        }
}

fun checkSafety() {
    val safetyCount = parseListsFromFile("src/main/2024/resources/02/input.txt")
        .count(::isValidSequence)
    print("%s reports are safe\n".format(safetyCount))

    val safetyCountDampened = parseListsFromFile("src/main/2024/resources/02/input.txt")
        .count(::safetyAlgorithmWithDampeners)
    print("%s reports are safe with active dampeners\n".format(safetyCountDampened))
}

fun main(){
    checkSafety()
}