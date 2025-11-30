package com.appecoviaje.data

import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDao: ReservationDao) {
    fun getReservationsForUser(userId: Int): Flow<List<Reservation>> {
        return reservationDao.getReservationsForUser(userId)
    }

    suspend fun insert(reservation: Reservation) {
        reservationDao.insert(reservation)
    }

    suspend fun delete(reservationId: Int) {
        reservationDao.delete(reservationId)
    }
}
