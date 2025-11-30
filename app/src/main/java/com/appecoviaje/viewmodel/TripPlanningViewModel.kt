package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TripPlanningUiState(
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val weatherData: Map<Int, String> = emptyMap() // TripId -> Temperature String
)

class TripPlanningViewModel(
    private val tripRepository: TripRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripPlanningUiState())
    val uiState: StateFlow<TripPlanningUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private var allTrips: List<Trip> = emptyList()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Assuming getAllTrips is a suspend function or flow. Based on previous code it seemed to be a Flow in one version and suspend in another.
            // Let's assume it returns a List directly or we collect it.
            // Checking TripRepository usage in previous steps... it seemed to return a Flow in the broken file but List in my plan.
            // Let's look at TripRepository to be sure, but for now I'll assume it returns a List for simplicity in this overwrite, 
            // or I'll use the flow pattern if that was established.
            // Actually, looking at the broken file, it had `tripRepository.getAllTrips().onEach...`.
            // So it returns a Flow.
            
            tripRepository.getAllTrips().collect { trips ->
                allTrips = trips
                filterTrips()
                fetchWeatherForTrips(trips)
            }
        }
    }

    private fun fetchWeatherForTrips(trips: List<Trip>) {
        viewModelScope.launch {
            val weatherMap = _uiState.value.weatherData.toMutableMap()
            trips.forEach { trip ->
                if (!weatherMap.containsKey(trip.id)) {
                    val weather = weatherRepository.getWeather(trip.latitude, trip.longitude)
                    if (weather != null) {
                        weatherMap[trip.id] = "${weather.temperature}°C"
                    }
                }
            }
            _uiState.update { it.copy(weatherData = weatherMap) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterTrips()
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
        filterTrips()
    }

    private fun filterTrips() {
        val query = _searchQuery.value
        val category = _selectedCategory.value

        val filteredTrips = allTrips.filter { trip ->
            val matchesSearch = trip.title.contains(query, ignoreCase = true) ||
                                trip.description.contains(query, ignoreCase = true)
            val matchesCategory = category == null || trip.category == category
            matchesSearch && matchesCategory
        }
        _uiState.update { it.copy(trips = filteredTrips, isLoading = false) }
    }

    fun toggleFavorite(trip: Trip) {
        viewModelScope.launch {
            tripRepository.update(trip.copy(isFavorite = !trip.isFavorite))
        }
    }
}
