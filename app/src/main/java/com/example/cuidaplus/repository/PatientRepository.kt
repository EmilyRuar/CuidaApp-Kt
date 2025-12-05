
package com.example.cuidaplus.repository
import com.example.cuidaplus.data.dao.PatientDao
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.data.remote.PatientRequest
import com.example.cuidaplus.data.remote.PatientResponse
import com.example.cuidaplus.data.remote.RetrofitClient
import com.example.cuidaplus.data.remote.PatientApiService



class PatientRepository(private val patientDao: PatientDao) {

    private val TAG = "PatientRepository"
    suspend fun getPatient(): List<Patient>{
        return RetrofitClient.api.getPosts()
    }
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
    }}
