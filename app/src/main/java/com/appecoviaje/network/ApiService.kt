package com.appecoviaje.network

import com.appecoviaje.data.Reservation
import com.appecoviaje.data.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("trips")
    suspend fun getTrips(): List<Trip>

    @POST("reservations")
    suspend fun createReservation(@Body reservation: Reservation): Response<Unit>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>
}

data class LoginRequest(val username: String, val passwordHash: String)
data class RegisterRequest(val username: String, val passwordHash: String)
data class AuthResponse(val token: String, val userId: Int, val username: String)
