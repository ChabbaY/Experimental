package cloud.englert.experimental.api

import androidx.annotation.Keep

@Keep
class JokeResponse (val type: String,
                    val setup: String,
                    val punchline: String,
                    val id: Int)