package com.example.cuidaplus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuidaplus.ui.home.components.ServiciosGrid

@Composable
fun HomeScreen(
    onServiciosClick: () -> Unit,
    onLogout: () -> Unit
) {
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF475569)
    val backgroundColor = Color(0xFFF0F4FF)

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {

        // ---------- HEADER ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Bienvenido 👋", fontSize = 18.sp, color = secondaryColor)
                Text(
                    "¿Qué necesitas hoy?",
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
                    .size(42.dp)
                    .clickable { showLogoutDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- BUSCADOR ----------
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Buscar servicio, cuidado, atención...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))


        // ---------- BOTÓN SERVICIOS ----------
        Button(
            onClick = { onServiciosClick() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.List, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Nuestros Servicios", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))


        // ---------- RESEÑAS ----------
        Text(
            "Clientes Satisfechos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        val reseñas = listOf(
            Triple("Carolina S.", "Excelente atención, muy profesionales y cariñosos con mi mamá.", 5),
            Triple("Diego P.", "Servicio rápido, confiable y el cuidador fue muy puntual.", 4),
            Triple("Marcela R.", "Gran experiencia, recomiendo totalmente.", 5)
        )

        LazyColumn {
            items(reseñas) { (nombre, comentario, estrellas) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryColor)
                        Text(comentario, fontSize = 14.sp, color = secondaryColor)

                        Spacer(Modifier.height(6.dp))

                        Row {
                            repeat(estrellas) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- REDES SOCIALES ----------
        Text(
            "Síguenos en redes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Facebook, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp))
            Icon(Icons.Default.Share, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp))
            Icon(Icons.Default.Phone, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp))
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
