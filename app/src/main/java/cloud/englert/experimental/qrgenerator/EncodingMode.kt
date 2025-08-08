package cloud.englert.experimental.qrgenerator

import androidx.core.text.isDigitsOnly

class EncodingMode {
    companion object {
        private const val REGEX_ALPHANUMERIC = "[A-Za-z0-9]*"
        private const val REGEX_LATIN1 = "[\u0020-\u007e\u00a0-\u00ff]*"
        private const val REGEX_KANJI = "[\u3400-\u4dbf\u4e00-\u9fc6]*"

        fun of(value: String): Int {
            if (value.isDigitsOnly())
                return Mode.NUMERIC.value
            if (value.matches(Regex.fromLiteral(REGEX_ALPHANUMERIC)))
                return Mode.ALPHANUMERIC.value
            if (value.matches(Regex.fromLiteral(REGEX_LATIN1)))
                return Mode.BYTE.value
            if (value.matches(Regex.fromLiteral(REGEX_KANJI)))
                return Mode.KANJI.value
            return Mode.ECI.value
        }
    }

    private enum class Mode(val value: Int) {
        NUMERIC(0b0001),
        ALPHANUMERIC(0b0010),
        BYTE(0b0100),
        KANJI(0b1000),
        ECI(0b1111)
    }
}