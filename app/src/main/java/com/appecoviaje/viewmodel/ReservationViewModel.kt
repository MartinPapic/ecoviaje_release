package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.Reservation
import com.appecoviaje.data.ReservationRepository
import com.appecoviaje.data.Trip
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Data class to hold combined reservation and trip details
data class ReservationDetails(
    val reservationId: Int,
    val tripTitle: String,
    val tripLocation: String,
    val reservationDate: Long,
    val eventDate: Long,
    val adventureType: String
)

class ReservationViewModel(
    private val reservationRepository: ReservationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _reservations = userPreferencesRepository.userToken
        .flatMapLatest { userToken ->
            val userId = userToken?.toIntOrNull() ?: -1
            reservationRepository.getReservationsForUser(userId)
        }

    private val _trips = tripRepository.getAllTrips()

    // Combine reservations and trips to create detailed reservation info
    val reservationDetails: StateFlow<List<ReservationDetails>> = _reservations.combine(_trips) { reservations, trips ->
        reservations.mapNotNull { reservation ->
            trips.find { it.id == reservation.tripId }?.let {
                ReservationDetails(
                    reservationId = reservation.id,
                    tripTitle = it.title,
                    tripLocation = it.location,
                    reservationDate = reservation.reservationDate,
                    eventDate = reservation.eventDate,
                    adventureType = reservation.adventureType
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Expose trips for the dropdown menu
    val trips: StateFlow<List<Trip>> = _trips.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addReservation(tripId: Int, eventDate: Long, adventureType: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val userId = userPreferencesRepository.userToken.first()?.toIntOrNull()
            if (userId != null) {
                val newReservation = Reservation(
                    tripId = tripId,
                    userId = userId,
                    reservationDate = System.currentTimeMillis(),
                    eventDate = eventDate,
                    adventureType = adventureType
                )
                
                // 1. Save to local DB (Source of Truth for UI)
                reservationRepository.insert(newReservation)

                // 2. Sync with Backend (Mock)
                try {
                    val response = com.appecoviaje.network.RetrofitClient.instance.createReservation(newReservation)
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError()
                }
            }
        }
    }

    fun deleteReservation(reservationId: Int) {
        viewModelScope.launch {
            reservationRepository.delete(reservationId)
        }
    }
}
