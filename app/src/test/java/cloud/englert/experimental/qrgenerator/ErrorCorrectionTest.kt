package cloud.englert.experimental.qrgenerator

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorCorrectionTest {
    val data1 = arrayOf(67,85,70,134,87,38,85,194,119,50,6,18,6,103,38).toIntArray()
    val result1 =
        arrayOf(213,199,11,45,115,247,241,223,229,248,154,117,154,111,86,161,111,39).toIntArray()
    val data2 = arrayOf(246,246,66,7,118,134,242,7,38,86,22,198,199,146,6).toIntArray()
    val result2 =
        arrayOf(87,204,96,60,202,182,124,157,200,134,27,129,209,17,163,163,120,133).toIntArray()
    val data3 = arrayOf(182,230,247,119,50,7,118,134,87,38,82,6,134,151,50,7).toIntArray()
    val result3 =
        arrayOf(148,116,177,212,76,133,75,242,238,76,195,230,189,10,108,240,192,141).toIntArray()
    val data4 = arrayOf(70,247,118,86,194,6,151,50,224,236,17,236,17,236,17,236).toIntArray()
    val result4 =
        arrayOf(140,100,250,247,108,131,37,104,253,113,111,235,197,83,6,205,89,74).toIntArray()

    @Test
    fun canMultiply() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.multiply(16, 32)
        assertEquals(58, result)
    }

    @Test
    fun canDivide() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.divide(58, 32)
        assertEquals(16, result)
    }

    @Test
    fun canGetGeneratorPoly() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.getGeneratorPoly(16)
        val expected = intArrayOf(1,59,13,104,189,68,209,30,8,163,65,41,229,98,50,36,59)
        println(result.contentToString())
        println(expected.contentToString())
        assert(result.contentEquals(expected))
    }

    @Test
    fun canMultiplyPoly() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection
            .polyMultiply(intArrayOf(1,3,2), intArrayOf(2,1,7))
        val expected = intArrayOf(2,7,14,23,14)
        println(result.contentToString())
        println(expected.contentToString())
        assert(result.contentEquals(expected))
    }

    @Test
    fun canModuloPoly() {

    }

    @Test
    fun example1() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.getErrorCorrectionCodewords(data1, 18)
        println(result.contentToString())
        println(result1.contentToString())
        assert(result.contentEquals(result1))
    }

    @Test
    fun example2() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.getErrorCorrectionCodewords(data2, 18)
        println(result.contentToString())
        println(result2.contentToString())
        assert(result.contentEquals(result2))
    }

    @Test
    fun example3() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.getErrorCorrectionCodewords(data3, 18)
        println(result.contentToString())
        println(result3.contentToString())
        assert(result.contentEquals(result3))
    }

    @Test
    fun example4() {
        val errorCorrection = ErrorCorrection()
        val result = errorCorrection.getErrorCorrectionCodewords(data4, 18)
        println(result.contentToString())
        println(result4.contentToString())
        assert(result.contentEquals(result4))
    }
}