package com.appecoviaje.data

import com.appecoviaje.network.WeatherApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    private val apiService: WeatherApiService = mockk()
    private val repository = WeatherRepository(apiService)

    @Test
    fun `getWeather returns data on success`() = runTest {
        // Given
        val weatherResponse = WeatherResponse(CurrentWeather(25.0, 1))
        coEvery { apiService.getWeather(any(), any()) } returns Response.success(weatherResponse)

        // When
        val result = repository.getWeather(0.0, 0.0)

        // Then
        assertEquals(25.0, result?.temperature)
    }

    @Test
    fun `getWeather returns null on failure`() = runTest {
        // Given
        coEvery { apiService.getWeather(any(), any()) } returns Response.error(404, okhttp3.ResponseBody.create(null, ""))

        // When
        val result = repository.getWeather(0.0, 0.0)

        // Then
        assertNull(result)
    }
}
