package com.example.cuidaplus.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cuidaplus.data.Message

@Composable
fun ChatScreen() {
    val primaryColor = Color(0xFF1E3A8A)

    // Lista de mensajes
    val messages = remember {
        mutableStateListOf(
            Message("¡Bienvenido al chat de CuidaPlus! 😊 ¿En qué podemos ayudarte hoy?", false)
        )
    }

    // Estado del input
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        // LISTA DE MENSAJES
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    contentAlignment = if (message.isSentByUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.isSentByUser) primaryColor else Color(0xFFE0E7FF)
                        )
                    ) {
                        Text(
                            message.text,
                            modifier = Modifier.padding(8.dp),
                            color = if (message.isSentByUser) Color.White else primaryColor
                        )
                    }
                }
            }
        }

        // INPUT + BOTÓN
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje") }
            )

            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        val userMessage = text.trim()
                        messages.add(Message(userMessage, true))

                        // RESPUESTA AUTOMÁTICA
                        val response = getCuidaPlusResponse(userMessage)
                        messages.add(Message(response, false))

                        text = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = primaryColor)
            }
        }
    }
}

/* ------------------------------
      MOTOR DE RESPUESTAS
   ------------------------------ */

fun getCuidaPlusResponse(userText: String): String {
    val text = userText.lowercase()

    return when {
        text.contains("precio") || text.contains("cuánto") -> """
            Nuestros precios dependen del tipo de servicio:
            • Enfermería: desde $18.000 por visita.
            • Cuidadora por hora: desde $8.000.
            • Turnos completos: desde $45.000.
            
            ¿Te gustaría una cotización personalizada?
        """.trimIndent()

        text.contains("servicios") || text.contains("qué ofrecen") || text.contains("hacen") -> """
            En CuidaPlus ofrecemos:
            • Enfermería a domicilio (curaciones, inyecciones, control de signos vitales)
            • Cuidadores de adultos mayores
            • Acompañamiento 24/7
            • Administración de medicamentos
            
            ¿Qué servicio necesitas?
        """.trimIndent()

        text.contains("enfermera") || text.contains("enfermería") -> """
            Contamos con enfermeras certificadas para:
            • Curaciones avanzadas
            • Inyecciones
            • Control de presión, glucosa y signos vitales
            • Post operatorio
            
            ¿Qué procedimiento necesitas?
        """.trimIndent()

        text.contains("cuidadora") || text.contains("cuidador") -> """
            Nuestras cuidadoras pueden ayudarte con:
            • Aseo personal
            • Movilización del paciente
            • Acompañamiento
            • Alimentación y medicamentos
            
            ¿Necesitas una cuidadora por hora o por turno completo?
        """.trimIndent()

        text.contains("horario") || text.contains("disponible") -> """
            Atendemos las 24 horas del día, los 7 días de la semana. 🕒  
            Solo indícanos el día, horario y dirección para agendar tu servicio.
        """.trimIndent()

        text.contains("dónde atienden") || text.contains("zona") || text.contains("cobertura") -> """
            Actualmente atendemos en:
            • Santiago Centro
            • Las Condes
            • Providencia
            • Ñuñoa
            • Maipú
            • La Florida
            • San Miguel
            
            ¿En qué comuna necesitas el servicio?
        """.trimIndent()

        text.contains("contacto") || text.contains("hablar") -> """
            Podemos tomar tus datos para contactarte o también puedes llamar al:
            📞 +56 9 9999 9999
            
            ¿Deseas que te contacte un asesor?
        """.trimIndent()

        else -> """
            ¡Gracias por escribir! 😊
            No entendí muy bien tu consulta, pero puedo ayudarte con:
            • Precios
            • Servicios
            • Enfermeras
            • Cuidadoras
            • Disponibilidad
            • Zonas de atención
            
            Solo escribe una palabra clave y te respondo.
        """.trimIndent()
    }
}
