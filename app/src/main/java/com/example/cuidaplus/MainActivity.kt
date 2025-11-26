
package com.example.cuidaplus

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cuidaplus.navigation.NavGraph
import com.example.cuidaplus.ui.theme.CuidaplusTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CuidaplusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ✅ Inicializamos NavController
                    val navController = rememberNavController()

                    // ✅ Llamamos al NavGraph con la ruta inicial
                    NavGraph(
                        navController = navController,
                        startDestination = "login"
                    )
                }
            }
        }
    }
}


