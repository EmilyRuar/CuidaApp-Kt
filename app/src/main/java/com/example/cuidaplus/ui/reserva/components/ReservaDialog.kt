package com.example.cuidaplus.ui.reserva.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cuidaplus.data.Cuidador
import com.example.cuidaplus.data.Reserva
import com.example.cuidaplus.data.cuidadoresList
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaDialog(
    reserva: Reserva?,
    onDismiss: () -> Unit,
    onSave: (Cuidador, String, String) -> Unit
) {
    var selectedCuidador by remember { mutableStateOf(reserva?.cuidador ?: cuidadoresList.first()) }
    var fechaText by remember {
        mutableStateOf(
            reserva?.fecha ?: LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }
    var horaText by remember {
        mutableStateOf(
            reserva?.hora ?: LocalTime.now().withSecond(0).withNano(0).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        )
    }

    val dialogTitle = if (reserva == null) "Crear Nueva Reserva" else "Editar Reserva"
    val primaryColor = Color(0xFF1E3A8A)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = selectedCuidador.nombre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cuidador") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar Cuidador")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    cuidadoresList.forEach { cuidador ->
                        DropdownMenuItem(
                            text = { Text(cuidador.nombre) },
                            onClick = {
                                selectedCuidador = cuidador
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = fechaText,
                    onValueChange = { fechaText = it },
                    label = { Text("Fecha (ej: 21/10/2025)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = horaText,
                    onValueChange = { horaText = it },
                    label = { Text("Hora (ej: 10:00 AM o 10:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fechaText.isNotBlank() && horaText.isNotBlank()) {
                        onSave(selectedCuidador, fechaText, horaText)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) {
                Text("Cancelar")
            }
        }
    )
}