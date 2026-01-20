package cloud.englert.experimental.qrgenerator

class EncodingMode {
    companion object {
        private const val REGEX_NUMERIC = "[0-9]*"
        private const val REGEX_ALPHANUMERIC = "[A-Z0-9 ]*"
        private const val REGEX_LATIN1 = "[\u0020-\u007e\u00a0-\u00ff]*"
        private const val REGEX_KANJI = "[\u3400-\u4dbf\u4e00-\u9fc6]*"

        fun of(value: String): Int {
            if (value.matches(REGEX_NUMERIC.toRegex()))
                return Mode.NUMERIC.value
            if (value.matches(REGEX_ALPHANUMERIC.toRegex()))
                return Mode.ALPHANUMERIC.value
            if (value.matches(REGEX_LATIN1.toRegex()))
                return Mode.BYTE.value
            if (value.matches(REGEX_KANJI.toRegex()))
                return Mode.KANJI.value
            return Mode.ECI.value
        }
    }

    enum class Mode(val value: Int) {
        NUMERIC(0b0001),
        ALPHANUMERIC(0b0010),
        BYTE(0b0100),
        KANJI(0b1000),
        ECI(0b1111)
    }
}