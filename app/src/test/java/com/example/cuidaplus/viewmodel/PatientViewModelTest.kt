
package com.example.cuidaplus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.repository.PatientRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PatientViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var patientRepository: PatientRepository
    private lateinit var viewModel: PatientViewModel
    private val userId = "user123"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        patientRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun createViewModel() {
        coEvery { patientRepository.getPatientsByUser(userId) } returns emptyList()
        viewModel = PatientViewModel(patientRepository, userId)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    // ========== Tests de cambio de estado del formulario ==========

    @Test
    fun `onNameChange updates name and clears error`() = runTest {
        createViewModel()
        val name = "Juan Pérez"

        viewModel.onNameChange(name)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.name).isEqualTo(name)
            assertThat(state.nameError).isNull()
        }
    }

    @Test
    fun `onAgeChange updates age and clears error`() = runTest {
        createViewModel()
        val age = "30"

        viewModel.onAgeChange(age)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.age).isEqualTo(age)
            assertThat(state.ageError).isNull()
        }
    }

    @Test
    fun `onGenderChange updates gender and clears error`() = runTest {
        createViewModel()
        val gender = "Masculino"

        viewModel.onGenderChange(gender)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.gender).isEqualTo(gender)
            assertThat(state.genderError).isNull()
        }
    }

    @Test
    fun `onWeightChange updates weight and clears error`() = runTest {
        createViewModel()
        val weight = "70.5"

        viewModel.onWeightChange(weight)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.weight).isEqualTo(weight)
            assertThat(state.weightError).isNull()
        }
    }

    @Test
    fun `onHeightChange updates height and clears error`() = runTest {
        createViewModel()
        val height = "175"

        viewModel.onHeightChange(height)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.height).isEqualTo(height)
            assertThat(state.heightError).isNull()
        }
    }

    @Test
    fun `onBloodTypeChange updates bloodType and clears error`() = runTest {
        createViewModel()
        val bloodType = "O+"

        viewModel.onBloodTypeChange(bloodType)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.bloodType).isEqualTo(bloodType)
            assertThat(state.bloodTypeError).isNull()
        }
    }

    @Test
    fun `onImageSelected updates imageUrl`() = runTest {
        createViewModel()
        val imageUrl = "https://example.com/patient.jpg"

        viewModel.onImageSelected(imageUrl)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.imageUrl).isEqualTo(imageUrl)
        }
    }

// ========== Tests de validación ==========

    @Test
    fun `savePatient with empty name shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.nameError).isEqualTo("El nombre es requerido")
        }
    }

    @Test
    fun `savePatient with invalid age shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("abc")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.ageError).isEqualTo("La edad debe ser un número")
        }
    }

    @Test
    fun `savePatient with age out of range shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("150")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.ageError).isEqualTo("La edad debe estar entre 0 y 120")
        }
    }

    @Test
    fun `savePatient with invalid weight shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("abc")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.weightError).isEqualTo("El peso debe ser un número")
        }
    }

    @Test
    fun `savePatient with weight out of range shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("350")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.weightError).isEqualTo("El peso debe estar entre 0 y 300 kg")
        }
    }

    @Test
    fun `savePatient with invalid height shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("abc")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.heightError).isEqualTo("La altura debe ser un número")
        }
    }

    @Test
    fun `savePatient with height out of range shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("300")
        viewModel.onBloodTypeChange("O+")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.heightError).isEqualTo("La altura debe estar entre 0 y 250 cm")
        }
    }

// ========== Tests de guardar paciente ==========

    @Test
    fun `savePatient with valid data succeeds`() = runTest {
        createViewModel()
        val patient = Patient(
            id = "patient1",
            name = "Juan Pérez",
            age = 30,
            gender = "Masculino",
            weight = 70.0,
            height = 175.0,
            bloodType = "O+",
            imageUrl = null,
            userId = userId
        )

        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        coEvery { patientRepository.addPatient(match { true }) } returns Result.success(patient)

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.isSuccess).isTrue()
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorMessage).isNull()
        }
    }

    @Test
    fun `savePatient failure shows error message`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")
        viewModel.onGenderChange("Masculino")
        viewModel.onWeightChange("70")
        viewModel.onHeightChange("175")
        viewModel.onBloodTypeChange("O+")

        coEvery { patientRepository.addPatient(match { true }) } returns Result.failure(Exception("Error de conexión"))

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.isSuccess).isFalse()
            assertThat(state.errorMessage).contains("Error de conexión")
        }
    }

// ========== Tests de editar paciente ==========

    @Test
    fun `startEditingPatient populates form with patient data`() = runTest {
        createViewModel()
        val patient = Patient(
            id = "patient1",
            name = "Juan Pérez",
            age = 30,
            gender = "Masculino",
            weight = 70.0,
            height = 175.0,
            bloodType = "O+",
            imageUrl = "https://example.com/patient.jpg",
            userId = userId
        )

        viewModel.startEditingPatient(patient)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.name).isEqualTo("Juan Pérez")
            assertThat(state.age).isEqualTo("30")
            assertThat(state.gender).isEqualTo("Masculino")
            assertThat(state.weight).isEqualTo("70.0")
            assertThat(state.height).isEqualTo("175.0")
            assertThat(state.bloodType).isEqualTo("O+")
            assertThat(state.imageUrl).isEqualTo("https://example.com/patient.jpg")
        }

        viewModel.editingPatient.test {
            val editingPatient = awaitItem()
            assertThat(editingPatient).isEqualTo(patient)
        }
    }

    @Test
    fun `clearForm resets form state`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("30")

        viewModel.clearForm()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.name).isEmpty()
            assertThat(state.age).isEmpty()
        }

        viewModel.editingPatient.test {
            val editingPatient = awaitItem()
            assertThat(editingPatient).isNull()
        }
    }

// ========== Tests de eliminar paciente ==========

    @Test
    fun `deletePatient removes patient from list`() = runTest {
        val patients = listOf(
            Patient("patient1", "Juan Pérez", 30, "Masculino", 70.0, 175.0, "O+", null, userId),
            Patient("patient2", "Ana López", 25, "Femenino", 60.0, 165.0, "A+", null, userId)
        )
        coEvery { patientRepository.getPatientsByUser(userId) } returns patients
        viewModel = PatientViewModel(patientRepository, userId)
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { patientRepository.deletePatient("patient1") } returns Result.success(Unit)
        coEvery { patientRepository.getPatientsByUser(userId) } returns patients.filter { it.id != "patient1" }

        viewModel.deletePatient("patient1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.patients.test {
            val patientList = awaitItem()
            assertThat(patientList).hasSize(1)
            assertThat(patientList[0].id).isEqualTo("patient2")
        }
    }

    @Test
    fun `deletePatient failure shows error message`() = runTest {
        createViewModel()
        coEvery { patientRepository.deletePatient("patient1") } returns Result.failure(Exception("Error al eliminar"))

        viewModel.deletePatient("patient1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.errorMessage).contains("Error al eliminar")
        }
    }

// ========== Tests de cargar pacientes ==========

    @Test
    fun `refreshPatients loads patients from repository`() = runTest {
        val patients = listOf(
            Patient("patient1", "Juan Pérez", 30, "Masculino", 70.0, 175.0, "O+", null, userId)
        )
        coEvery { patientRepository.getPatientsByUser(userId) } returns patients
        viewModel = PatientViewModel(patientRepository, userId)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.patients.test {
            val patientList = awaitItem()
            assertThat(patientList).hasSize(1)
            assertThat(patientList[0].name).isEqualTo("Juan Pérez")
        }
    }

// ========== Tests de estado ==========

    @Test
    fun `resetSuccessState resets isSuccess to false`() = runTest {
        createViewModel()

        viewModel.resetSuccessState()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.isSuccess).isFalse()
        }
    }

    @Test
    fun `clearError clears error message`() = runTest {
        createViewModel()

        viewModel.clearError()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.errorMessage).isNull()
        }
    }
}
