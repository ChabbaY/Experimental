package cloud.englert.experimental.qrgenerator

class ModulePlacement {
    companion object {
        /**
         * black module as 1, white module as 0
         */
        fun placeData(data: String, version: Int) {
            val size = version * 4 + 17
            // matrix[row][column], prefilled with 2 (unallocated)
            val matrix = Array(size) { it -> IntArray(size) { it -> 2 } }

            // finder patterns
            setFinderPattern(matrix, 3, 3)
            setFinderPattern(matrix, size - 4, 3)
            setFinderPattern(matrix, 3, size - 4)

            val alignmentPatternLocations = getAlignmentPatternLocations(version)

        }

        /**
         * Sets the finder pattern in the matrix. Row & column define the center
         */
        private fun setFinderPattern(matrix: Array<IntArray>, row: Int, column: Int) {
            for (rowIndex in -3 .. 3) {
                for (columnIndex in -3 .. 3) {
                    if (((rowIndex == -3) || (rowIndex == 3)) ||
                        ((columnIndex == -3) || (columnIndex == 3)) ||
                        ((rowIndex > -2) && (rowIndex < 2) &&
                                (columnIndex > -2) && (columnIndex < 2))) {
                        matrix[row + rowIndex][column + columnIndex] = 1
                    } else {
                        matrix[row + rowIndex][column + columnIndex] = 0
                    }
                }
            }
        }

        private fun getAlignmentPatternLocations(version: Int): Array<IntArray?> {
            val indices = ALIGNMENT_PATTERN_LOCATIONS[version - 2]
            val maxIndex = indices.size - 1
            val numPatterns = indices.size * indices.size - 3
            var patternIndex = 0
            val result = arrayOfNulls<IntArray>(numPatterns)
            for (index1 in 0 .. maxIndex) {
                for (index2 in 0 .. maxIndex) {
                    // skip if overlaps finder patterns
                    if (((index1 == 0) && (index2 == 0)) ||
                        ((index1 == 0) && (index2 == maxIndex)) ||
                        ((index1 == maxIndex) && (index2 == 0))) continue

                    result[patternIndex] = intArrayOf(indices[index1], indices[index2])
                    patternIndex++
                }
            }
            return result
        }

        // alignment patterns for versions 2-40
        private val ALIGNMENT_PATTERN_LOCATIONS = arrayOf(
            arrayOf(6, 18),
            arrayOf(6, 22),
            arrayOf(6, 26),
            arrayOf(6, 30),
            arrayOf(6, 34),
            arrayOf(6, 22, 38),
            arrayOf(6, 24, 42),
            arrayOf(6, 26, 46),
            arrayOf(6, 28, 50),
            arrayOf(6, 30, 54),
            arrayOf(6, 32, 58),
            arrayOf(6, 34, 62),
            arrayOf(6, 26, 46, 66),
            arrayOf(6, 26, 48, 70),
            arrayOf(6, 26, 50, 74),
            arrayOf(6, 30, 54, 78),
            arrayOf(6, 30, 56, 82),
            arrayOf(6, 30, 58, 86),
            arrayOf(6, 34, 62, 90),
            arrayOf(6, 28, 50, 72, 94),
            arrayOf(6, 26, 50, 74, 98),
            arrayOf(6, 30, 54, 78, 102),
            arrayOf(6, 28, 54, 80, 106),
            arrayOf(6, 32, 58, 84, 110),
            arrayOf(6, 30, 58, 86, 114),
            arrayOf(6, 34, 62, 90, 118),
            arrayOf(6, 26, 50, 74, 98, 122),
            arrayOf(6, 30, 54, 78, 102, 126),
            arrayOf(6, 26, 52, 78, 104, 130),
            arrayOf(6, 30, 56, 82, 108, 134),
            arrayOf(6, 34, 60, 86, 112, 138),
            arrayOf(6, 30, 58, 86, 114, 142),
            arrayOf(6, 34, 62, 90, 118, 146),
            arrayOf(6, 30, 54, 78, 102, 126, 150),
            arrayOf(6, 24, 50, 76, 102, 128, 154),
            arrayOf(6, 28, 54, 80, 106, 132, 158),
            arrayOf(6, 32, 58, 84, 110, 136, 162),
            arrayOf(6, 26, 54, 82, 110, 138, 166),
            arrayOf(6, 30, 58, 86, 114, 142, 170)
        )
    }
}