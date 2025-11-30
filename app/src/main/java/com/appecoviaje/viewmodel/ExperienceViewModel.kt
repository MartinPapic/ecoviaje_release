package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Experience
import com.appecoviaje.data.ExperienceRepository
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExperienceViewModel(
    private val experienceRepository: ExperienceRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _selectedTripId = MutableStateFlow<Int?>(null)
    val selectedTripId: StateFlow<Int?> = _selectedTripId

    val experiences: StateFlow<List<Experience>> = selectedTripId
        .flatMapLatest { tripId ->
            if (tripId != null) {
                experienceRepository.getExperiencesForTrip(tripId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val trips: StateFlow<List<Trip>> = tripRepository.getAllTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSelectedTripId(tripId: Int) {
        _selectedTripId.value = tripId
    }

    fun addExperience(comment: String, rating: Int, photoUri: String?) {
        viewModelScope.launch {
            val userId = userPreferencesRepository.userToken.first()?.toIntOrNull()
            val username = userPreferencesRepository.username.first()
            val tripId = selectedTripId.value
            if (userId != null && tripId != null && username != null) {
                val newExperience = Experience(
                    tripId = tripId,
                    userId = userId,
                    username = username,
                    rating = rating,
                    comment = comment,
                    photoUri = photoUri
                )
                experienceRepository.insert(newExperience)
            }
        }
    }

    fun deleteExperience(experienceId: Int) {
        viewModelScope.launch {
            experienceRepository.delete(experienceId)
        }
    }
}
