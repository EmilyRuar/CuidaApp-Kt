package com.example.cuidaplus.ui.agenda

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuidaplus.data.Reserva
import com.example.cuidaplus.ui.agenda.components.CalendarView
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen(reservas: List<Reserva>) {
    val primaryColor = Color(0xFF1E3A8A)
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Agenda de citas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val reservasDelDia = remember(selectedDate, reservas) {
            reservas.filter {
                it.fecha == selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }

        Text(
            "Citas para ${selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = primaryColor
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            if (reservasDelDia.isEmpty()) {
                item {
                    Text("No hay citas programadas para este día.", color = Color.Gray)
                }
            } else {
                items(reservasDelDia) { reserva ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = reserva.cuidador.fotoRes),
                                contentDescription = reserva.cuidador.nombre,
                                modifier = Modifier.size(48.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(reserva.cuidador.nombre, fontWeight = FontWeight.Bold)
                                Text("${reserva.cuidador.especialidad} - ${reserva.hora}")
                            }
                        }
                    }
                }
            }
        }
    }
}
