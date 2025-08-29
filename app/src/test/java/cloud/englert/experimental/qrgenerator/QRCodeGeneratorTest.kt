package cloud.englert.experimental.qrgenerator

import org.junit.Assert.assertEquals
import org.junit.Test

class QRCodeGeneratorTest {
    private val helloWorldResult = arrayOf(
        arrayOf(1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1),
        arrayOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0),
        arrayOf(1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0),
        arrayOf(0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0),
        arrayOf(1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0),
        arrayOf(1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0),
        arrayOf(1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0),
    )

    /**
     * mode 0001 length 0000000110 123 0001111011 456 0111001000
     * len 34 req 16*8 = 128 term 0000 00 + 11 pad bytes 11101100 & 00010001
     * ec 00100111011011011000010010110011111101100010000000111010011010101001110101110110
     */
    @Test
    fun canGenerateNumericBinary() {
        val generator = QRCodeGenerator()
        generator.generateCode("123456",
            Version.ErrorCorrection.MEDIUM)
        assertEquals("0001000000011000011110110111001000000000111011000001000111101100" +
                "00010001111011000001000111101100000100011110110000010001111011000010011101101101" +
                "1000010010110011111101100010000000111010011010101001110101110110",
            generator.getBinaryData())
    }

    /**
     * mode 0010 length 000001011 HE 779 LL 966 O  1116 WO 1464 RL 1236 D 13
     * HE 01100001011 LL 01111000110 O 10001011100 WO 10110111000 RL 10011010100 D 001101
     * len 74 req 16*8 = 128 term 0000 00 + 6 pad bytes 11101100 & 00010001
     * ec 11000100001000110010011101110111111010111101011111100111111000100101110100010111
     */
    @Test
    fun canGenerateAlphanumericBinary() {
        val generator = QRCodeGenerator()
        generator.generateCode("HELLO WORLD",
            Version.ErrorCorrection.MEDIUM)
        assertEquals("0010000001011011000010110111100011010001011100101101110001001101" +
                "01000011010000001110110000010001111011000001000111101100000100011100010000100011" +
                "0010011101110111111010111101011111100111111000100101110100010111",
            generator.getBinaryData())
    }

    /**
     * mode 0100 length 00001101 H 01001000 e 01100101 l 01101100 l 01101100 o 01101111 , 00101100
     *   00100000 w 01110111 o 01101111 r 01110010 l 01101100 d 01100100 ! 00100001
     *   len 116 req 16*8 = 128 term 0000 + 1 pad byte 11101100
     *   ec 10011100010011010010111001101101011011001110110010011011010010110011000001011110
     */
    @Test
    fun canGenerateByteBinary() {
        val generator = QRCodeGenerator()
        generator.generateCode("Hello, world!",
            Version.ErrorCorrection.MEDIUM)
        assertEquals("0100000011010100100001100101011011000110110001101111001011000010" +
                "00000111011101101111011100100110110001100100001000010000111011001001110001001101" +
                "0010111001101101011011001110110010011011010010110011000001011110",
            generator.getBinaryData())
    }

    @Test
    fun canGenerateKanjiBinary() {
        // TODO kanji test once implemented
    }

    @Test
    fun fullHelloWorldExample() {
        val generator = QRCodeGenerator()
        generator.generateCode("HELLO WORLD",
            Version.ErrorCorrection.QUARTILE)

        val placement = ModulePlacement.placeData(generator.getBinaryData(), 1)
        var matrix = placement[0]
        val dataModules = placement[1]
        val mask = Mask.apply(matrix, dataModules)
        matrix = mask.matrix
        ModulePlacement.setFormatAndVersionInformation(matrix, 1,
            Version.ErrorCorrection.QUARTILE, mask.pattern)

        for (row in 0 until 21) {
            println(matrix[row].contentToString())
            println(helloWorldResult[row].contentToString())
            println()
            assert(matrix[row].contentEquals(helloWorldResult[row].toIntArray()))
        }
    }
}