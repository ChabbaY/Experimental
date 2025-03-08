package cloud.englert.experimental.api

import retrofit2.http.GET

interface JokeApi {
    @GET("random_joke")
    suspend fun getJoke(): JokeResponse
}