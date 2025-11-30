package com.appecoviaje.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USERNAME = stringPreferencesKey("username")
    private val DARK_MODE = booleanPreferencesKey("dark_mode")

    val userToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_TOKEN]
        }

    val username: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USERNAME]
        }

    suspend fun saveUserData(token: String, username: String) {
        dataStore.edit { settings ->
            settings[USER_TOKEN] = token
            settings[USERNAME] = username
        }
    }

    val darkMode: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_MODE] ?: false
        }

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.edit { settings ->
            settings[DARK_MODE] = darkMode
        }
    }

    private val LAST_SELECTED_TRIP_ID = androidx.datastore.preferences.core.intPreferencesKey("last_selected_trip_id")

    val lastSelectedTripId: Flow<Int?> = dataStore.data
        .map { preferences ->
            preferences[LAST_SELECTED_TRIP_ID]
        }

    suspend fun setLastSelectedTripId(tripId: Int) {
        dataStore.edit { settings ->
            settings[LAST_SELECTED_TRIP_ID] = tripId
        }
    }
}
