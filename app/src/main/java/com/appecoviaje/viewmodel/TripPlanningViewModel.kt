package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class TripPlanningUiState(
    val trips: List<Trip> = emptyList()
)

class TripPlanningViewModel(private val tripRepository: TripRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow(TripPlanningUiState())
    val uiState: StateFlow<TripPlanningUiState> = _uiState.asStateFlow()

    private var allTrips: List<Trip> = emptyList()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        tripRepository.getAllTrips()
            .onEach { trips ->
                allTrips = trips
                filterTrips()
            }
            .launchIn(viewModelScope)
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
        _uiState.value = TripPlanningUiState(trips = filteredTrips)
    }

    fun toggleFavorite(trip: Trip) {
        viewModelScope.launch {
            tripRepository.update(trip.copy(isFavorite = !trip.isFavorite))
        }
    }
}
