package com.example.cuidaplus.data

data class Cuidador(
    val nombre: String,
    val especialidad: String,
    val valoracion: Float,
    val fotoRes: Int
)

data class Message(
    val text: String,
    val isSentByUser: Boolean
)

data class Reserva(
    val cuidador: Cuidador,
    val fecha: String,
    val hora: String,
    val id: Int
)
