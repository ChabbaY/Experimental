package cloud.englert.experimental.qrgenerator

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Order

class QRCodeGeneratorTest {
    /**
     * mode 0001 length 0000000110 123 0001111011 456 0111001000
     * len 34 req 16*8 = 128 term 0000 00 + 11 pad bytes 11101100 & 00010001
     */
    @Test
    @Order(1)
    fun canGenerateNumericBinary() {
        val generator = QRCodeGenerator()
        generator.generate("123456")
        assertEquals("0001000000011000011110110111001000000000111011000001000111101100" +
                "0001000111101100000100011110110000010001111011000001000111101100",
            generator.getBinaryData())
    }

    /**
     * mode 0010 length 000001011 HE 779 LL 966 O  1116 WO 1464 RL 1236 D 13
     * HE 01100001011 LL 01111000110 O 10001011100 WO 10110111000 RL 10011010100 D 001101
     * len 74 req 16*8 = 128 term 0000 00 + 6 pad bytes 11101100 & 00010001
     */
    @Test
    @Order(2)
    fun canGenerateAlphanumericBinary() {
        val generator = QRCodeGenerator()
        generator.generate("HELLO WORLD")
        assertEquals("0010000001011011000010110111100011010001011100101101110001001101" +
                "0100001101000000111011000001000111101100000100011110110000010001",
            generator.getBinaryData())
    }

    /**
     * mode 0100 length 00001101 H 01001000 e 01100101 l 01101100 l 01101100 o 01101111 , 00101100
     *   00100000 w 01110111 o 01101111 r 01110010 l 01101100 d 01100100 ! 00100001
     *   len 116 req 16*8 = 128 term 0000 + 1 pad byte 11101100
     */
    @Test
    @Order(3)
    fun canGenerateByteBinary() {
        val generator = QRCodeGenerator()
        generator.generate("Hello, world!")
        assertEquals("0100000011010100100001100101011011000110110001101111001011000010" +
                "0000011101110110111101110010011011000110010000100001000011101100",
            generator.getBinaryData())
    }

    @Test
    fun canGenerateKanjiBinary() {
        // TODO kanji test once implemented
    }
}