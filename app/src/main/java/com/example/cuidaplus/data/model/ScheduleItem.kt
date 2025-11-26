
package com.example.cuidaplus.data.model

sealed class ScheduleItem(
    open val id: String,
    open val patientId: String,
    open val patientName: String,
    open val dateTime: Long,
    open val time: String
) {
    // Programación de medicación para el paciente
    data class MedicineSchedule(
        override val id: String,
        override val patientId: String,
        override val patientName: String,
        val medicineName: String,
        val dosage: String,
        override val dateTime: Long,
        override val time: String
    ) : ScheduleItem(id, patientId, patientName, dateTime, time)

    // Programación de cita médica o servicio
    data class ServiceAppointment(
        override val id: String,
        override val patientId: String,
        override val patientName: String,
        val serviceName: String,
        val serviceCategory: String,
        val serviceDescription: String,
        override val dateTime: Long,
        override val time: String,
        val status: String // Pendiente, Confirmada, Cancelada, Completada
    ) : ScheduleItem(id, patientId, patientName, dateTime, time)
}
