package be.howest.ti.taskifox.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.howest.ti.taskifox.data.repositories.SettingsRepository
import be.howest.ti.taskifox.data.repositories.TodosRepository
import be.howest.ti.taskifox.data.repositories.WeatherRepository
import be.howest.ti.taskifox.models.ITodo
import be.howest.ti.taskifox.models.TodoWithProjectColor
import be.howest.ti.taskifox.models.Weather
import be.howest.ti.taskifox.ui.uistates.TodosUIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodosViewModel(
    private val todosRepository: TodosRepository,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _todosUIState = MutableStateFlow(TodosUIState())
    private val _toastMessages = MutableSharedFlow<String>()
    val toastMessages = _toastMessages.asSharedFlow()

    var weatherByDate: Map<LocalDate, Weather> = mapOf()
        private set

    val todosUIState: StateFlow<TodosUIState> =
        todosRepository.getAllTodosGroupedByDueDateWithProjectColStream().map { TodosUIState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TodosUIState()
            )

    init {
        viewModelScope.launch {
            weatherByDate = getWeather()
        }
    }

    private suspend fun getTodos(): Map<LocalDate, List<TodoWithProjectColor>> {
        return todosRepository.getAllTodosGroupedByDueDateWithProjectColStream().first()
    }

    private suspend fun getWeather(): Map<LocalDate, Weather> {
        return if (settingsRepository.getShowWeatherData()) {
            val result = weatherRepository.getWeatherForecast(
                city = settingsRepository.getWeatherCity() ?: "",
                countryCode = settingsRepository.getWeatherCountryCode() ?: "",
                apiKey = settingsRepository.getWeatherApiKey() ?: ""
            )
            result.getOrElse { e ->
                viewModelScope.launch { _toastMessages.emit("Failed to fetch weather information") }
                Log.e("WEATHER FETCH", "Failed to fetch weather information: ${e.message}", e)
                mapOf()
            }
        } else {
            mapOf()
        }
    }

    suspend fun deleteTodo(todo: ITodo) {
        todosRepository.delete(todo)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}