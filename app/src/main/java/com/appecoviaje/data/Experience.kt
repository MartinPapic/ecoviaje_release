package com.appecoviaje.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experiences")
data class Experience(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val userId: Int,
    val username: String, // Added username field
    val rating: Int, // Changed to Int
    val comment: String,
    val photoUri: String?
)
