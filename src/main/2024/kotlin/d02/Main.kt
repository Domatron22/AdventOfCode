package d02

import java.io.File

/**
 * Parse lists from a given file
 */
fun parseListsFromFile(filename: String): List<List<Int>> =
    try {
        File(filename).useLines { lines ->
            lines.filter { it.isNotEmpty() }
                .map { line ->
                    line.trim()
                        .split(Regex("\\s+"))
                        .mapNotNull { it.toIntOrNull() }
                }
                .toList()
        }
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        emptyList()
    }

/**
 * Check a report to see if it follows these safety checks:
 *  - The levels are either all increasing or all decreasing.
 *  - Any two adjacent levels differ by at least one and at most three.
 *
 * curve:
 *  1 - increase
 *  0 - decrease
 */
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

/**
 * Safety checks with updated tolerance levels, only one number in the sequence can be a bad level and the report will still be safe.
 */
fun safetyAlgorithmWithDampeners(reports: List<Int>): Boolean =
    reports.size < 2 || isValidSequence(reports) ||
    reports.indices.any { skipIndex ->
        isValidSequence(reports.filterIndexed { index, _ -> index != skipIndex })
}

/**
 * Specify file and run safety checks
 */
fun checkSafety() = File("src/main/2024/resources/02/input.txt").let { file ->
    parseListsFromFile(file.path).let { lists ->
        println("${lists.count(::isValidSequence)} reports are safe")
        println("${lists.count(::safetyAlgorithmWithDampeners)} reports are safe with active dampeners")
    }
}

fun main() = checkSafety()