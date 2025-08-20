package cloud.englert.experimental.qrgenerator

class ErrorCorrection() {
    init {
        var value = 1
        for (exponent in 1..255) {
            value = if (value > 127) {
                value.shl(1).xor(285)
            } else {
                value.shl(1)
            }
            LOG[value] = exponent
            EXP[exponent] = value
        }

        LOG[1] = 0
        EXP[0] = 1
    }

    fun getErrorCorrectionCodewords(data: IntArray, numECCodewords: Int): IntArray {
        val messagePoly = data.copyOf()
        return polyDivideRest(messagePoly, getGeneratorPoly(numECCodewords))
    }

    private fun getGeneratorPoly(degree: Int): IntArray {
        var result = arrayOf(1).toIntArray()
        for (index in 0 until degree) {
            result = polyMultiply(result, arrayOf(1, EXP[index]).toIntArray())
        }
        return result
    }

    private fun multiply(a: Int, b: Int): Int {
        return EXP[(LOG[a] + LOG[b]) % 255]
    }

    private fun divide(a: Int, b: Int): Int {
        return EXP[(LOG[a] + LOG[b] * 254) % 255]
    }

    private fun polyMultiply(poly1: IntArray, poly2: IntArray): IntArray {
        val result = IntArray(poly1.size + poly2.size - 1)
        for (index in 0 until result.size) {
            var coefficient = 0
            for (index1 in 0..index) {
                val index2 = index - index1
                if (index1 < poly1.size && index2 < poly2.size) {
                    coefficient =
                        coefficient.xor(multiply(poly1[index1], poly2[index2]))
                }
            }
            result[index] = coefficient
        }
        return result
    }

    private fun polyDivideRest(poly1: IntArray, poly2: IntArray): IntArray {
        val quotientLength = poly1.size - poly2.size + 1
        var result = poly1.copyOf()
        for (count in 0 until quotientLength) {
            if (result[0] != 0) {
                val factor = arrayOf(divide(result[0], poly2[0])).toIntArray()
                val multiplied = polyMultiply(poly2, factor)
                val subtr = IntArray(result.size)
                for (index in 0 until multiplied.size) {
                    if (index < subtr.size) subtr[index] = multiplied[index]
                }
                result.mapIndexed { index, value -> value.xor(subtr[index])}
            }

            result = result.slice(1..result.size - 1).toIntArray()
        }
        return result
    }

    companion object {
        private val LOG = IntArray(256)
        private val EXP = IntArray(256)
    }
}