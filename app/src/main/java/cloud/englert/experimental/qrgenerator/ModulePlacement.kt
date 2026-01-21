package cloud.englert.experimental.qrgenerator

class ModulePlacement {
    companion object {
        /**
         * black module as 1, white module as 0
         */
        fun placeData(data: String, version: Int): Array<Array<IntArray>> {
            val size = version * 4 + 17
            // matrix[row][column], prefilled with 2 (unallocated)
            val matrix = Array(size) { IntArray(size) { 2 } }
            val dataModules = Array(size) { IntArray(size) }

            // finder patterns
            setFinderPattern(matrix, 3, 3)
            setFinderPattern(matrix, size - 4, 3)
            setFinderPattern(matrix, 3, size - 4)
            setSeparators(matrix, size)

            // alignment patterns
            if (version > 1) {
                val alignmentPatternLocations = getAlignmentPatternLocations(version)
                for (coordinate in alignmentPatternLocations) {
                    if (coordinate == null) continue
                    setAlignmentPattern(matrix, coordinate[0], coordinate[1])
                }
            }

            // timing patterns
            setTimingPatterns(matrix, size)

            // dark module
            matrix[version * 4 + 9][8] = 1

            // reserve areas
            reserveAreas(matrix, size)

            // fill in data
            placeDataBits(matrix, data, size, dataModules)

            return arrayOf(matrix, dataModules)
        }

        fun setFormatAndVersionInformation(matrix: Array<IntArray>, version: Int,
                                           errorCorrection: Version.ErrorCorrection, mask: Int) {
            // format information
            val formatInformation = getFormatInformation(errorCorrection, mask)
            matrix[8][0] = formatInformation[0]
            matrix[8][1] = formatInformation[1]
            matrix[8][2] = formatInformation[2]
            matrix[8][3] = formatInformation[3]
            matrix[8][4] = formatInformation[4]
            matrix[8][5] = formatInformation[5]
            matrix[8][7] = formatInformation[6]
            matrix[8][8] = formatInformation[7]
            matrix[7][8] = formatInformation[8]
            matrix[5][8] = formatInformation[9]
            matrix[4][8] = formatInformation[10]
            matrix[3][8] = formatInformation[11]
            matrix[2][8] = formatInformation[12]
            matrix[1][8] = formatInformation[13]
            matrix[0][8] = formatInformation[14]
            matrix[matrix.size - 1][8] = formatInformation[0]
            matrix[matrix.size - 2][8] = formatInformation[1]
            matrix[matrix.size - 3][8] = formatInformation[2]
            matrix[matrix.size - 4][8] = formatInformation[3]
            matrix[matrix.size - 5][8] = formatInformation[4]
            matrix[matrix.size - 6][8] = formatInformation[5]
            matrix[matrix.size - 7][8] = formatInformation[6]
            matrix[8][matrix.size - 8] = formatInformation[7]
            matrix[8][matrix.size - 7] = formatInformation[8]
            matrix[8][matrix.size - 6] = formatInformation[9]
            matrix[8][matrix.size - 5] = formatInformation[10]
            matrix[8][matrix.size - 4] = formatInformation[11]
            matrix[8][matrix.size - 3] = formatInformation[12]
            matrix[8][matrix.size - 2] = formatInformation[13]
            matrix[8][matrix.size - 1] = formatInformation[14]

            // version information
            if (version > 6) {
                val versionInformation = getVersionInformation(version)
                // bottom left
                var index = 0
                for (column in (0 .. 5).reversed()) {
                    for (row in ((matrix.size - 9) .. (matrix.size - 11)).reversed()) {
                        matrix[row][column] = versionInformation[index]
                        index++
                    }
                }
                // top right
                index = 0
                for (row in (0 .. 5).reversed()) {
                    for (column in ((matrix.size - 9) .. (matrix.size - 11)).reversed()) {
                        matrix[row][column] = versionInformation[index]
                        index++
                    }
                }
            }
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

        private fun setSeparators(matrix: Array<IntArray>, size: Int) {
            for (row in 0 .. 7) {
                matrix[row][7] = 0
                matrix[row][size - 8] = 0
            }
            for (row in (size - 8) ..< size) {
                matrix[row][7] = 0
            }
            for (column in 0 .. 6) {
                matrix[7][column] = 0
                matrix[size - 8][column] = 0
            }
            for (column in (size - 7) ..< size) {
                matrix[7][column] = 0
            }
        }

        private fun setAlignmentPattern(matrix: Array<IntArray>, row: Int, column: Int) {
            for (rowIndex in -1 .. 1) {
                for (columnIndex in -1 .. 1) {
                    matrix[row + rowIndex][column + columnIndex] = 0
                }
            }
            matrix[row][column] = 1
            for (index in -2 .. 2) {
                matrix[row - 2][column + index] = 1
                matrix[row + 2][column + index] = 1
                matrix[row + index][column - 2] = 1
                matrix[row + index][column + 2] = 1
            }
        }

        private fun setTimingPatterns(matrix: Array<IntArray>, size: Int) {
            for (row in 8 .. (size - 9)) {
                matrix[row][6] = if (row % 2 == 0) 1 else 0
            }
            for (column in 8 .. (size - 9)) {
                matrix[6][column] = if (column % 2 == 0) 1 else 0
            }
        }

        private fun reserveAreas(matrix: Array<IntArray>, size: Int) {
            // format information
            for (row in 0 .. 5) {
                matrix[row][8] = 3
            }
            matrix[7][8] = 3
            matrix[8][8] = 3
            matrix[8][7] = 3
            for (column in 0 .. 5) {
                matrix[8][column] = 3
            }
            for (row in (size - 7) ..< size) {
                matrix[row][8] = 3
            }
            for (column in (size - 8) ..< size) {
                matrix[8][column] = 3
            }

            // version information (version 7 or higher)
            if (size > 44) {
                for (index1 in 0 .. 5) {
                    for (index2 in (size - 9) .. (size - 11)) {
                        matrix[index1][index2] = 3
                        matrix[index2][index1] = 3
                    }
                }
            }
        }

        private fun placeDataBits(matrix: Array<IntArray>, data: String, size: Int,
                                  dataModules: Array<IntArray>) {
            val columns = size / 2
            var dataIndex = 0
            for (index in 0 until columns) {
                var column = size - 1 - (index * 2)
                if (column < 7) column--
                val upwards = index % 2 == 0

                var row = if (upwards) size - 1 else 0
                val deltaRow = if (upwards) -1 else 1

                while ((row > -1) && (row < size)) {
                    for (columnIndex in 0 .. 1) {
                        if (matrix[row][column - columnIndex] == 2) {
                            if (dataIndex < data.length) {
                                matrix[row][column - columnIndex] =
                                    if (data[dataIndex] == '1') 1 else 0
                                dataIndex++
                            } else {
                                matrix[row][column - columnIndex] = 0
                            }
                            dataModules[row][column - columnIndex] = 1
                        }
                    }

                    row += deltaRow
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

        private fun getFormatInformation(errorCorrection: Version.ErrorCorrection,
                                         mask: Int): IntArray {
            return FORMAT_INFORMATION[errorCorrection.ordinal][mask].toIntArray()
        }

        private fun getVersionInformation(version: Int): IntArray {
            return VERSION_INFORMATION[version - 7].toIntArray()
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

        /**
         * format information by ec -> mask
         */
        private val FORMAT_INFORMATION = arrayOf(
            arrayOf( // ec L
                arrayOf(1,1,1,0,1,1,1,1,1,0,0,0,1,0,0), arrayOf(1,1,1,0,0,1,0,1,1,1,1,0,0,1,1),
                arrayOf(1,1,1,1,1,0,1,1,0,1,0,1,0,1,0), arrayOf(1,1,1,1,0,0,0,1,0,0,1,1,1,0,1),
                arrayOf(1,1,0,0,1,1,0,0,0,1,0,1,1,1,1), arrayOf(1,1,0,0,0,1,1,0,0,0,1,1,0,0,0),
                arrayOf(1,1,0,1,1,0,0,0,1,0,0,0,0,0,1), arrayOf(1,1,0,1,0,0,1,0,1,1,1,0,1,1,0)
            ), arrayOf( // ec M
                arrayOf(1,0,1,0,1,0,0,0,0,0,1,0,0,1,0), arrayOf(1,0,1,0,0,0,1,0,0,1,0,0,1,0,1),
                arrayOf(1,0,1,1,1,1,0,0,1,1,1,1,1,0,0), arrayOf(1,0,1,1,0,1,1,0,1,0,0,1,0,1,1),
                arrayOf(1,0,0,0,1,0,1,1,1,1,1,1,0,0,1), arrayOf(1,0,0,0,0,0,0,1,1,0,0,1,1,1,0),
                arrayOf(1,0,0,1,1,1,1,1,0,0,1,0,1,1,1), arrayOf(1,0,0,1,0,1,0,1,0,1,0,0,0,0,0)
            ), arrayOf( // ec Q
                arrayOf(0,1,1,0,1,0,1,0,1,0,1,1,1,1,1), arrayOf(0,1,1,0,0,0,0,0,1,1,0,1,0,0,0),
                arrayOf(0,1,1,1,1,1,1,0,0,1,1,0,0,0,1), arrayOf(0,1,1,1,0,1,0,0,0,0,0,0,1,1,0),
                arrayOf(0,1,0,0,1,0,0,1,0,1,1,0,1,0,0), arrayOf(0,1,0,0,0,0,1,1,0,0,0,0,0,1,1),
                arrayOf(0,1,0,1,1,1,0,1,1,0,1,1,0,1,0), arrayOf(0,1,0,1,0,1,1,1,1,1,0,1,1,0,1)
            ), arrayOf( // ec H
                arrayOf(0,0,1,0,1,1,0,1,0,0,0,1,0,0,1), arrayOf(0,0,1,0,0,1,1,1,0,1,1,1,1,1,0),
                arrayOf(0,0,1,1,1,0,0,1,1,1,0,0,1,1,1), arrayOf(0,0,1,1,0,0,1,1,1,0,1,0,0,0,0),
                arrayOf(0,0,0,0,1,1,1,0,1,1,0,0,0,1,0), arrayOf(0,0,0,0,0,1,0,0,1,0,1,0,1,0,1),
                arrayOf(0,0,0,1,1,0,1,0,0,0,0,1,1,0,0), arrayOf(0,0,0,1,0,0,0,0,0,1,1,1,0,1,1)
            )
        )

        /**
         * version information for versions 7-40
         */
        private val VERSION_INFORMATION = arrayOf(
            arrayOf(0,0,0,1,1,1,1,1,0,0,1,0,0,1,0,1,0,0),
            arrayOf(0,0,1,0,0,0,0,1,0,1,1,0,1,1,1,1,0,0),
            arrayOf(0,0,1,0,0,1,1,0,1,0,1,0,0,1,1,0,0,1),
            arrayOf(0,0,1,0,1,0,0,1,0,0,1,1,0,1,0,0,1,1),
            arrayOf(0,0,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,0),
            arrayOf(0,0,1,1,0,0,0,1,1,1,0,1,1,0,0,0,1,0),
            arrayOf(0,0,1,1,0,1,1,0,0,0,0,1,0,0,0,1,1,1),
            arrayOf(0,0,1,1,1,0,0,1,1,0,0,0,0,0,1,1,0,1),
            arrayOf(0,0,1,1,1,1,1,0,0,1,0,0,1,0,1,0,0,0),
            arrayOf(0,1,0,0,0,0,1,0,1,1,0,1,1,1,1,0,0,0),
            arrayOf(0,1,0,0,0,1,0,1,0,0,0,1,0,1,1,1,0,1),
            arrayOf(0,1,0,0,1,0,1,0,1,0,0,0,0,1,0,1,1,1),
            arrayOf(0,1,0,0,1,1,0,1,0,1,0,0,1,1,0,0,1,0),
            arrayOf(0,1,0,1,0,0,1,0,0,1,1,0,1,0,0,1,1,0),
            arrayOf(0,1,0,1,0,1,0,1,1,0,1,0,0,0,0,0,1,1),
            arrayOf(0,1,0,1,1,0,1,0,0,0,1,1,0,0,1,0,0,1),
            arrayOf(0,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,0,0),
            arrayOf(0,1,1,0,0,0,1,1,1,0,1,1,0,0,0,1,0,0),
            arrayOf(0,1,1,0,0,1,0,0,0,1,1,1,1,0,0,0,0,1),
            arrayOf(0,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,1),
            arrayOf(0,1,1,0,1,1,0,0,0,0,1,0,0,0,1,1,1,0),
            arrayOf(0,1,1,1,0,0,1,1,0,0,0,0,0,1,1,0,1,0),
            arrayOf(0,1,1,1,0,1,0,0,1,1,0,0,1,1,1,1,1,1),
            arrayOf(0,1,1,1,1,0,1,1,0,1,0,1,1,1,0,1,0,1),
            arrayOf(0,1,1,1,1,1,0,0,1,0,0,1,0,1,0,0,0,0),
            arrayOf(1,0,0,0,0,0,1,0,0,1,1,1,0,1,0,1,0,1),
            arrayOf(1,0,0,0,0,1,0,1,1,0,1,1,1,1,0,0,0,0),
            arrayOf(1,0,0,0,1,0,1,0,0,0,1,0,1,1,1,0,1,0),
            arrayOf(1,0,0,0,1,1,0,1,1,1,1,0,0,1,1,1,1,1),
            arrayOf(1,0,0,1,0,0,1,0,1,1,0,0,0,0,1,0,1,1),
            arrayOf(1,0,0,1,0,1,0,1,0,0,0,0,1,0,1,1,1,0),
            arrayOf(1,0,0,1,1,0,1,0,1,0,0,1,1,0,0,1,0,0),
            arrayOf(1,0,0,1,1,1,0,1,0,1,0,1,0,0,0,0,0,1),
            arrayOf(1,0,1,0,0,0,1,1,0,0,0,1,1,0,1,0,0,1)
        )
    }
}