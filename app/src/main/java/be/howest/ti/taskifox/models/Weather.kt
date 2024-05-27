package be.howest.ti.taskifox.models

import com.squareup.moshi.Json

data class WeatherResponse (
    val list: List<WeatherItem>
)

data class WeatherItem (
    @Json(name = "dt_txt") val dateTimeString: String,
    val weather: List<Weather>
)

data class Weather (
    val description: String,
    var icon: String // url needs to be wrapped around it in repo.
)
