package com.example.cuidaplus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.cuidaplus.ui.home.components.HeaderUsuario
import com.example.cuidaplus.ui.home.components.ServiciosGrid

@Composable
fun HomeScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF475569)
    val backgroundColor = Color(0xFFF8FAFC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        HeaderUsuario()

        Text("Encuentra tu especialista", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Buscar por nombre, especialidad...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Categorías", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Adultos", "Niños", "Nutrición", "Domicilio").forEach { categoria ->
                AssistChip(
                    onClick = { },
                    label = { Text(categoria) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = primaryColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        ServiciosGrid()
        Spacer(modifier = Modifier.height(24.dp))

        Text("Especialistas disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(
                listOf(
                    Triple("Ana Pérez", "Cuidadora de Adultos Mayores", "⭐ 4.8"),
                    Triple("Luis Gómez", "Enfermero Calificado", "⭐ 4.5"),
                    Triple("María Torres", "Cuidadora a domicilio", "⭐ 4.9")
                )
            ) { (nombre, especialidad, rating) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = primaryColor, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryColor)
                            Text(especialidad, fontSize = 14.sp, color = secondaryColor)
                            Text(rating, fontSize = 14.sp, color = Color(0xFFFB923C))
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text("Agendar", color = Color.White)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}