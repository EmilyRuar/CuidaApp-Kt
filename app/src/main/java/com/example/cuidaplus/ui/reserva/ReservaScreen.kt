package com.example.cuidaplus.ui.reserva

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.cuidaplus.ui.reserva.components.ReservaDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservaScreen(
    reservas: List<Reserva>,
    onAddReserva: (Reserva) -> Unit,
    onUpdateReserva: (Reserva) -> Unit,
    onDeleteReserva: (Reserva) -> Unit
) {
    val primaryColor = Color(0xFF1E3A8A)
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedReserva by remember { mutableStateOf<Reserva?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        // ---------- HEADER ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mis Reservas",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryColor
            )

            Button(
                onClick = {
                    selectedReserva = null
                    isDialogOpen = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Nueva Reserva")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- LISTA ----------
        LazyColumn {
            items(reservas, key = { it.id }) { reserva ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // ---------- IZQUIERDA: foto + info ----------
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Image(
                                painter = painterResource(id = reserva.cuidador.fotoRes),
                                contentDescription = reserva.cuidador.nombre,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    reserva.cuidador.nombre,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor,
                                    fontSize = 17.sp
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    "${reserva.fecha} • ${reserva.hora}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        // ---------- DERECHA: acciones ----------
                        Row {
                            IconButton(
                                onClick = {
                                    selectedReserva = reserva
                                    isDialogOpen = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color(0xFF3B82F6)
                                )
                            }

                            IconButton(
                                onClick = { onDeleteReserva(reserva) }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------- DIALOG ----------
    if (isDialogOpen) {
        ReservaDialog(
            reserva = selectedReserva,
            onDismiss = { isDialogOpen = false },
            onSave = { cuidador, fecha, hora ->
                if (selectedReserva == null) {
                    val newReserva = Reserva(cuidador, fecha, hora, 0)
                    onAddReserva(newReserva)
                } else {
                    val updated = selectedReserva!!.copy(
                        cuidador = cuidador,
                        fecha = fecha,
                        hora = hora
                    )
                    onUpdateReserva(updated)
                }
                isDialogOpen = false
            }
        )
    }
}
