package be.howest.ti.taskifox.data

import be.howest.ti.taskifox.data.repositories.OpenWeatherMapWeatherRepository
import be.howest.ti.taskifox.data.sources.network.OpenWeatherMapApiService
import be.howest.ti.taskifox.models.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate

class OpenWeatherMapWeatherRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var weatherMapApiService: OpenWeatherMapApiService
    private lateinit var repository: OpenWeatherMapWeatherRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        val client = OkHttpClient.Builder()
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(moshiConverterFactory)
            .build()

        weatherMapApiService = retrofit.create(OpenWeatherMapApiService::class.java)
        repository = OpenWeatherMapWeatherRepository(weatherMapApiService)
    }

    @After
    fun stopMockServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSuccessfulWeatherForecastFetch() = runBlocking {
        val mockResponse = this::class.java.getResource("/mock_open_weather_map_response.json")
            ?.readText()
            ?: throw IllegalStateException("Mock JSON response file not found")

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val expectedResult = mapOf(
            LocalDate.of(2024, 5, 17) to Weather("light rain", "https://openweathermap.org/img/wn/10d@2x.png"),
            LocalDate.of(2024, 5, 18) to Weather("light rain", "https://openweathermap.org/img/wn/10n@2x.png"),
            LocalDate.of(2024, 5, 19) to Weather("light rain", "https://openweathermap.org/img/wn/10d@2x.png"),
            LocalDate.of(2024, 5, 20) to Weather("clear sky", "https://openweathermap.org/img/wn/01n@2x.png"),
            LocalDate.of(2024, 5, 21) to Weather("clear sky", "https://openweathermap.org/img/wn/01n@2x.png"),
            LocalDate.of(2024, 5, 22) to Weather("light rain", "https://openweathermap.org/img/wn/10n@2x.png")
        )

        val result = repository.getWeatherForecast("brussels", "be",
            "the_key_to_enlightenment")
        if (result.isFailure) {
            result.exceptionOrNull()?.let { throw it }
        }
        val weatherMap = result.getOrNull()

        assert(result.isSuccess)
        assertEquals(expectedResult, weatherMap)
    }

    @Test
    fun `test weather forecast fetch failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("{\"cod\":\"404\"}")
        )

        val result = repository.getWeatherForecast("brussels", "be",
            "the_previous_key_was_lying_this_is_the_true_key_to_enlightenment")

        assert(result.isFailure)
    }
}