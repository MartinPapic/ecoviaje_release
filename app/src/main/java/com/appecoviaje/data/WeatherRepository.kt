package com.appecoviaje.data

import com.appecoviaje.network.WeatherApiService

class WeatherRepository(private val weatherApiService: WeatherApiService) {
    suspend fun getWeather(lat: Double, lon: Double): CurrentWeather? {
        return try {
            val response = weatherApiService.getWeather(lat, lon)
            if (response.isSuccessful) {
                response.body()?.currentWeather
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
