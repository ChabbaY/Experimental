package cloud.englert.experimental.qrgenerator

import kotlin.math.floor
import kotlin.math.log

class Version {
    companion object {
        fun of(mode: Int, characters: Int, errorCorrection: ErrorCorrection): Int {
            val modeIndex = floor(log(mode.toDouble(), 2.0)).toInt()
            val errorIndex = errorCorrection.ordinal
            for (version in 0..39) {
                if (CAPACITIES[version][errorIndex][modeIndex] >= characters) {
                    return version + 1
                }
            }
            return 40
        }

        private val CAPACITIES = arrayOf(
            arrayOf( // version 1
                arrayOf(41, 25, 17, 10),
                arrayOf(34, 20, 14, 8),
                arrayOf(27, 16, 11, 7),
                arrayOf(17, 10, 7, 4)
            ), arrayOf( // version 2
                arrayOf(77, 47, 32, 20),
                arrayOf(63, 38, 26, 16),
                arrayOf(48, 29, 20, 12),
                arrayOf(34, 20, 14, 8)
            ), arrayOf( // version 3
                arrayOf(127, 77, 53, 32),
                arrayOf(101, 61, 42, 26),
                arrayOf(77, 47, 32, 20),
                arrayOf(58, 35, 24, 15)
            ), arrayOf( // version 4
                arrayOf(187, 114, 78, 48),
                arrayOf(149, 90, 62, 38),
                arrayOf(111, 67, 46, 28),
                arrayOf(82, 50, 34, 21)
            ), arrayOf( // version 5
                arrayOf(255, 154, 106, 65),
                arrayOf(202, 122, 84, 52),
                arrayOf(144, 87, 60, 37),
                arrayOf(106, 64, 44, 27)
            ), arrayOf( // version 6
                arrayOf(322, 195, 134, 82),
                arrayOf(255, 154, 106, 65),
                arrayOf(178, 108, 74, 45),
                arrayOf(139, 84, 58, 36)
            ), arrayOf( // version 7
                arrayOf(370, 224, 154, 95),
                arrayOf(293, 178, 122, 75),
                arrayOf(207, 125, 86, 53),
                arrayOf(154, 93, 64, 39)
            ), arrayOf( // version 8
                arrayOf(461, 279, 192, 118),
                arrayOf(365, 221, 152, 93),
                arrayOf(259, 157, 108, 66),
                arrayOf(202, 122, 84, 52)
            ), arrayOf( // version 9
                arrayOf(552, 335, 230, 141),
                arrayOf(432, 262, 180, 111),
                arrayOf(312, 189, 130, 80),
                arrayOf(235, 143, 98, 60)
            ), arrayOf( // version 10
                arrayOf(652, 395, 271, 167),
                arrayOf(513, 311, 213, 131),
                arrayOf(364, 221, 151, 93),
                arrayOf(288, 174, 119, 74)
            ), arrayOf( // version 11
                arrayOf(772, 468, 321, 198),
                arrayOf(604, 366, 251, 155),
                arrayOf(427, 259, 177, 109),
                arrayOf(331, 200, 137, 85)
            ), arrayOf( // version 12
                arrayOf(883, 535, 367, 226),
                arrayOf(691, 419, 287, 177),
                arrayOf(489, 296, 203, 125),
                arrayOf(374, 227, 155, 96)
            ), arrayOf( // version 13
                arrayOf(1022, 619, 425, 262),
                arrayOf(796, 483, 331, 204),
                arrayOf(580, 352, 241, 149),
                arrayOf(427, 259, 177, 109)
            ), arrayOf( // version 14
                arrayOf(1101, 667, 458, 282),
                arrayOf(871, 528, 362, 223),
                arrayOf(621, 376, 258, 159),
                arrayOf(468, 283, 194, 120)
            ), arrayOf( // version 15
                arrayOf(1250, 758, 520, 320),
                arrayOf(991, 600, 412, 254),
                arrayOf(703, 426, 292, 180),
                arrayOf(530, 321, 220, 136)
            ), arrayOf( // version 16
                arrayOf(1408, 854, 586, 361),
                arrayOf(1082, 656, 450, 277),
                arrayOf(775, 470, 322, 198),
                arrayOf(602, 365, 250, 154)
            ), arrayOf( // version 17
                arrayOf(1548, 938, 644, 397),
                arrayOf(1212, 734, 504, 310),
                arrayOf(876, 531, 364, 224),
                arrayOf(674, 408, 280, 173)
            ), arrayOf( // version 18
                arrayOf(1725, 1046, 718, 442),
                arrayOf(1346, 816, 560, 345),
                arrayOf(948, 574, 394, 243),
                arrayOf(746, 452, 310, 191)
            ), arrayOf( // version 19
                arrayOf(1903, 1153, 792, 488),
                arrayOf(1500, 909, 624, 384),
                arrayOf(1063, 644, 442, 272),
                arrayOf(813, 493, 338, 208)
            ), arrayOf( // version 20
                arrayOf(2061, 1249, 858, 528),
                arrayOf(1600, 970, 666, 410),
                arrayOf(1159, 702, 482, 297),
                arrayOf(919, 557, 382, 235)
            ), arrayOf( // version 21
                arrayOf(2232, 1352, 929, 572),
                arrayOf(1708, 1035, 711, 438),
                arrayOf(1224, 742, 509, 314),
                arrayOf(969, 587, 403, 248)
            ), arrayOf( // version 22
                arrayOf(2409, 1460, 1003, 618),
                arrayOf(1872, 1134, 779, 480),
                arrayOf(1358, 823, 565, 348),
                arrayOf(1056, 640, 439, 270)
            ), arrayOf( // version 23
                arrayOf(2620, 1588, 1091, 672),
                arrayOf(2059, 1248, 857, 528),
                arrayOf(1468, 890, 611, 376),
                arrayOf(1108, 672, 461, 284)
            ), arrayOf( // version 24
                arrayOf(2812, 1704, 1171, 721),
                arrayOf(2188, 1326, 911, 561),
                arrayOf(1588, 963, 661, 407),
                arrayOf(1228, 744, 511, 315)
            ), arrayOf( // version 25
                arrayOf(3057, 1853, 1273, 784),
                arrayOf(2395, 1451, 997, 614),
                arrayOf(1718, 1041, 715, 440),
                arrayOf(1286, 779, 535, 330)
            ), arrayOf( // version 26
                arrayOf(3283, 1990, 1367, 842),
                arrayOf(2544, 1542, 1059, 652),
                arrayOf(1804, 1094, 751, 462),
                arrayOf(1425, 864, 593, 365)
            ), arrayOf( // version 27
                arrayOf(3517, 2132, 1465, 902),
                arrayOf(2701, 1637, 1125, 692),
                arrayOf(1933, 1172, 805, 496),
                arrayOf(1501, 910, 625, 385)
            ), arrayOf( // version 28
                arrayOf(3669, 2223, 1528, 940),
                arrayOf(2857, 1732, 1190, 732),
                arrayOf(2085, 1263, 868, 534),
                arrayOf(1581, 958, 658, 405)
            ), arrayOf( // version 29
                arrayOf(3909, 2369, 1628, 1002),
                arrayOf(3035, 1839, 1264, 778),
                arrayOf(2181, 1322, 908, 559),
                arrayOf(1677, 1016, 698, 430)
            ), arrayOf( // version 30
                arrayOf(4158, 2520, 1732, 1066),
                arrayOf(3289, 1994, 1370, 843),
                arrayOf(2358, 1429, 982, 604),
                arrayOf(1782, 1080, 742, 457)
            ), arrayOf( // version 31
                arrayOf(4417, 2677, 1840, 1132),
                arrayOf(3486, 2113, 1452, 894),
                arrayOf(2473, 1499, 1030, 634),
                arrayOf(1897, 1150, 790, 486)
            ), arrayOf( // version 32
                arrayOf(4686, 2840, 1952, 1201),
                arrayOf(3693, 2238, 1538, 947),
                arrayOf(2670, 1618, 1112, 684),
                arrayOf(2022, 1226, 842, 518)
            ), arrayOf( // version 33
                arrayOf(4965, 3009, 2068, 1273),
                arrayOf(3909, 2369, 1628, 1002),
                arrayOf(2805, 1700, 1168, 719),
                arrayOf(2157, 1307, 898, 553)
            ), arrayOf( // version 34
                arrayOf(5253, 3183, 2188, 1347),
                arrayOf(4134, 2506, 1722, 1060),
                arrayOf(2949, 1787, 1228, 756),
                arrayOf(2301, 1394, 958, 590)
            ), arrayOf( // version 35
                arrayOf(5529, 3351, 2303, 1417),
                arrayOf(4343, 2632, 1809, 1113),
                arrayOf(3081, 1867, 1283, 790),
                arrayOf(2361, 1431, 983, 605)
            ), arrayOf( // version 36
                arrayOf(5836, 3537, 2431, 1496),
                arrayOf(4588, 2780, 1911, 1176),
                arrayOf(3244, 1966, 1351, 832),
                arrayOf(2524, 1530, 1051, 647)
            ), arrayOf( // version 37
                arrayOf(6153, 3729, 2563, 1577),
                arrayOf(4775, 2894, 1989, 1224),
                arrayOf(3417, 2071, 1423, 876),
                arrayOf(2625, 1591, 1093, 673)
            ), arrayOf( // version 38
                arrayOf(6479, 3927, 2699, 1661),
                arrayOf(5039, 3054, 2099, 1292),
                arrayOf(3599, 2181, 1499, 923),
                arrayOf(2735, 1658, 1139, 701)
            ), arrayOf( // version 39
                arrayOf(6743, 4087, 2809, 1729),
                arrayOf(5313, 3220, 2213, 1362),
                arrayOf(3791, 2298, 1579, 972),
                arrayOf(2927, 1774, 1219, 750)
            ), arrayOf( // version 40
                arrayOf(7089, 4296, 2953, 1817),
                arrayOf(5596, 3391, 2331, 1435),
                arrayOf(3993, 2420, 1663, 1024),
                arrayOf(3057, 1852, 1273, 784)
            )
        )
    }

    enum class ErrorCorrection {
        LOW, MEDIUM, QUARTILE, HIGH
    }
}