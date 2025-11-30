package com.appecoviaje.data

import com.appecoviaje.network.ApiService
import com.appecoviaje.network.LoginRequest
import com.appecoviaje.network.RegisterRequest

class UserRepository(private val userDao: UserDao, private val apiService: ApiService) {
    suspend fun createUser(user: User) {
        // Local save
        userDao.insert(user)
    }

    suspend fun register(username: String, passwordHash: String): Boolean {
        return try {
            val response = apiService.register(RegisterRequest(username, passwordHash))
            if (response.isSuccessful && response.body() != null) {
                // Save locally on success
                userDao.insert(User(username = username, passwordHash = passwordHash))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(username: String, passwordHash: String): Boolean {
        return try {
            val response = apiService.login(LoginRequest(username, passwordHash))
            response.isSuccessful && response.body() != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
