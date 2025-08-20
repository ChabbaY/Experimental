package cloud.englert.experimental.qrgenerator

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