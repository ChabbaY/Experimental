package cloud.englert.experimental.qrgenerator

import org.junit.Test

class ModulePlacementTest {
    private val data = "0001000000011000011110110111001000000000111011000001000111101100000100011" +
            "110110000010001111011000001000111101100000100011110110000100111011011011000010010110" +
            "011111101100010000000111010011010101001110101110110" // content: 123456
    private val expected = arrayOf(
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 0, 3, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(3, 3, 3, 3, 3, 3, 1, 3, 3, 0, 0, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3),
        intArrayOf(1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1),
        intArrayOf(1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1),
        intArrayOf(1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1),
        intArrayOf(0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 0, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 1, 0, 3, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0),
        intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 3, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0),
        intArrayOf(1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0),
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    )
    @Test
    fun testPlacement() {
        val result = ModulePlacement.placeData(data, 1)
        for (index in 0 until 21) {
            // println(result[index].contentToString())
            // println(expected[index].contentToString())
            // println()
            assert(result[0][index].contentEquals(expected[index]))
        }
    }
}