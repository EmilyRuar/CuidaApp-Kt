
package com.example.cuidaplus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.cuidaplus.data.model.Patient
import com.example.cuidaplus.repository.PatientRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
        val age = "35"

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

    // ========== Tests de validación ==========
    @Test
    fun `savePatient with empty name shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("")
        viewModel.onAgeChange("35")
        viewModel.onGenderChange("Masculino")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.nameError).isEqualTo("El nombre es obligatorio")
        }
    }

    @Test
    fun `savePatient with invalid age shows error`() = runTest {
        createViewModel()
        viewModel.onNameChange("Juan")
        viewModel.onAgeChange("abc")
        viewModel.onGenderChange("Masculino")

        viewModel.savePatient()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.ageError).isEqualTo("Edad inválida")
        }
    }

    // ========== Tests de guardar paciente ==========
    @Test
    fun `savePatient with valid data succeeds`() = runTest {
        createViewModel()
        val patient = Patient(
            id = "p1",
            name = "Juan Pérez",
            age = 35,
            gender = "Masculino",
            weight = 70.0,
            height = 1.75,
            bloodType = "O+",
            imageUrl = null,
            userId = userId
        )

        viewModel.onNameChange("Juan Pérez")
        viewModel.onAgeChange("35")
        viewModel.onGenderChange("Masculino")

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
        viewModel.onAgeChange("35")
        viewModel.onGenderChange("Masculino")

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
            id = "p1",
            name = "Juan Pérez",
            age = 35,
            gender = "Masculino",
            weight = 70.0,
            height = 1.75,
            bloodType = "O+",
            imageUrl = "https://example.com/image.jpg",
            userId = userId
        )

        viewModel.startEditingPatient(patient)

        viewModel.formState.test {
            val state = awaitItem()
            assertThat(state.name).isEqualTo("Juan Pérez")
            assertThat(state.age).isEqualTo("35")
            assertThat(state.gender).isEqualTo("Masculino")
            assertThat(state.weight).isEqualTo("70.0")
            assertThat(state.height).isEqualTo("1.75")
            assertThat(state.bloodType).isEqualTo("O+")
            assertThat(state.imageUrl).isEqualTo("https://example.com/image.jpg")
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
        viewModel.onAgeChange("35")
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
            Patient("p1", "Juan Pérez", 35, "Masculino", 70.0, 1.75, "O+", null, userId),
            Patient("p2", "Ana López", 28, "Femenino", 60.0, 1.65, "A+", null, userId)
        )

        coEvery { patientRepository.getPatientsByUser(userId) } returns patients
        viewModel = PatientViewModel(patientRepository, userId)
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { patientRepository.deletePatient("p1") } returns Result.success(Unit)
        coEvery { patientRepository.getPatientsByUser(userId) } returns patients.filter { it.id != "p1" }

        viewModel.deletePatient("p1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.patients.test {
            val patientList = awaitItem()
            assertThat(patientList).hasSize(1)
            assertThat(patientList[0].id).isEqualTo("p2")
        }
    }

    @Test
    fun `deletePatient failure shows error message`() = runTest {
        createViewModel()
        coEvery { patientRepository.deletePatient("p1") } returns Result.failure(Exception("Error al eliminar"))

        viewModel.deletePatient("p1")
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
            Patient("p1", "Juan Pérez", 35, "Masculino", 70.0, 1.75, "O+", null, userId)
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
