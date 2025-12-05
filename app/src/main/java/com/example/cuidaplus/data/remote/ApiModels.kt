
package com.example.cuidaplus.data.remote

import com.google.gson.annotations.SerializedName

// ============ AUTH MODELS ============

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val id: String,
    val email: String,
    val name: String,

)

// ============ PATIENT MODELS ============

data class PatientRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val weight: Double?,
    val height: Double?,
    val bloodType: String?,
    val imageUrl: String? = null
)

data class PatientResponse(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val weight: Double?,
    val height: Double?,
    val bloodType: String?,
    val imageUrl: String?,
    val userId: String, // ID del médico o dueño del registro
    val createdAt: String,
    val updatedAt: String
)

// ============ ERROR RESPONSE ============

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val validationErrors: Map<String, String>? = null
)
