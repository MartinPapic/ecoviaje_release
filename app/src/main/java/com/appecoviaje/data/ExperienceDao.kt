package com.appecoviaje.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperienceDao {
    @Insert
    suspend fun insert(experience: Experience)

    @Query("SELECT * FROM experiences WHERE tripId = :tripId")
    fun getExperiencesForTrip(tripId: Int): Flow<List<Experience>>

    @Query("DELETE FROM experiences WHERE id = :experienceId")
    suspend fun delete(experienceId: Int)
}
