package com.appecoviaje.data

import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {
    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()

    suspend fun insert(trip: Trip) {
        tripDao.insert(trip)
    }

    suspend fun update(trip: Trip) {
        tripDao.update(trip)
    }
}
