
package com.example.cuidaplus.navigation
import com.example.cuidaplus.viewmodel.LoginViewModel
import com.example.cuidaplus.viewmodel.RegisterViewModelFactory
import com.example.cuidaplus.viewmodel.LoginViewModelFactory
import com.example.cuidaplus.viewmodel.RegisterViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cuidaplus.data.SessionManager
import com.example.cuidaplus.data.database.AppDatabase
import com.example.cuidaplus.repository.AuthRepository
import com.example.cuidaplus.repository.PatientRepository
import com.example.cuidaplus.repository.ServiceRepository
import com.example.cuidaplus.ui.home.HomeScreen
import com.example.cuidaplus.ui.login.LoginScreen
import com.example.cuidaplus.ui.register.RegisterScreen
import com.example.cuidaplus.ui.home.HomeScreen
import com.example.cuidaplus.ui.services.ServiceListScreen


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ServiceList : Screen("service_list")

}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val authRepository = AuthRepository(sessionManager)

    // Inicializar base de datos y repositorios
    val database = remember { AppDatabase.getDatabase(context) }

    NavHost(navController = navController, startDestination = startDestination) {

        // Pantalla Login
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(authRepository))
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { /* TODO: Implementar */ },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Registro
        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(authRepository))
            RegisterScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }


// Pantalla Home
        composable(Screen.Home.route) {
            HomeScreen(
                onServiciosClick = {
                    navController.navigate(Screen.ServiceList.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

    }
    }

