package cloud.englert.experimental.qrgenerator

import org.junit.Assert.assertEquals
import org.junit.Test

class EncodingModeTest {
    @Test
    fun canDetectNumeric() {
        assertEquals(0b0001, EncodingMode.of("1234"))
    }

    @Test
    fun canDetectAlphanumeric() {
        assertEquals(0b0010, EncodingMode.of("Hallo1234"))
    }

    @Test
    fun canDetectLatin1() {
        assertEquals(0b0100, EncodingMode.of("Hello World!"))
    }

    @Test
    fun canDetectKanji() {
        assertEquals(0b1000, EncodingMode.of("漢字"))
    }
}