package com.example.cuidaplus.ui.agenda

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
fun AgendaScreen(
    reservas: List<Reserva>,
    onNuevaCita: (() -> Unit)? = null // opcional
) {
    val primaryColor = Color(0xFF1E3A8A)
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // BUSCADOR
    var searchText by remember { mutableStateOf("") }

    // FILTRO ESPECIALIDAD
    var especialidadSeleccionada by remember { mutableStateOf("Todas") }
    val especialidades = listOf("Todas") + reservas.map { it.cuidador.especialidad }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // HEADER
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Agenda de citas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Text("Revisa, filtra y gestiona tus próximas atenciones",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Botón nueva cita
            IconButton(
                onClick = { onNuevaCita?.invoke() },
                modifier = Modifier
                    .size(48.dp)
                    .background(primaryColor, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cita", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CALENDARIO
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BUSCADOR
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por cuidador...") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // FILTRO ESPECIALIDAD
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Especialidad:", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(8.dp))

            DropdownMenuFiltro(
                opciones = especialidades,
                seleccion = especialidadSeleccionada,
                onSeleccionChange = { especialidadSeleccionada = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FILTRAR LISTA
        val reservasDelDia = remember(selectedDate, reservas, searchText, especialidadSeleccionada) {
            reservas.filter { r ->
                r.fecha == selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) &&
                        (searchText.isEmpty() || r.cuidador.nombre.contains(searchText, ignoreCase = true)) &&
                        (especialidadSeleccionada == "Todas" || r.cuidador.especialidad == especialidadSeleccionada)
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
                    Text("No hay citas programadas para este día.",
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(reservasDelDia) { reserva ->
                    ItemCitaCard(reserva)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuFiltro(opciones: List<String>, seleccion: String, onSeleccionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(seleccion)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSeleccionChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ItemCitaCard(reserva: Reserva) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6FF))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = reserva.cuidador.fotoRes),
                contentDescription = reserva.cuidador.nombre,
                modifier = Modifier.size(60.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(reserva.cuidador.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(reserva.cuidador.especialidad, color = Color.Gray, fontSize = 13.sp)
                Text("Hora: ${reserva.hora}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

        }
    }
}

@Composable
fun EstadoBadge(estado: String) {
    val color = when (estado) {
        "Confirmada" -> Color(0xFF4CAF50)
        "Pendiente" -> Color(0xFFFFC107)
        "Cancelada" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(estado, color = Color.White, fontSize = 12.sp)
    }
}
