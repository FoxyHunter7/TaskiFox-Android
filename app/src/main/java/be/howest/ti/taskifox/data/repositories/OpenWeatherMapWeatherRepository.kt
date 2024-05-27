package be.howest.ti.taskifox.data.repositories

import be.howest.ti.taskifox.data.sources.network.OpenWeatherMapApiService
import be.howest.ti.taskifox.models.Weather
import be.howest.ti.taskifox.models.WeatherResponse
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate

class OpenWeatherMapWeatherRepository(
    private val weatherApiService: OpenWeatherMapApiService
) : WeatherRepository {
    override suspend fun getWeatherForecast(
        city: String,
        countryCode: String,
        apiKey: String
    ): Result<Map<LocalDate, Weather>> {
        return try {
            val weatherResponse = weatherApiService.getWeatherForecast(
                location = "$city,$countryCode",
                apiKey = apiKey
            )

            Result.success(weatherResponse.parseToWeatherMap().wrapUrlAroundIcons())
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }

    private fun WeatherResponse.parseToWeatherMap(): Map<LocalDate, Weather> {
        return this.groupWeatherByDate().mapValues {
                (_, weatherList) -> weatherList.getMostFrequentWeather()
            ?: throw IllegalArgumentException("Found date with no weather")
        }
    }

    private fun WeatherResponse.groupWeatherByDate(): Map<LocalDate, List<Weather>> {
        val weatherMap = mutableMapOf<LocalDate, MutableList<Weather>>()

        this.list.forEach { weatherItem ->
            val date = LocalDate.parse(weatherItem.dateTimeString.split(" ")[0])
            val weather = weatherItem.weather.first()

            val weatherList = weatherMap.getOrPut(date) { mutableListOf() }
            weatherList.add(weather)
        }

        return weatherMap
    }

    private fun List<Weather>.getMostFrequentWeather(): Weather? {
        return this.groupBy { it }.maxByOrNull { it.value.size }?.key
    }

    private fun Map<LocalDate, Weather>.wrapUrlAroundIcons(): Map<LocalDate, Weather> {
        this.values.forEach { weather -> weather.icon = "https://openweathermap.org/img/wn/${weather.icon}@2x.png" }
        return this
    }
}