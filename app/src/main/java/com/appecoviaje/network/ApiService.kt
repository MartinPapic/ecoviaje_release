package com.appecoviaje.network

import com.appecoviaje.data.Reservation
import com.appecoviaje.data.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("rest/v1/trips?select=*")
    suspend fun getTrips(): List<Trip>

    @POST("rest/v1/reservations")
    suspend fun createReservation(@Body reservation: Reservation): Response<Unit>

    @POST("auth/v1/token?grant_type=password")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/v1/signup")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>
}

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)
data class AuthResponse(val access_token: String, val user: UserDto?)
data class UserDto(val id: String, val email: String?)
