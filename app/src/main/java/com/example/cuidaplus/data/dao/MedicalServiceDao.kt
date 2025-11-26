
package com.example.cuidaplus.data.dao

import androidx.room.*
import com.example.cuidaplus.data.model.MedicalService
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalServiceDao {

    // Obtener todos los servicios médicos disponibles ordenados por categoría
    @Query("SELECT * FROM medical_services WHERE isAvailable = 1 ORDER BY category ASC")
    fun getAllServices(): Flow<List<MedicalService>>

    // Obtener un servicio específico por su ID
    @Query("SELECT * FROM medical_services WHERE id = :serviceId")
    suspend fun getServiceById(serviceId: String): MedicalService?

    // Obtener servicios por categoría (ej: cardiología, pediatría) que estén disponibles
    @Query("SELECT * FROM medical_services WHERE category = :category AND isAvailable = 1")
    fun getServicesByCategory(category: String): Flow<List<MedicalService>>

    // Insertar un nuevo servicio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: MedicalService)

    // Insertar varios servicios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<MedicalService>)

    // Actualizar un servicio existente
    @Update
    suspend fun updateService(service: MedicalService)

    // Eliminar un servicio específico
    @Delete
    suspend fun deleteService(service: MedicalService)
}
