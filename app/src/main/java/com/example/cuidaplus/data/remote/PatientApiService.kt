
package com.example.cuidaplus.data.remote

import retrofit2.http.*

interface PatientApiService {

    @GET("/api/patients")
    suspend fun getPatients(): List<PatientResponse>

    @GET("/api/patients/{id}")
    suspend fun getPatientById(@Path("id") patientId: String): PatientResponse

    @POST("/api/patients")
    suspend fun createPatient(@Body patient: PatientRequest): PatientResponse

    @PUT("/api/patients/{id}")
    suspend fun updatePatient(
        @Path("id") patientId: String,
        @Body patient: PatientRequest
    ): PatientResponse

    @DELETE("/api/patients/{id}")
    suspend fun deletePatient(@Path("id") patientId: String)

    @GET("/api/patients/bloodType/{bloodType}")
    suspend fun getPatientsByBloodType(@Path("bloodType") bloodType: String): List<PatientResponse>
}
