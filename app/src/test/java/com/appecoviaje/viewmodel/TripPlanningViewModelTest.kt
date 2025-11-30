package com.appecoviaje.viewmodel

import com.appecoviaje.data.CurrentWeather
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TripPlanningViewModelTest {

    private val tripRepository: TripRepository = mockk()
    private val weatherRepository: WeatherRepository = mockk()
    private lateinit var viewModel: TripPlanningViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTrips loads trips and fetches weather`() = runTest {
        // Given
        val trips = listOf(
            Trip(1, "Trip 1", "Desc 1", "Loc 1", 0.0, 0.0, 0, "Montaña", false),
            Trip(2, "Trip 2", "Desc 2", "Loc 2", 0.0, 0.0, 0, "Playa", false)
        )
        coEvery { tripRepository.getAllTrips() } returns flowOf(trips)
        coEvery { weatherRepository.getWeather(any(), any()) } returns CurrentWeather(20.0, 1)

        // When
        viewModel = TripPlanningViewModel(tripRepository, weatherRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(trips, viewModel.uiState.value.trips)
        assertEquals("20.0°C", viewModel.uiState.value.weatherData[1])
        assertEquals("20.0°C", viewModel.uiState.value.weatherData[2])
    }

    @Test
    fun `searchQuery filters trips`() = runTest {
        // Given
        val trips = listOf(
            Trip(1, "Alpha", "Desc 1", "Loc 1", 0.0, 0.0, 0, "Montaña", false),
            Trip(2, "Beta", "Desc 2", "Loc 2", 0.0, 0.0, 0, "Playa", false)
        )
        coEvery { tripRepository.getAllTrips() } returns flowOf(trips)
        coEvery { weatherRepository.getWeather(any(), any()) } returns null

        viewModel = TripPlanningViewModel(tripRepository, weatherRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSearchQueryChange("Alpha")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.uiState.value.trips.size)
        assertEquals("Alpha", viewModel.uiState.value.trips[0].title)
    }

    @Test
    fun `category selection filters trips`() = runTest {
        // Given
        val trips = listOf(
            Trip(1, "Trip 1", "Desc 1", "Loc 1", 0.0, 0.0, 0, "Montaña", false),
            Trip(2, "Trip 2", "Desc 2", "Loc 2", 0.0, 0.0, 0, "Playa", false)
        )
        coEvery { tripRepository.getAllTrips() } returns flowOf(trips)
        coEvery { weatherRepository.getWeather(any(), any()) } returns null

        viewModel = TripPlanningViewModel(tripRepository, weatherRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onCategorySelected("Playa")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.uiState.value.trips.size)
        assertEquals("Trip 2", viewModel.uiState.value.trips[0].title)
    }
}
