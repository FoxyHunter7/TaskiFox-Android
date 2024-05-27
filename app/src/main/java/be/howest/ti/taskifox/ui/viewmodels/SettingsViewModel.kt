package be.howest.ti.taskifox.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.ti.taskifox.data.repositories.SettingsRepository
import be.howest.ti.taskifox.ui.uistates.SettingsUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _settingsUIState = MutableStateFlow(SettingsUIState())
    val settingsUIState = _settingsUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _settingsUIState.value = SettingsUIState(
                apiKey = settingsRepository.getWeatherApiKey() ?: "",
                showWeatherData = settingsRepository.getShowWeatherData(),
                city = settingsRepository.getWeatherCity() ?: "",
                countryCode = settingsRepository.getWeatherCountryCode() ?: ""
            )
        }
    }

    fun updateShowWeatherInUIState(setting: Boolean) {
        _settingsUIState.value = _settingsUIState.value.copy(
            showWeatherData = setting
        )
    }
    fun updateApiKeyInUIState(apiKey: String) {
        _settingsUIState.value = _settingsUIState.value.copy(
            apiKey = apiKey
        )
    }
    fun updateCityInUIState(city: String) {
        _settingsUIState.value = _settingsUIState.value.copy(
            city = city
        )
    }
    fun updateCountryCodeInUIState(countryCode: String) {
        _settingsUIState.value = _settingsUIState.value.copy(
            countryCode = countryCode
        )
    }

    fun saveSettings() {
        settingsRepository.saveShowWeatherData(settingsUIState.value.showWeatherData)
        settingsRepository.saveWeatherApiKey(settingsUIState.value.apiKey)
        settingsRepository.saveWeatherCity(settingsUIState.value.city)
        settingsRepository.saveWeatherCountryCode(settingsUIState.value.countryCode)
    }
}