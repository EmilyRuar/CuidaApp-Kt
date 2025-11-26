
package com.example.cuidaplus.repository

import com.example.cuidaplus.data.dao.AppointmentDao
import com.example.cuidaplus.data.dao.MedicalServiceDao
import com.example.cuidaplus.data.model.Appointment
import com.example.cuidaplus.data.model.MedicalService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ServiceRepository(
    private val serviceDao: MedicalServiceDao,
    private val appointmentDao: AppointmentDao
) {

    // Servicios Médicos
    fun getAllServices(): Flow<List<MedicalService>> {
        return serviceDao.getAllServices()
    }

    suspend fun getServiceById(serviceId: String): MedicalService? {
        return serviceDao.getServiceById(serviceId)
    }

    fun getServicesByCategory(category: String): Flow<List<MedicalService>> {
        return serviceDao.getServicesByCategory(category)
    }

    /**
     * Inicializa servicios médicos de ejemplo en la base local
     */
    suspend fun initializeSampleServices() {
        val sampleServices = listOf(
            MedicalService(
                id = "1",
                name = "Consulta General",
                shortDescription = "Revisión médica completa",
                description = "Consulta médica general que incluye examen físico, revisión de signos vitales y recomendaciones de salud.",
                price = 25000.0,
                duration = 30,
                category = "Consulta"
            ),
            MedicalService(
                id = "2",
                name = "Pediatría",
                shortDescription = "Atención especializada para niños",
                description = "Consulta pediátrica para control de crecimiento, desarrollo y prevención de enfermedades.",
                price = 30000.0,
                duration = 30,
                category = "Especialidad"
            ),
            MedicalService(
                id = "3",
                name = "Cardiología",
                shortDescription = "Evaluación del corazón",
                description = "Consulta especializada en salud cardiovascular, incluye electrocardiograma y diagnóstico.",
                price = 45000.0,
                duration = 45,
                category = "Especialidad"
            ),
            MedicalService(
                id = "4",
                name = "Exámenes de Laboratorio",
                shortDescription = "Análisis clínicos",
                description = "Servicio de laboratorio para análisis de sangre, orina y otros estudios diagnósticos.",
                price = 35000.0,
                duration = 30,
                category = "Diagnóstico"
            ),
            MedicalService(
                id = "5",
                name = "Urgencias 24/7",
                shortDescription = "Atención inmediata",
                description = "Servicio de urgencias médicas disponible las 24 horas para casos críticos.",
                price = 50000.0,
                duration = 60,
                category = "Urgencia"
            )
        )

        sampleServices.forEach { service ->
            serviceDao.insertService(service)
        }
    }

    // Citas Médicas
    fun getAppointmentsByUser(userId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByUser(userId)
    }

    fun getAppointmentsBySpecialist(specialistId: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsBySpecialist(specialistId)
    }

    suspend fun createAppointment(appointment: Appointment): Result<Appointment> {
        return try {
            val newAppointment = appointment.copy(id = UUID.randomUUID().toString())
            appointmentDao.insertAppointment(newAppointment)
            Result.success(newAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAppointment(appointment: Appointment): Result<Appointment> {
        return try {
            appointmentDao.updateAppointment(appointment)
            Result.success(appointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        return try {
            val appointment = appointmentDao.getAppointmentById(appointmentId)
            if (appointment != null) {
                appointmentDao.updateAppointment(appointment.copy(status = "Cancelada"))
                Result.success(Unit)
            } else {
                Result.failure(Exception("Cita no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
