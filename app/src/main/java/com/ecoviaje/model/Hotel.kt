package com.ecoviaje.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotels")
data class Hotel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val sustainabilityRating: Float,
    val imageUrl: String
)
