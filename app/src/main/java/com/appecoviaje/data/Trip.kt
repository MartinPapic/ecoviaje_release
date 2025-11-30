package com.appecoviaje.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("image_res_id")
    @DrawableRes val imageResId: Int, // Field for local drawable resource ID
    val category: String, // New category field
    @SerializedName("is_favorite")
    val isFavorite: Boolean = false
)
