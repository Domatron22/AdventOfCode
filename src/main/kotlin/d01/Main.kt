package d01

import java.io.File

fun parseListsFromFile(filename: String): Pair<List<Int>, List<Int>> {
    return try {
        val lines = File(filename)
            .readLines()
            .filter { it.trim().isNotEmpty() }

        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        lines.forEach { line ->
            val parts = line.trim().split(Regex("\\s+"))
            if (parts.size == 2) {
                parts[0].toIntOrNull()?.let { leftList.add(it) }
                parts[1].toIntOrNull()?.let { rightList.add(it) }
            }
        }

        Pair(leftList, rightList)
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        Pair(emptyList(), emptyList())
    }
}

fun calculateListDistance(leftList: List<Int>, rightList: List<Int>): Int {
    // Sort both lists to ensure we pair up the smallest numbers first
    val sortedLeft = leftList.sorted()
    val sortedRight = rightList.sorted()

    // Calculate the total distance by pairing up elements and finding their absolute difference
    return sortedLeft.zip(sortedRight).sumOf { (left, right) -> kotlin.math.abs(left - right) }
}

fun calculateSimilarityScore(leftList: List<Int>, rightList: List<Int>): Int{
    // Calculate the amount of times the number in the left list appears in the right list, multiply and sum it up
    return leftList.sumOf { item -> kotlin.math.abs(item * rightList.count { it == item }) }
}

fun main() {
    val (leftList, rightList) = parseListsFromFile("src/main/resources/01/input.txt")

    // println("Left List: $leftList")
    // println("Right List: $rightList")

    val totalDistance = calculateListDistance(leftList, rightList)
    println("Total distance: $totalDistance")

    val totalSimilarity = calculateSimilarityScore(leftList, rightList)
    println("Total similarity: $totalSimilarity")
}
