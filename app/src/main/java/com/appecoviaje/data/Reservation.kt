package com.appecoviaje.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val userId: Int,
    val reservationDate: Long, // Date when the reservation was made
    val eventDate: Long,       // New: Date selected from calendar for the event
    val adventureType: String  // New: Type of adventure (e.g., "Alojamiento", "Tour")
)
