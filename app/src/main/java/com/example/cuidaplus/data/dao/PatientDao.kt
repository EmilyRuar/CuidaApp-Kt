
package com.example.cuidaplus.data.dao

import androidx.room.*
import com.example.cuidaplus.data.model.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    // Obtener todos los pacientes de un usuario (ej: médico) ordenados por nombre
    @Query("SELECT * FROM patients WHERE userId = :userId ORDER BY name ASC")
    fun getPatientsByUser(userId: String): Flow<List<Patient>>

    // Obtener un paciente específico por su ID
    @Query("SELECT * FROM patients WHERE id = :patientId")
    suspend fun getPatientById(patientId: String): Patient?

    // Insertar un nuevo paciente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    // Actualizar datos de un paciente
    @Update
    suspend fun updatePatient(patient: Patient)

    // Eliminar un paciente específico
    @Delete
    suspend fun deletePatient(patient: Patient)

    // Eliminar un paciente por su ID
    @Query("DELETE FROM patients WHERE id = :patientId")
    suspend fun deletePatientById(patientId: String)

    // Obtener todos los pacientes registrados
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<Patient>>

    // Eliminar todos los pacientes
    @Query("DELETE FROM patients")
    suspend fun deleteAll()
}
