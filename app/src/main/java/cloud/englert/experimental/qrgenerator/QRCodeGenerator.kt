package cloud.englert.experimental.qrgenerator

class QRCodeGenerator() {
    private var mode: Int = 0
    private lateinit var version: Version

    fun generate(content: String) {
        mode = EncodingMode.of(content)
        version = Version.of(mode, content.length, Version.ErrorCorrection.LOW)
        val numLengthBits = version.getLengthBitsNumber(mode)
        val numDataCodewords = version.getDataCodewordsNumber()
        getByteData(content, numLengthBits, numDataCodewords)
    }

    private fun getByteData(content: String, numLengthBits: Int, numDataCodewords: Int): IntArray {
        val data = IntArray(numDataCodewords)
        val rightShift = (4 + numLengthBits).and(7)
        val leftShift = 8 - rightShift
        val andMask = 0x01.shl(rightShift) - 1
        val dataIndexStart = if(numLengthBits > 12) 2 else 1

        data[0] = 64 + (content.length.shr(numLengthBits - 4))
        if (numLengthBits > 12) {
            data[1] = content.length.shr(rightShift).and(255)
        }
        data[dataIndexStart] = content.length.and(andMask).shl(leftShift)

        for (index in 0 until content.length) {
            val byte = content[index].code
            data[index + dataIndexStart] =
                data[index + dataIndexStart].or(byte.shr(rightShift))
            data[index + dataIndexStart + 1] = byte.and(andMask).shl(leftShift)
        }
        val remaining = numDataCodewords - content.length - dataIndexStart - 1
        for (index in 0 until remaining) {
            val byte = if (index % 2 == 1) 17 else 236
            data[index + content.length + 2] = byte
        }

        return data
    }
}