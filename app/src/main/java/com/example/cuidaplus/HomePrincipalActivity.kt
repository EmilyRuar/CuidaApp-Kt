package com.example.cuidaplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime

// ------------------------
// Modelo de datos simulado
// ------------------------
data class Cuidador(val nombre: String, val especialidad: String, val valoracion: Float, val fotoRes: Int)
data class Message(val text: String, val isSentByUser: Boolean)
data class Reserva(val cuidador: Cuidador, val fecha: String, val hora: String)

// ------------------------
// Lista de cuidadores con foto
// ------------------------
val cuidadoresList = listOf(
    Cuidador("Ana Pérez", "Cuidadora de Adultos Mayores", 4.8f, R.drawable.cuidador1),
    Cuidador("Luis Gómez", "Enfermero Calificado", 4.5f, R.drawable.cuidador9),
    Cuidador("María Torres", "Cuidadora a domicilio", 4.9f, R.drawable.cuidador3),
    Cuidador("Carlos Soto", "Enfermero", 4.7f, R.drawable.cuidador4),
    Cuidador("Lucía Fernández", "Cuidadora", 4.6f, R.drawable.cuidador8),
    Cuidador("Sandra Ramírez", "Cuidador Especializado", 4.4f, R.drawable.cuidador6),
    Cuidador("Isabel Rojas", "Enfermera", 4.9f, R.drawable.cuidador7),
    Cuidador("Pedro Castillo", "Cuidador Adulto Mayor", 4.3f, R.drawable.cuidador5),
    Cuidador("Marta Díaz", "Cuidadora a domicilio", 4.8f, R.drawable.cuidador14),
    Cuidador("Andrés Molina", "Enfermero Calificado", 4.5f, R.drawable.cuidador10),
    Cuidador("Verónica Castro", "Cuidadora Especializada", 4.7f, R.drawable.cuidador11),
    Cuidador("Maria Palacios", "Cuidadora Adulto Mayor", 4.6f, R.drawable.cuidador12),
    Cuidador("Fernanda Salazar", "Cuidadora a domicilio", 4.9f, R.drawable.cuidador13)
)

// ------------------------
// BottomNavigation Items
// ------------------------
sealed class BottomNavItem(val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Cuidadores : BottomNavItem("Cuidadores", Icons.Filled.Person)
    object Agenda : BottomNavItem("Agenda", Icons.Filled.CalendarToday)
    object Chat : BottomNavItem("Chat", Icons.Filled.Chat)
    object Reserva : BottomNavItem("Reserva", Icons.Filled.List)
}

// ------------------------
// HomePrincipalActivity
// ------------------------
class HomePrincipalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeApp()
        }
    }
}

// ------------------------
// Composable principal
// ------------------------
@Composable
fun HomeApp() {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val primaryColor = Color(0xFF1E3A8A)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = primaryColor) {
                listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Cuidadores,
                    BottomNavItem.Agenda,
                    BottomNavItem.Chat,
                    BottomNavItem.Reserva
                ).forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label, tint = Color.White) },
                        label = { Text(item.label, color = Color.White) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)) { // color uniforme en todas las pantallas
            when (selectedTab) {
                BottomNavItem.Home -> HomeUsuarioScreen()
                BottomNavItem.Cuidadores -> CuidadoresScreen()
                BottomNavItem.Agenda -> AgendaScreen()
                BottomNavItem.Chat -> ChatScreen()
                BottomNavItem.Reserva -> ReservaScreen()
            }
        }
    }
}

// ------------------------
// 1️⃣ HomeUsuarioScreen
// ------------------------
@Composable
fun HomeUsuarioScreen() {
    val primaryColor = Color(0xFF1E3A8A)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Header con perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("María Pérez", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                Text("Cliente", fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* Abrir configuración */ }) {
                Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = primaryColor)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Novedades
        Text("Novedades", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(listOf(
                "Nueva cuidadora disponible cerca de tu zona",
                "Tu próxima cita es el 21/10/2025 a las 10:00 AM"
            )) { novedad ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))
                ) {
                    Text(novedad, modifier = Modifier.padding(16.dp), color = primaryColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Accesos rápidos
        Text("Accesos rápidos", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            QuickActionButton("Agendar cita", primaryColor)
            QuickActionButton("Buscar cuidadora", primaryColor)
            QuickActionButton("Chat", primaryColor)
            QuickActionButton("Ver agenda", primaryColor)
        }
    }
}

@Composable
fun QuickActionButton(label: String, color: Color) {
    Button(
        onClick = { /* Acción */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(label, color = Color.White) }
}

// ------------------------
// 2️⃣ CuidadoresScreen
// ------------------------
@Composable
fun CuidadoresScreen() {
    val primaryColor = Color(0xFF1E3A8A)

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(cuidadoresList) { cuidador ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))

            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = cuidador.fotoRes),
                        contentDescription = cuidador.nombre,
                        modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(cuidador.nombre, fontWeight = FontWeight.Bold, color = primaryColor)
                        Text(cuidador.especialidad)
                        Text("Valoración: ${cuidador.valoracion}")
                        Button(onClick = { /* Agendar cita */ },
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

// ------------------------
// 3️⃣ AgendaScreen
// ------------------------
@Composable
fun AgendaScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Agenda de citas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(16.dp))

        // Calendario simulado
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("Calendario", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Doctores disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(cuidadoresList.take(5)) { doctor ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = doctor.fotoRes),
                            contentDescription = doctor.nombre,
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(doctor.nombre, fontWeight = FontWeight.Bold)
                            Text(doctor.especialidad)
                        }
                    }
                }
            }
        }
    }
}

// ------------------------
// 4️⃣ ChatScreen
// ------------------------
@Composable
fun ChatScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    val messages = remember { mutableStateListOf(
        Message("¡Bienvenido al chat! Por favor, elige un número entre 1 y 5:", false)
    )}

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    contentAlignment = if (message.isSentByUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (message.isSentByUser) primaryColor else Color.White)
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje") }
            )
            IconButton(onClick = {
                if (text.isNotBlank()) {
                    messages.add(Message(text, true))
                    text = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = primaryColor)
            }
        }
    }
}

// ------------------------
// 5️⃣ ReservaScreen
// ------------------------
@Composable
fun ReservaScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    val reservas = listOf(
        Reserva(cuidadoresList[0], "21/10/2025", "10:00 AM"),
        Reserva(cuidadoresList[1], "22/10/2025", "11:00 AM"),
        Reserva(cuidadoresList[2], "23/10/2025", "09:00 AM")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reservas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(reservas) { reserva ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = reserva.cuidador.fotoRes),
                            contentDescription = reserva.cuidador.nombre,
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(reserva.cuidador.nombre, fontWeight = FontWeight.Bold, color = primaryColor)
                            Text("${reserva.fecha} - ${reserva.hora}")
                        }
                    }
                }
            }
        }
    }
}
