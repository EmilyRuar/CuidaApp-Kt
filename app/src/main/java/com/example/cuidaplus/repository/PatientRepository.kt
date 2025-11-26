
package com.example.cuidaplus.repository

import android.util.Log
import com.example.cuidaplus.data.dao.PatientDao
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.data.remote.PatientRequest
import com.example.cuidaplus.data.remote.PatientResponse
import com.example.cuidaplus.data.remote.RetrofitClient

class PatientRepository(private val patientDao: PatientDao) {

    private val TAG = "PatientRepository"

    /**
     * Convierte PatientResponse de la API a Patient local
     */
    private fun PatientResponse.toPatient(userId: String): Patient {
        return Patient(
            id = this.id,
            name = this.name,
            age = this.age,
            gender = this.gender,
            weight = this.weight,
            height = this.height,
            bloodType = this.bloodType,
            imageUrl = this.imageUrl,
            userId = userId
        )
    }

    /**
     * Convierte Patient local a PatientRequest para la API
     */
    private fun Patient.toPatientRequest(): PatientRequest {
        return PatientRequest(
            name = this.name,
            age = this.age,
            gender = this.gender,
            weight = this.weight,
            height = this.height,
            bloodType = this.bloodType,
            imageUrl = this.imageUrl
        )
    }

    /**
     * Obtener pacientes: SOLO DESDE BACKEND (sin fallback local)
     */
    suspend fun getPatientsByUser(userId: String): List<Patient> {
        return try {
            Log.d(TAG, "Fetching patients from API for user: $userId")

            val remotePatients = RetrofitClient.patientApi.getPatients()
            val patients = remotePatients.map { it.toPatient(userId) }

            Log.d(TAG, "✅ Successfully fetched ${patients.size} patients from API")

            patients
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to fetch patients from API", e)
            throw Exception("No se pudo conectar con el servidor. Verifica tu conexión.", e)
        }
    }

    /**
     * Agregar paciente: SOLO EN BACKEND
     */
    suspend fun addPatient(patient: Patient): Result<Patient> {
        return try {
            Log.d(TAG, "Creating patient in API: ${patient.name}")

            val request = patient.toPatientRequest()
            val response = RetrofitClient.patientApi.createPatient(request)
            val createdPatient = response.toPatient(patient.userId)

            Log.d(TAG, "✅ Patient created successfully in API with id: ${createdPatient.id}")

            Result.success(createdPatient)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to create patient in API", e)
            Result.failure(Exception("No se pudo crear el paciente. Verifica tu conexión.", e))
        }
    }

    /**
     * Actualizar paciente: SOLO EN BACKEND
     */
    suspend fun updatePatient(patient: Patient): Result<Patient> {
        return try {
            Log.d(TAG, "Updating patient in API: ${patient.id}")

            val request = patient.toPatientRequest()
            val response = RetrofitClient.patientApi.updatePatient(patient.id, request)
            val updatedPatient = response.toPatient(patient.userId)

            Log.d(TAG, "✅ Patient updated successfully in API")

            Result.success(updatedPatient)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to update patient in API", e)
            Result.failure(Exception("No se pudo actualizar el paciente. Verifica tu conexión.", e))
        }
    }

    /**
     * Eliminar paciente: SOLO EN BACKEND
     */
    suspend fun deletePatient(patientId: String): Result<Unit> {
        return try {
            Log.d(TAG, "Deleting patient from API: $patientId")

            RetrofitClient.patientApi.deletePatient(patientId)

            Log.d(TAG, "✅ Patient deleted successfully from API")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to delete patient from API", e)
            Result.failure(Exception("No se pudo eliminar el paciente. Verifica tu conexión.", e))
        }
    }

    /**
     * Obtener paciente por ID: SOLO DESDE BACKEND
     */
    suspend fun getPatientById(patientId: String): Patient? {
        return try {
            Log.d(TAG, "Fetching patient by id from API: $patientId")
            val response = RetrofitClient.patientApi.getPatientById(patientId)

            Log.d(TAG, "✅ Patient fetched successfully from API")

            response.toPatient(response.userId)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to fetch patient from API", e)
            null
        }
    }
}
