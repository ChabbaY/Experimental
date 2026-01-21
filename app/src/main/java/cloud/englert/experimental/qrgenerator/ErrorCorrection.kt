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

    fun getGeneratorPoly(degree: Int): IntArray {
        var result = intArrayOf(1)
        for (index in 0 until degree) {
            result = polyMultiply(result, intArrayOf(1, EXP[index]))
        }
        return result
    }

    fun multiply(a: Int, b: Int): Int {
        return EXP[(LOG[a] + LOG[b]) % 255]
    }

    fun divide(a: Int, b: Int): Int {
        return EXP[(LOG[a] + LOG[b] * 254) % 255]
    }

    fun polyMultiply(poly1: IntArray, poly2: IntArray): IntArray {
        val result = IntArray(poly1.size + poly2.size - 1)
        for (index1 in 0 until poly1.size) {
            for (index2 in 0 until poly2.size) {
                result[index1 + index2] =
                    result[index1 + index2] xor multiply(poly1[index1], poly2[index2])
            }
        }
        return result
    }

    fun polyDivideRest(poly1: IntArray, poly2: IntArray): IntArray {
        val steps = poly1.size
        var result = poly1.copyOf(poly1.size + poly2.size - 1)
        (1..steps).forEach { _ ->
            val multiplied = polyMultiply(poly2, intArrayOf(result[0]))
            result = result.mapIndexed { index, value ->
                if (index < multiplied.size) value.xor(multiplied[index])
                else value
            }.toIntArray()
            result = result.slice(1..< result.size).toIntArray()
        }
        return result
    }

    companion object {
        private val LOG = IntArray(256)
        private val EXP = IntArray(256)
    }
}