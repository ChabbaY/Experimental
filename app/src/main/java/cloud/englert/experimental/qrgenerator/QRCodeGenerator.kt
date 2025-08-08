package cloud.englert.experimental.qrgenerator

import kotlin.math.floor
import kotlin.math.log

class QRCodeGenerator() {
    private var mode: Int = 0
    private var version: Int = 0

    fun generate(value: String) {
        mode = EncodingMode.of(value)
        version = Version.of(mode, value.length, Version.ErrorCorrection.LOW)
        val lengthBitsSize = getLengthBits(mode, version)
    }

    private fun getLengthBits(mode: Int, version: Int): Int {
        val modeIndex = floor(log(mode.toDouble(), 2.0)).toInt()
        val bitsIndex = if (version > 26) 2
        else if (version > 9) 1
        else 0
        return LENGTH_BITS[modeIndex][bitsIndex]
    }

    companion object {
        private val LENGTH_BITS = arrayOf(
            arrayOf(10, 12, 14),
            arrayOf(9, 11, 13),
            arrayOf(8, 16, 16),
            arrayOf(8, 10, 12)
        )
    }
}