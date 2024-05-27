package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.models.Weather
import java.time.LocalDate

interface WeatherRepository {
    suspend fun getWeatherForecast(city: String, countryCode: String, apiKey: String): Result<Map<LocalDate, Weather>>
}