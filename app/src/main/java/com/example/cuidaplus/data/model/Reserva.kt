package com.example.cuidaplus.data.model
data class Reserva(
    val fecha: String,
    val hora: String,
    val estado: String = "Pendiente"
)