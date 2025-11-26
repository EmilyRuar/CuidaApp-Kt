
package com.example.cuidaplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = false)
    val id: String, // ID único del paciente
    val name: String, // Nombre del paciente
    val age: Int, // Edad en años
    val gender: String, // Masculino, Femenino, Otro
    val weight: Double?, // Peso en kg (opcional)
    val height: Double?, // Altura en cm (opcional)
    val bloodType: String?, // Tipo de sangre (opcional)
    val imageUrl: String? = null, // Foto del paciente (opcional)
    val userId: String // ID del usuario dueño del registro (ej: médico)
)
