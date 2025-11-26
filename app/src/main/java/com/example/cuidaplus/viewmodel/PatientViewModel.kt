
package com.example.cuidaplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.repository.PatientRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PatientFormState(
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val bloodType: String = "",
    val imageUrl: String? = null,

    // Errores de validación
    val nameError: String? = null,
    val ageError: String? = null,
    val genderError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val bloodTypeError: String? = null,

    // Estado general
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class PatientViewModel(
    private val patientRepository: PatientRepository,
    private val userId: String
) : ViewModel() {

    private val _formState = MutableStateFlow(PatientFormState())
    val formState: StateFlow<PatientFormState> = _formState.asStateFlow()

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    private val _editingPatient = MutableStateFlow<Patient?>(null)
    val editingPatient: StateFlow<Patient?> = _editingPatient.asStateFlow()

    private var loadPatientsJob: Job? = null

    init {
        loadPatients()
    }

    private fun loadPatients() {
        loadPatientsJob?.cancel()
        loadPatientsJob = viewModelScope.launch {
            try {
                val patientList = patientRepository.getPatientsByUser(userId)
                _patients.value = patientList
            } catch (e: Exception) {
                _patients.value = emptyList()
            }
        }
    }

    fun refreshPatients() = loadPatients()

    // Actualización de campos
    fun onNameChange(name: String) = _formState.update { it.copy(name = name, nameError = null) }
    fun onAgeChange(age: String) = _formState.update { it.copy(age = age, ageError = null) }
    fun onGenderChange(gender: String) = _formState.update { it.copy(gender = gender, genderError = null) }
    fun onWeightChange(weight: String) = _formState.update { it.copy(weight = weight, weightError = null) }
    fun onHeightChange(height: String) = _formState.update { it.copy(height = height, heightError = null) }
    fun onBloodTypeChange(bloodType: String) = _formState.update { it.copy(bloodType = bloodType, bloodTypeError = null) }
    fun onImageSelected(imageUri: String?) = _formState.update { it.copy(imageUrl = imageUri) }

    fun clearError() = _formState.update { it.copy(errorMessage = null) }

    fun startEditingPatient(patient: Patient) {
        _editingPatient.value = patient
        _formState.update {
            PatientFormState(
                name = patient.name,
                age = patient.age.toString(),
                gender = patient.gender,
                weight = patient.weight?.toString() ?: "",
                height = patient.height?.toString() ?: "",
                bloodType = patient.bloodType ?: "",
                imageUrl = patient.imageUrl
            )
        }
    }

    fun clearForm() {
        _editingPatient.value = null
        _formState.value = PatientFormState()
    }

    fun resetSuccessState() = _formState.update { it.copy(isSuccess = false) }

    fun savePatient() {
        if (!validateForm()) return

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }

            try {
                val patient = Patient(
                    id = _editingPatient.value?.id ?: "",
                    name = _formState.value.name,
                    age = _formState.value.age.toInt(),
                    gender = _formState.value.gender,
                    weight = _formState.value.weight.toDoubleOrNull(),
                    height = _formState.value.height.toDoubleOrNull(),
                    bloodType = _formState.value.bloodType,
                    imageUrl = _formState.value.imageUrl,
                    userId = userId
                )

                val result = if (_editingPatient.value != null) {
                    patientRepository.updatePatient(patient)
                } else {
                    patientRepository.addPatient(patient)
                }

                result.fold(
                    onSuccess = {
                        _formState.update { it.copy(isLoading = false, isSuccess = true, errorMessage = null) }
                        loadPatients()
                    },
                    onFailure = { exception ->
                        _formState.update { it.copy(isLoading = false, errorMessage = exception.message ?: "Error al guardar paciente") }
                    }
                )
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, errorMessage = "Error inesperado: ${e.message}") }
            }
        }
    }

    fun deletePatient(patientId: String) {
        viewModelScope.launch {
            try {
                val result = patientRepository.deletePatient(patientId)
                result.fold(
                    onSuccess = {
                        _patients.value = _patients.value.filter { it.id != patientId }
                        loadPatients()
                    },
                    onFailure = { exception ->
                        _formState.update { it.copy(errorMessage = "Error al eliminar paciente: ${exception.message}") }
                    }
                )
            } catch (e: Exception) {
                _formState.update { it.copy(errorMessage = "Error al eliminar paciente: ${e.message}") }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        val current = _formState.value

        if (current.name.isBlank()) {
            _formState.update { it.copy(nameError = "El nombre es obligatorio") }
            isValid = false
        }
        if (current.age.isBlank() || current.age.toIntOrNull() == null) {
            _formState.update { it.copy(ageError = "Edad inválida") }
            isValid = false
        }
        if (current.gender.isBlank()) {
            _formState.update { it.copy(genderError = "Selecciona un género") }
            isValid = false
        }
        return isValid
    }
}

class PatientViewModelFactory(
    private val patientRepository: PatientRepository,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            return PatientViewModel(patientRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
