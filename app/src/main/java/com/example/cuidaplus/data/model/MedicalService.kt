
package com.example.cuidaplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_services")
data class MedicalService(
    @PrimaryKey(autoGenerate = false)
    val id: String, // ID único del servicio
    val name: String, // Nombre del servicio (ej: Consulta General)
    val description: String, // Descripción completa del servicio
    val shortDescription: String, // Descripción breve para mostrar en tarjetas
    val price: Double, // Precio del servicio
    val duration: Int, // Duración en minutos
    val category: String, // Categoría: Consulta, Pediatría, Cardiología, etc.
    val imageUrl: String? = null, // Imagen opcional del servicio
    val isAvailable: Boolean = true // Indica si el servicio está disponible
)
