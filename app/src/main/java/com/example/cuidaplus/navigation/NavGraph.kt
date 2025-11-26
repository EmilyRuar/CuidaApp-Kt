package com.example.cuidaplus.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cuidaplus.data.Reserva
import com.example.cuidaplus.data.cuidadoresList
import com.example.cuidaplus.ui.home.HomeScreen
import com.example.cuidaplus.ui.agenda.AgendaScreen
import com.example.cuidaplus.ui.chat.ChatScreen
import com.example.cuidaplus.ui.especialistas.EspecialistasScreen
import com.example.cuidaplus.ui.reserva.ReservaScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val primaryColor = Color(0xFF1E3A8A)

    val reservasState = remember {
        mutableStateListOf(
            Reserva(cuidadoresList[0], "21/10/2025", "10:00 AM", 1),
            Reserva(cuidadoresList[1], "22/10/2025", "11:00 AM", 2),
            Reserva(cuidadoresList[2], "23/10/2025", "09:00 AM", 3)
        )
    }

    var nextId by remember { mutableStateOf(4) }

    val addReserva: (Reserva) -> Unit = { nuevaReserva ->
        reservasState.add(nuevaReserva.copy(id = nextId))
        nextId++
    }

    val updateReserva: (Reserva) -> Unit = { reservaActualizada ->
        val index = reservasState.indexOfFirst { it.id == reservaActualizada.id }
        if (index != -1) {
            reservasState[index] = reservaActualizada
        }
    }

    val deleteReserva: (Reserva) -> Unit = { reservaAEliminar ->
        reservasState.remove(reservaAEliminar)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = primaryColor) {
                listOf(
                    BottomNavItem.Equipo,
                    BottomNavItem.Agenda,
                    BottomNavItem.Home,
                    BottomNavItem.Chat,
                    BottomNavItem.Reserva
                ).forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(item.icon, contentDescription = item.label, tint = Color.White)
                        },
                        label = { Text(item.label, color = Color.White) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (selectedTab) {
                BottomNavItem.Home -> HomeScreen()
                BottomNavItem.Equipo -> EspecialistasScreen()
                BottomNavItem.Agenda -> AgendaScreen(reservas = reservasState)
                BottomNavItem.Chat -> ChatScreen()
                BottomNavItem.Reserva -> ReservaScreen(
                    reservas = reservasState,
                    onAddReserva = addReserva,
                    onUpdateReserva = updateReserva,
                    onDeleteReserva = deleteReserva
                )
            }
        }
    }
}
