package com.appecoviaje.data

import com.appecoviaje.network.ApiService
import com.appecoviaje.network.LoginRequest
import com.appecoviaje.network.RegisterRequest

class UserRepository(private val userDao: UserDao, private val apiService: ApiService) {
    suspend fun createUser(user: User) {
        // Local save
        userDao.insert(user)
    }

    suspend fun login(username: String, passwordHash: String): Boolean {
        return try {
            // Supabase requires email, we'll assume username is email for now or append a fake domain if needed
            // For this migration, let's assume the user enters an email.
            val response = apiService.login(LoginRequest(email = username, password = passwordHash))
            if (response.isSuccessful && response.body() != null) {
                val authData = response.body()!!
                // Save token if needed
                true
            } else {
                android.util.Log.e("UserRepository", "Login failed: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun register(username: String, passwordHash: String): Boolean {
        return try {
            val response = apiService.register(RegisterRequest(email = username, password = passwordHash))
            if (!response.isSuccessful) {
                android.util.Log.e("UserRepository", "Register failed: ${response.errorBody()?.string()}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
