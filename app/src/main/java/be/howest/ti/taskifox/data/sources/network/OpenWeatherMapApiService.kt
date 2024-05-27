package be.howest.ti.taskifox.data.sources.network

import be.howest.ti.taskifox.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApiService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}