package com.example.cuidaplus

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cuidaplus.util.PreferencesManager
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

/*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
*/



@Composable
fun HomeScreen(nombre: String, apellido: String, navController: NavController){
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Bienvenido(a) $nombre $apellido", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val activity = context as Activity
                val intent = Intent(context, HomePrincipalActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
    }
}
