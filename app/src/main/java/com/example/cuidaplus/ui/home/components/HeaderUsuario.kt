package com.example.cuidaplus.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderUsuario(nombre: String = "Emily Rupay") {
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF475569)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("¡Bienvenida!", fontSize = 16.sp, color = secondaryColor)
            Text(nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            Text("Cliente", fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Perfil",
                tint = primaryColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
