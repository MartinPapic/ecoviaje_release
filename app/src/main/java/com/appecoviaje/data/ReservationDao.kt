package com.appecoviaje.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Insert
    suspend fun insert(reservation: Reservation)

    @Query("SELECT * FROM reservations WHERE userId = :userId")
    fun getReservationsForUser(userId: Int): Flow<List<Reservation>>

    @Query("DELETE FROM reservations WHERE id = :reservationId")
    suspend fun delete(reservationId: Int)
}
