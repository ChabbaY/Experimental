package cloud.englert.experimental.qrgenerator

import cloud.englert.experimental.qrgenerator.EncodingMode.Mode

class QRCodeGenerator() {
    private lateinit var version: Version
    private var binaryData = StringBuilder("")
    private var errorCorrection = ErrorCorrection()

    fun generate(content: String) {
        val mode = EncodingMode.of(content)
        version = Version.of(mode, content.length,
            Version.ErrorCorrection.MEDIUM)
        binaryData.append(toBinary(mode, 4))
        val numLengthBits = version.getLengthBitsNumber()
        binaryData.append(toBinary(content.length, numLengthBits))
        appendBinaryData(content)
        appendPadding()
        // appendErrorCorrection() TODO
    }

    fun getBinaryData(): String {
        return binaryData.toString()
    }

    private fun getBinaryDataAsArray(): IntArray {
        val bytes = binaryData.toString().chunked(8)
        val result = IntArray(bytes.size)
        for (index in 0 until bytes.size) {
            result[index] = binaryToInt(bytes[index])
        }
        return result
    }

    private fun appendBinaryData(content: String) {
        when(version.mode) {
            Mode.NUMERIC.value -> {
                val groups = content.chunked(3)
                for (group in groups) {
                    val number = group.toInt()
                    when(group.length) {
                        3 -> binaryData.append(toBinary(number, 10))
                        2 -> binaryData.append(toBinary(number, 7))
                        1 -> binaryData.append(toBinary(number, 4))
                    }
                }
            }
            Mode.ALPHANUMERIC.value -> {
                val groups = content.chunked(2)
                for (group in groups) {
                    when(group.length) {
                        2 -> {
                            val number = alphanumericCode(group[0]) * 45 +
                                    alphanumericCode(group[1])
                            binaryData.append(toBinary(number, 11))
                        }
                        1 -> {
                            val number = alphanumericCode(group[0])
                            binaryData.append(toBinary(number, 6))
                        }
                    }
                }
            }
            Mode.BYTE.value, Mode.ECI.value -> {
                for (char in content) {
                    binaryData.append(toBinary(char.code, 8))
                }
            }
            Mode.KANJI.value -> {
                // TODO kanji encoding
            }
        }
    }

    private fun appendPadding() {
        val requiredDataBits = version.getDataCodewordsNumber() * 8
        var remainingBits = requiredDataBits - binaryData.length
        if (remainingBits > 0) {
            if (remainingBits > 3) {
                // add 4 bit terminator and fill up last byte if necessary
                binaryData.append("0000")
                val lastByteBits = binaryData.length % 8
                if (lastByteBits != 0) {
                    binaryData.append("0".repeat(8 - lastByteBits))
                    remainingBits -= (8 - lastByteBits)
                }
                remainingBits -= 4

                // if necessary add pad bytes
                if (remainingBits > 0) {
                    val remainingBytes = remainingBits / 8
                    for (index in 0 until remainingBytes) {
                        if (index % 2 == 0) {
                            binaryData.append("11101100") // 236
                        } else {
                            binaryData.append("00010001") // 17
                        }
                    }
                }
            } else {
                // add remaining bits
                binaryData.append("0".repeat(remainingBits))
            }
        }
    }

    private fun appendErrorCorrection() {
        val codewords = errorCorrection.getErrorCorrectionCodewords(getBinaryDataAsArray(),
            version.getErrorCorrectionCodewordsNumber())
        for (codeword in codewords) {
            binaryData.append(toBinary(codeword, 8))
        }
    }

    private fun toBinary(number: Int, length: Int): String {
        val builder = StringBuilder("")
        for (index in 0 until length) {
            builder.append(number.shr(length - index - 1).and(1))
        }
        return builder.toString()
    }

    private fun binaryToInt(binary: String): Int {
        var result = 0
        for (index in 0 until binary.length) {
            result += binary[index].digitToInt().shl(binary.length - index - 1)
        }
        return result
    }

    private fun alphanumericCode(char: Char): Int {
        return when(char) {
            '0' -> 0
            '1' -> 1
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'A' -> 10
            'B' -> 11
            'C' -> 12
            'D' -> 13
            'E' -> 14
            'F' -> 15
            'G' -> 16
            'H' -> 17
            'I' -> 18
            'J' -> 19
            'K' -> 20
            'L' -> 21
            'M' -> 22
            'N' -> 23
            'O' -> 24
            'P' -> 25
            'Q' -> 26
            'R' -> 27
            'S' -> 28
            'T' -> 29
            'U' -> 30
            'V' -> 31
            'W' -> 32
            'X' -> 33
            'Y' -> 34
            'Z' -> 35
            ' ' -> 36
            '$' -> 37
            '%' -> 38
            '*' -> 39
            '+' -> 40
            '-' -> 41
            '.' -> 42
            '/' -> 43
            else -> 44 // ':'
        }
    }
}