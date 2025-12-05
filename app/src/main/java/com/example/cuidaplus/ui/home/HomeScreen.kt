package com.example.cuidaplus.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onServiciosClick: () -> Unit,
    onLogout: () -> Unit
) {
    val primaryColor = Color(0xFF2563EB)
    val backgroundColor = Color(0xFFF8FAFC)

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Equipo") },
                    label = { Text("Equipo") },
                    selected = false,
                    onClick = {
                        // Si quieres navegar aquí, luego lo agregamos.
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Agenda") },
                    label = { Text("Agenda") },
                    selected = false,
                    onClick = {
                        onServiciosClick() // si tu agenda está en servicios
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Home") },
                    selected = true, // <-- pantalla activa
                    onClick = {}
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = false,
                    onClick = {
                        // próximamente
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Reservas") },
                    label = { Text("Reserva") },
                    selected = false,
                    onClick = {
                        onServiciosClick() // se puede separar luego
                    }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // ---------- HEADER ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Bienvenido 👋", fontSize = 18.sp, color = Color.Gray)
                    Text(
                        "Explora nuestros servicios",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }

                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Usuario",
                    tint = primaryColor,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { showLogoutDialog = true } // ← mismo comportamiento que antes
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // ---------- BUSCADOR ----------
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Buscar servicios o pacientes...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            // ---------- SERVICIOS DESTACADOS ----------
            Text(
                "Servicios destacados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    listOf(
                        "Cuidado en casa",
                        "Atención médica",
                        "Rehabilitación",
                        "Asesoría nutricional"
                    )
                ) { servicio ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable { onServiciosClick() },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(servicio, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }


    // ---------- DIALOGO DE CERRAR SESIÓN ----------
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás segura que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Sí", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
