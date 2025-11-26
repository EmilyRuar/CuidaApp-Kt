
package com.example.cuidaplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cuidaplus.data.model.Appointment
import com.example.cuidaplus.data.model.MedicalService
import com.example.cuidaplus.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppointmentFormState(
    val selectedPatientId: String = "",
    val selectedDate: Long = 0L,
    val selectedTime: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ServiceViewModel(
    private val serviceRepository: ServiceRepository,
    private val userId: String
) : ViewModel() {

    private val _services = MutableStateFlow<List<MedicalService>>(emptyList())
    val services: StateFlow<List<MedicalService>> = _services.asStateFlow()

    private val _selectedService = MutableStateFlow<MedicalService?>(null)
    val selectedService: StateFlow<MedicalService?> = _selectedService.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _appointmentFormState = MutableStateFlow(AppointmentFormState())
    val appointmentFormState: StateFlow<AppointmentFormState> = _appointmentFormState.asStateFlow()

    init {
        loadServices()
        loadAppointments()
        initializeSampleData()
    }

    private fun initializeSampleData() {
        viewModelScope.launch {
            try {
                serviceRepository.initializeSampleServices()
            } catch (_: Exception) {
                // Servicios ya inicializados
            }
        }
    }

    private fun loadServices() {
        viewModelScope.launch {
            serviceRepository.getAllServices().collect { serviceList ->
                _services.value = serviceList
            }
        }
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            serviceRepository.getAppointmentsByUser(userId).collect { appointmentList ->
                _appointments.value = appointmentList
            }
        }
    }

    fun selectService(serviceId: String) {
        viewModelScope.launch {
            val service = serviceRepository.getServiceById(serviceId)
            _selectedService.value = service
        }
    }

    fun clearSelectedService() {
        _selectedService.value = null
    }

    fun onPatientSelected(patientId: String) {
        _appointmentFormState.update { it.copy(selectedPatientId = patientId) }
    }

    fun onDateSelected(date: Long) {
        _appointmentFormState.update { it.copy(selectedDate = date) }
    }

    fun onTimeSelected(time: String) {
        _appointmentFormState.update { it.copy(selectedTime = time) }
    }

    fun onNotesChange(notes: String) {
        _appointmentFormState.update { it.copy(notes = notes) }
    }

    fun clearAppointmentForm() {
        _appointmentFormState.value = AppointmentFormState()
    }

    fun resetSuccessState() {
        _appointmentFormState.update { it.copy(isSuccess = false) }
    }

    fun createAppointment(serviceId: String) {
        val state = _appointmentFormState.value

        if (state.selectedPatientId.isEmpty()) {
            _appointmentFormState.update { it.copy(errorMessage = "Debes seleccionar un paciente") }
            return
        }
        if (state.selectedDate == 0L) {
            _appointmentFormState.update { it.copy(errorMessage = "Debes seleccionar una fecha") }
            return
        }
        if (state.selectedTime.isEmpty()) {
            _appointmentFormState.update { it.copy(errorMessage = "Debes seleccionar una hora") }
            return
        }

        viewModelScope.launch {
            _appointmentFormState.update { it.copy(isLoading = true) }
            try {
                val appointment = Appointment(
                    id = "",
                    serviceId = serviceId,
                    specialistId = "", // Puedes agregar lógica para asignar especialista
                    userId = userId,
                    date = state.selectedDate,
                    time = state.selectedTime,
                    status = "Pendiente",
                    notes = state.notes.ifEmpty { null }
                )

                val result = serviceRepository.createAppointment(appointment)
                result.fold(
                    onSuccess = {
                        _appointmentFormState.update {
                            it.copy(isLoading = false, isSuccess = true, errorMessage = null)
                        }
                    },
                    onFailure = { exception ->
                        _appointmentFormState.update {
                            it.copy(isLoading = false, errorMessage = exception.message ?: "Error al crear la cita")
                        }
                    }
                )
            } catch (e: Exception) {
                _appointmentFormState.update {
                    it.copy(isLoading = false, errorMessage = "Error inesperado: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _appointmentFormState.update { it.copy(errorMessage = null) }
    }
}

class ServiceViewModelFactory(
    private val serviceRepository: ServiceRepository,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceViewModel::class.java)) {
            return ServiceViewModel(serviceRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
