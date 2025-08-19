package cloud.englert.experimental.qrgenerator

import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

import cloud.englert.experimental.qrgenerator.Version.ErrorCorrection

class VersionTest {
    @Test
    fun versionWithMinMode() {
        assertEquals(22, version1.code)
        assertEquals(ErrorCorrection.MEDIUM, version1.errorCorrection)
        assertEquals(0b0001, version1.mode)
    }

    @Test
    fun versionWithMaxMode() {
        assertEquals(13, version2.code)
        assertEquals(ErrorCorrection.MEDIUM, version2.errorCorrection)
        assertEquals(0b1000, version2.mode)
    }

    @Test
    fun versionWithMinCharacters() {
        assertEquals(1, version3.code)
        assertEquals(ErrorCorrection.QUARTILE, version3.errorCorrection)
        assertEquals(0b0100, version3.mode)
    }

    @Test
    fun versionWithMaxCharacters() {
        assertEquals(40, version4.code)
        assertEquals(ErrorCorrection.QUARTILE, version4.errorCorrection)
        assertEquals(0b0100, version4.mode)
    }

    @Test
    fun versionWithMinErrorCorrection() {
        assertEquals(5, version5.code)
        assertEquals(ErrorCorrection.LOW, version5.errorCorrection)
        assertEquals(0b0010, version5.mode)
    }

    @Test
    fun versionWithMaxErrorCorrection() {
        assertEquals(32, version6.code)
        assertEquals(ErrorCorrection.HIGH, version6.errorCorrection)
        assertEquals(0b0010, version6.mode)
    }

    @Test
    fun hasCorrectLengthBitsNumber() {
        assertEquals(12, version1.getLengthBitsNumber())
        assertEquals(10, version2.getLengthBitsNumber())
        assertEquals( 8, version3.getLengthBitsNumber())
        assertEquals(16, version4.getLengthBitsNumber())
        assertEquals( 9, version5.getLengthBitsNumber())
        assertEquals(13, version6.getLengthBitsNumber())
    }

    @Test
    fun hasCorrectDataCodewordsNumber() {
        assertEquals( 782, version1.getDataCodewordsNumber())
        assertEquals( 334, version2.getDataCodewordsNumber())
        assertEquals(  13, version3.getDataCodewordsNumber())
        assertEquals(1666, version4.getDataCodewordsNumber())
        assertEquals( 108, version5.getDataCodewordsNumber())
        assertEquals( 845, version6.getDataCodewordsNumber())
    }

    companion object {
        lateinit var version1: Version; lateinit var version2: Version
        lateinit var version3: Version; lateinit var version4: Version
        lateinit var version5: Version; lateinit var version6: Version

        @BeforeClass
        @JvmStatic
        internal fun init() {
            // mode min (num) and max (kanji); versions: 22, 13
            version1 = Version.of(0b0001, 1872, ErrorCorrection.MEDIUM)
            version2 = Version.of(0b1000, 204, ErrorCorrection.MEDIUM)
            // characters min and max; versions: 1, 40
            version3 = Version.of(0b0100, 11, ErrorCorrection.QUARTILE)
            version4 = Version.of(0b0100, 1663, ErrorCorrection.QUARTILE)
            // error correction min (LOW) and max (HIGH); versions: 5, 32
            version5 = Version.of(0b0010, 154, ErrorCorrection.LOW)
            version6 = Version.of(0b0010, 1226, ErrorCorrection.HIGH)
        }
    }
}