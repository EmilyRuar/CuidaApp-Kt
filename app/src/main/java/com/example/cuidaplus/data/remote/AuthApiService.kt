
package com.example.cuidaplus.data.remote

import com.example.cuidaplus.data.model.Patient
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    fun getPosts(): List<Patient>
}
