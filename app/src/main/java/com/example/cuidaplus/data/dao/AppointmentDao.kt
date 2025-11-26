package com.example.cuidaplus.data.dao

import androidx.room.*
import com.example.cuidaplus.data.model.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    // Obtener todas las citas de un usuario ordenadas por fecha descendente
    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY date DESC")
    fun getAppointmentsByUser(userId: String): Flow<List<Appointment>>

    // Obtener una cita específica por su ID
    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    suspend fun getAppointmentById(appointmentId: String): Appointment?

    // Obtener citas asociadas a un especialista
    @Query("SELECT * FROM appointments WHERE specialistId = :specialistId ORDER BY date DESC")
    fun getAppointmentsBySpecialist(specialistId: String): Flow<List<Appointment>>

    // Obtener citas por tipo de servicio (ej: consulta general, pediatría)
    @Query("SELECT * FROM appointments WHERE serviceId = :serviceId ORDER BY date DESC")
    fun getAppointmentsByService(serviceId: String): Flow<List<Appointment>>

    // Insertar una nueva cita
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    // Actualizar una cita existente
    @Update
    suspend fun updateAppointment(appointment: Appointment)

    // Eliminar una cita específica
    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    // Eliminar una cita por su ID
    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun deleteAppointmentById(appointmentId: String)
}
