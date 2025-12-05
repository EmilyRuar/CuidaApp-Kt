package com.example.cuidaplus.data.remote


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users/login")
    fun login(@Body request: LoginRequest): Call<String>
}
