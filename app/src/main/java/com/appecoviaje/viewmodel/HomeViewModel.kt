package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val welcomeMessage: String = "Cargando...",
    val featuredTrip: Trip? = null,
    val isLoading: Boolean = true // To handle the loading state
)

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tripRepository: TripRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Load welcome message
        viewModelScope.launch {
            userPreferencesRepository.username.collect { username ->
                _uiState.update {
                    it.copy(welcomeMessage = "Bienvenido, ${username ?: "Invitado"}")
                }
            }
        }

        // Load featured trip safely
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Start loading
            val trips = tripRepository.getAllTrips().first()
            if (trips.isNotEmpty()) {
                _uiState.update {
                    it.copy(featuredTrip = trips.random(), isLoading = false) // Finish loading
                }
            } else {
                _uiState.update { it.copy(isLoading = false) } // Finish loading even if there are no trips
            }
        }
    }

    fun toggleFavorite(trip: Trip) {
        viewModelScope.launch {
            tripRepository.update(trip.copy(isFavorite = !trip.isFavorite))
        }
    }
}
