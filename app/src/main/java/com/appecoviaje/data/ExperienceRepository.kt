package com.appecoviaje.data

import kotlinx.coroutines.flow.Flow

class ExperienceRepository(private val experienceDao: ExperienceDao) {
    fun getExperiencesForTrip(tripId: Int): Flow<List<Experience>> {
        return experienceDao.getExperiencesForTrip(tripId)
    }

    suspend fun insert(experience: Experience) {
        experienceDao.insert(experience)
    }

    suspend fun delete(experienceId: Int) {
        experienceDao.delete(experienceId)
    }
}
