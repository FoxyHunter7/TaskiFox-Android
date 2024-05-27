package be.howest.ti.taskifox.data.repositories

interface SettingsRepository {
    fun saveWeatherApiKey(apiKey: String)
    fun getWeatherApiKey(): String?

    fun saveShowWeatherData(setting: Boolean)
    fun getShowWeatherData(): Boolean

    fun saveWeatherCity(city: String)
    fun getWeatherCity(): String?

    fun saveWeatherCountryCode(countryCode: String)
    fun getWeatherCountryCode(): String?
}