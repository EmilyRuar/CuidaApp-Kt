
package com.example.cuidaplus.data.remote

import com.example.cuidaplus.data.model.Patient
import retrofit2.http.*

interface PatientApiService {

    @GET("/api/patients")
    suspend fun getPatients(): List<Patient>

    @POST("/Patients")
    suspend fun createPatient(@Body patient: PatientRequest): PatientResponse

}
