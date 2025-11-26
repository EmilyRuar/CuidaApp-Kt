package com.example.cuidaplus.ui.especialistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuidaplus.data.cuidadoresList

@Composable
fun EspecialistasScreen() {
    val primaryColor = Color(0xFF1E3A8A)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Especialist@s",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(cuidadoresList) { cuidador ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = cuidador.fotoRes),
                            contentDescription = cuidador.nombre,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = cuidador.nombre,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                            Text(text = cuidador.especialidad)
                            Text(text = "Valoración: ${cuidador.valoracion}")
                            Button(
                                onClick = { },
                                modifier = Modifier.padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                            ) {
                                Text("Agendar cita", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
