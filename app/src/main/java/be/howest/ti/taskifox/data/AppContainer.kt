package be.howest.ti.taskifox.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import be.howest.ti.taskifox.data.repositories.OpenWeatherMapWeatherRepository
import be.howest.ti.taskifox.data.repositories.ProjectsRepository
import be.howest.ti.taskifox.data.repositories.RoomProjectsRepository
import be.howest.ti.taskifox.data.repositories.RoomTodosRepository
import be.howest.ti.taskifox.data.repositories.SettingsRepository
import be.howest.ti.taskifox.data.repositories.SharedPreferencesSettingsRepository
import be.howest.ti.taskifox.data.repositories.TodosRepository
import be.howest.ti.taskifox.data.repositories.WeatherRepository
import be.howest.ti.taskifox.data.sources.network.OpenWeatherMapApiService
import be.howest.ti.taskifox.data.sources.room.TaskiFoxDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface AppContainer {
    val todosRepository: TodosRepository
    val projectsRepository: ProjectsRepository
    val settingsRepository: SettingsRepository
    val weatherRepository: WeatherRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "encrypted_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("regular_prefs", Context.MODE_PRIVATE)
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val weatherApiService: OpenWeatherMapApiService by lazy {
        retrofit.create(OpenWeatherMapApiService::class.java)
    }

    override val weatherRepository: WeatherRepository by lazy {
        OpenWeatherMapWeatherRepository(weatherApiService)
    }

    override val settingsRepository by lazy {
        SharedPreferencesSettingsRepository(encryptedSharedPreferences, sharedPreferences)
    }

    override val todosRepository: TodosRepository by lazy {
        RoomTodosRepository(TaskiFoxDatabase.getDatabase(context).todoDao())
    }

    override val projectsRepository: ProjectsRepository by lazy {
        RoomProjectsRepository(TaskiFoxDatabase.getDatabase(context).projectDao())
    }
}
