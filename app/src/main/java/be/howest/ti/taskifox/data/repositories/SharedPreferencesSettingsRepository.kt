package be.howest.ti.taskifox.data.repositories

import android.content.SharedPreferences

class SharedPreferencesSettingsRepository(
    private val encryptedSharedPreferences: SharedPreferences,
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {
    override fun saveWeatherApiKey(apiKey: String) {
        encryptedSharedPreferences.edit().putString(WEATHER_API_KEY_KEY, apiKey).apply()
    }
    override fun getWeatherApiKey(): String? {
        return encryptedSharedPreferences.getString(WEATHER_API_KEY_KEY, "")
    }

    override fun saveShowWeatherData(setting: Boolean) {
        sharedPreferences.edit().putBoolean(SHOW_WEATHER_DATA_KEY, setting).apply()
    }

    override fun getShowWeatherData(): Boolean {
        return sharedPreferences.getBoolean(SHOW_WEATHER_DATA_KEY, false)
    }

    override fun saveWeatherCity(city: String) {
        sharedPreferences.edit().putString(WEATHER_CITY_KEY, city).apply()
    }

    override fun getWeatherCity(): String? {
        return sharedPreferences.getString(WEATHER_CITY_KEY, "")
    }

    override fun saveWeatherCountryCode(countryCode: String) {
        sharedPreferences.edit().putString(WEATHER_COUNTRY_CODE_KEY, countryCode).apply()
    }

    override fun getWeatherCountryCode(): String? {
        return sharedPreferences.getString(WEATHER_COUNTRY_CODE_KEY, "")
    }

    companion object {
        private const val WEATHER_API_KEY_KEY = "weather_api_key"
        private const val SHOW_WEATHER_DATA_KEY = "show_weather_data"
        private const val WEATHER_CITY_KEY = "weather_city"
        private const val WEATHER_COUNTRY_CODE_KEY = "weather_country_code"
    }
}