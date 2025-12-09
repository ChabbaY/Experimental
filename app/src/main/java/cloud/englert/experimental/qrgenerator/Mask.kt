package cloud.englert.experimental.qrgenerator

import android.util.Log
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

class Mask(val matrix: Array<IntArray>, val pattern: Int) {
    companion object {
        private val LOG_TAG = Mask::class.java.simpleName

        fun apply(matrix: Array<IntArray>, dataModules: Array<IntArray>): Mask {
            var bestResult = deepCopy(matrix)
            var chosenPattern = 0
            var bestScore = Int.MAX_VALUE

            for (pattern in 0 until 8) {
                val maskedMatrix = deepCopy(matrix)
                Log.d(LOG_TAG, "applying mask $pattern on matrix")
                applyMaskPattern(maskedMatrix, dataModules, pattern)
                val score = evaluateMatrix(maskedMatrix)
                Log.d(LOG_TAG, "mask $pattern scored a penalty of $score points")

                if (score < bestScore) {
                    bestScore = score
                    bestResult = maskedMatrix
                    chosenPattern = pattern
                    Log.d(LOG_TAG, "new best result set")
                }
            }

            return Mask(bestResult, chosenPattern)
        }

        private fun applyMaskPattern(matrix: Array<IntArray>, dataModules: Array<IntArray>, mask: Int) {
            for (row in 0 until matrix.size) {
                for (column in 0 until matrix.size) {
                    if (dataModules[row][column] == 1) {
                        when(mask) {
                            0 -> if ((row + column) % 2 == 0) mask(matrix, row, column)
                            1 -> if (row % 2 == 0) mask(matrix, row, column)
                            2 -> if (column % 3 == 0) mask(matrix, row, column)
                            3 -> if ((row + column) % 3 == 0) mask(matrix, row, column)
                            4 -> if (((row / 2) + (column / 2)) % 2 == 0) mask(matrix, row, column)
                            5 -> if (((row * column) % 2) + ((row * column) % 3) == 0) mask(matrix, row, column)
                            6 -> if ((((row * column) % 2) + ((row * column) % 3)) % 2 == 0) mask(matrix, row, column)
                            7 -> if ((((row + column) % 2) + ((row * column) % 3)) % 2 == 0) mask(matrix, row, column)
                        }
                    }
                }
            }
        }

        private fun mask(matrix: Array<IntArray>, row: Int, column: Int) {
            matrix[row][column] = if (matrix[row][column] == 1) 0 else 1
        }

        private fun deepCopy(matrix: Array<IntArray>): Array<IntArray> {
            val result = matrix.copyOf()
            for ((index, array) in matrix.withIndex()) {
                result[index] = array.copyOf()
            }
            return result
        }

        private fun evaluateMatrix(matrix: Array<IntArray>): Int {
            var result = 0

            result += evaluateCondition1(matrix)
            result += evaluateCondition2(matrix)
            result += evaluateCondition3(matrix)
            result += evaluateCondition4(matrix)

            return result
        }

        /**
         * penalty 3 for 5 in a row with same color, then +1 for each additional module
         */
        private fun evaluateCondition1(matrix: Array<IntArray>): Int {
            var score = 0
            for (row in 0 until matrix.size) {
                var lastValue = 2
                var streak = 1
                for (column in 0 until matrix.size) {
                    if (matrix[row][column] != lastValue) {
                        lastValue = matrix[row][column]
                        streak = 1
                    } else {
                        streak++
                        if (streak == 5) score += 3
                        else if (streak > 5) score++
                    }
                }
            }
            for (column in 0 until matrix.size) {
                var lastValue = 2
                var streak = 1
                for (row in 0 until matrix.size) {
                    if (matrix[row][column] != lastValue) {
                        lastValue = matrix[row][column]
                        streak = 1
                    } else {
                        streak++
                        if (streak == 5) score += 3
                        else if (streak > 5) score++
                    }
                }
            }
            return score
        }

        /**
         * penalty 3 for each 2x2 square with same color
         */
        private fun evaluateCondition2(matrix: Array<IntArray>): Int {
            var score = 0
            for (row in 0 until (matrix.size - 1)) {
                for (column in 0 until (matrix.size - 1)) {
                    if (matrix[row][column] == matrix[row][column + 1] &&
                        matrix[row][column] == matrix[row + 1][column] &&
                        matrix[row][column] == matrix[row + 1][column + 1]) {
                        score += 3
                    }
                }
            }
            return score
        }

        /**
         * penalty 40 for 00001011101 or 10111010000
         */
        private fun evaluateCondition3(matrix: Array<IntArray>): Int {
            var score = 0
            val penaltyPattern1 = intArrayOf(0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1)
            val penaltyPattern2 = intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0)
            for (row in 0 until matrix.size) {
                for (column in 0 until (matrix.size - 10)) {
                    val extract = matrix[row].sliceArray(column .. column + 10)
                    if (extract.contentEquals(penaltyPattern1) ||
                        extract.contentEquals(penaltyPattern2)) {
                        score += 40
                    }
                }
            }
            for (column in 0 until matrix.size) {
                for (row in 0 until (matrix.size - 10)) {
                    val extract = matrix.sliceArray(row .. row + 10)
                        .map { it[column] }.toIntArray()
                    if (extract.contentEquals(penaltyPattern1) ||
                        extract.contentEquals(penaltyPattern2)) {
                        score += 40
                    }
                }
            }
            return score
        }

        private fun evaluateCondition4(matrix: Array<IntArray>): Int {
            val totalModules = matrix.size * matrix.size
            var darkModules = 0
            for (row in 0 until matrix.size) {
                for (column in 0 until matrix.size) {
                    if (matrix[row][column] == 1) darkModules++
                }
            }
            val darkPercentage = floor(darkModules.toDouble() / totalModules * 100).toInt()
            val lowerBoundScore = abs((darkPercentage / 5) - 50) / 5
            val upperBoundScore = abs((darkPercentage / 5) - 45) / 5
            return min(lowerBoundScore, upperBoundScore) * 10
        }
    }
}