
package com.example.cuidaplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = false)
    val id: String, // ID único de la cita
    val serviceId: String, // ID del servicio médico (ej: consulta general)
    val specialistId: String, // ID del especialista asignado
    val userId: String, // ID del paciente o usuario
    val date: Long, // Fecha en timestamp
    val time: String, // Ej: "10:00 AM"
    val status: String, // Estados: Pendiente, Confirmada, Cancelada, Completada
    val notes: String? = null, // Observaciones opcionales
    val createdAt: Long = System.currentTimeMillis() // Fecha de creación
)
