package cloud.englert.experimental.api

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://official-joke-api.appspot.com/"
object JokeRetrofitInstance {
    private val client = OkHttpClient.Builder().build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: JokeApi by lazy {
        retrofit.create(JokeApi::class.java)
    }
}