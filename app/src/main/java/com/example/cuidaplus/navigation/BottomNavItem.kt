package com.example.cuidaplus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val label: String, val icon: ImageVector) {
    object Equipo : BottomNavItem("Equipo", Icons.Filled.Person)
    object Agenda : BottomNavItem("Agenda", Icons.Filled.CalendarToday)
    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Chat : BottomNavItem("Chat", Icons.Filled.Chat)
    object Reserva : BottomNavItem("Reserva", Icons.Filled.List)

}