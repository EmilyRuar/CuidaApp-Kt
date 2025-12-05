
package com.example.cuidaplus.viewmodel
import com.example.cuidaplus.repository.ServiceRepository
import com.example.cuidaplus.data.model.MedicalService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.repository.PatientRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.cuidaplus.data.remote.RetrofitClient


class PatientViewModel(
) : ViewModel() {

    // Flujo mutable que contiene la lista de servicios
    private val _patientList = MutableStateFlow<List<MedicalService>>(emptyList())

    // Flujo público de solo lectura
    val serviceList: StateFlow<List<MedicalService>> = _patientList

    // Se llama automáticamente al iniciar


    // Función que obtiene los datos en segundo plano



    }

    private fun PatientRepository.getPosts() {
        TODO("Not yet implemented")
    }

