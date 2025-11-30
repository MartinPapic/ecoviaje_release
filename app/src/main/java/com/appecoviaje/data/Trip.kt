package com.appecoviaje.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    @DrawableRes val imageResId: Int, // Field for local drawable resource ID
    val category: String, // New category field
    val isFavorite: Boolean = false
)
