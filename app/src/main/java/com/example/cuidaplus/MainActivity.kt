package com.example.cuidaplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat
import com.example.cuidaplus.navigation.NavGraph
import com.example.cuidaplus.navigation.Screen
import com.example.cuidaplus.ui.theme.CuidaplusTheme
import com.example.cuidaplus.viewmodel.PatientViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CuidaplusTheme {



                // Crear NavController
                val navController = rememberNavController()

                // Llamar a tu NavGraph REAL
                NavGraph(
                    navController = navController,
                    startDestination = Screen.Login.route   // 👈 ARRANCA
                )
            }
        }
    }
}
