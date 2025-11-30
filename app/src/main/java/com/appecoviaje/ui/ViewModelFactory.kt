package com.appecoviaje.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appecoviaje.data.AppDatabase
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.data.TripRepository
import com.appecoviaje.data.UserRepository
import com.appecoviaje.viewmodel.LoginViewModel
import com.appecoviaje.viewmodel.RegistrationViewModel
import com.appecoviaje.data.ExperienceRepository
import com.appecoviaje.data.ReservationRepository
import com.appecoviaje.viewmodel.ExperienceViewModel
import com.appecoviaje.viewmodel.HomeViewModel
import com.appecoviaje.viewmodel.ReservationViewModel
import com.appecoviaje.viewmodel.SettingsViewModel
import com.appecoviaje.viewmodel.TripPlanningViewModel
import java.lang.IllegalArgumentException

// Extensión para acceder al DataStore desde Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val userPreferencesRepository = UserPreferencesRepository(context.dataStore)
        val database = AppDatabase.getDatabase(context)
        val tripRepository = TripRepository(database.tripDao())
        val experienceRepository = ExperienceRepository(database.experienceDao())
        val reservationRepository = ReservationRepository(database.reservationDao())
        val userRepository = UserRepository(database.userDao(), com.appecoviaje.network.RetrofitClient.instance)
        val weatherRepository = com.appecoviaje.data.WeatherRepository(com.appecoviaje.network.RetrofitClient.weatherInstance)

        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(userPreferencesRepository, userRepository) as T
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) ->
                RegistrationViewModel(userPreferencesRepository, userRepository) as T
            modelClass.isAssignableFrom(TripPlanningViewModel::class.java) ->
                TripPlanningViewModel(tripRepository, weatherRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(userPreferencesRepository) as T
            modelClass.isAssignableFrom(ExperienceViewModel::class.java) ->
                ExperienceViewModel(experienceRepository, userPreferencesRepository, tripRepository) as T
            modelClass.isAssignableFrom(ReservationViewModel::class.java) ->
                ReservationViewModel(reservationRepository, userPreferencesRepository, tripRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(userPreferencesRepository, tripRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
