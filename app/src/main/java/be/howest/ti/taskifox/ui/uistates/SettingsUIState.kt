package be.howest.ti.taskifox.ui.uistates

data class SettingsUIState(
    val apiKey: String = "",
    val showWeatherData: Boolean = false,
    val city: String = "",
    val countryCode: String = ""
)