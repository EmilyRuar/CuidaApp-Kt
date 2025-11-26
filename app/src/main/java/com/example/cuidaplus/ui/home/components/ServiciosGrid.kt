package com.example.cuidaplus.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServiciosGrid() {
    val primaryColor = Color(0xFF1E3A8A)
    val servicios = listOf(
        Pair("Teleconsulta", Icons.Filled.Phone),
        Pair("Laboratorio", Icons.Filled.Science),
        Pair("Medicinas", Icons.Filled.LocalPharmacy),
        Pair("Seguimiento", Icons.Filled.LocationOn),
        Pair("Vacunas", Icons.Filled.MedicalServices),
        Pair("Seguro", Icons.Filled.Shield)
    )

    Text("Nuestros Servicios", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.height(200.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(servicios) { (titulo, icono) ->
            Card(
                modifier = Modifier.size(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icono, contentDescription = titulo, tint = primaryColor, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(titulo, fontSize = 12.sp, color = primaryColor, textAlign = TextAlign.Center)
                }
            }
        }
    }
}