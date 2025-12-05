
package com.example.cuidaplus.repository
import com.example.cuidaplus.data.SessionManager
import android.util.Log
import com.example.cuidaplus.data.remote.AuthApiService
import com.example.cuidaplus.data.remote.LoginRequest
import com.example.cuidaplus.data.remote.RegisterRequest
import com.example.cuidaplus.data.remote.RetrofitClient

data class User(
    val id: String,
    val email: String,
    val name: String
)



class AuthRepository(
    private val sessionManager: SessionManager,
    private val api: AuthApiService // Inyectamos la interfaz
) {

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, password))

            // Guardar sesión en DataStore
            sessionManager.saveUserSession(
                userId = response.id,
                email = response.email,
                name = response.name

            )

            val user = User(response.id, response.email, response.name)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))

            sessionManager.saveUserSession(
                userId = response.id,
                email = response.email,
                name = response.name

            )

            val user = User(response.id, response.email, response.name)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar: ${e.message}"))
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}

