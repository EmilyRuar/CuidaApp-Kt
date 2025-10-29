package com.example.cuidaplus

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

// ------------------------
// MODELOS DE DATOS
// ------------------------

// Representa un cuidador/profesional
data class Cuidador(
    val nombre: String,
    val especialidad: String,
    val valoracion: Float,
    val fotoRes: Int // recurso drawable (ej: R.drawable.cuidador1)
)

// Mensaje del chat (texto y si fue enviado por el usuario)
data class Message(val text: String, val isSentByUser: Boolean)

// Reserva con referencia a cuidador, fecha, hora e id para identificarla
data class Reserva(val cuidador: Cuidador, val fecha: String, val hora: String, val id: Int)

// ------------------------
// LISTA DE CUIDADORES (MOCK / DATOS LOCALES)
// ------------------------
// Recuerda: debes tener los drawables con los nombres referenciados en res/drawable
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
// ITEMS PARA BOTTOM NAVIGATION
// ------------------------
// Clase sealed para organizar las pestañas del bottom bar
sealed class BottomNavItem(val label: String, val icon: ImageVector) {
    object Equipo : BottomNavItem("Equipo", Icons.Filled.Person)
    object Agenda : BottomNavItem("Agenda", Icons.Filled.CalendarToday)
    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Chat : BottomNavItem("Chat", Icons.Filled.Chat)
    object Reserva : BottomNavItem("Reserva", Icons.Filled.List)
}

// ------------------------
// ACTIVIDAD PRINCIPAL
// ------------------------
class HomePrincipalActivity : ComponentActivity() {
    // Requiere API >= O (para LocalDate / LocalTime usados en calendarios)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Contenido Compose
        setContent {
            HomeApp()
        }
    }
}

// ------------------------
// COMPOSABLE RAÍZ - HomeApp
// - Maneja el estado global de la pantalla (reservas) y navegación inferior
// ------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeApp() {
    // Estado para la pestaña seleccionada del BottomNav
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val primaryColor = Color(0xFF1E3A8A)

    // 1) Estado mutable tipo lista para las reservas (soporta CRUD)
    val reservasState = remember {
        mutableStateListOf(
            Reserva(cuidadoresList[0], "21/10/2025", "10:00 AM", 1),
            Reserva(cuidadoresList[1], "22/10/2025", "11:00 AM", 2),
            Reserva(cuidadoresList[2], "23/10/2025", "09:00 AM", 3)
        )
    }

    // 2) Contador simple para asignar IDs únicos a nuevas reservas
    var nextId by remember { mutableStateOf(4) }

    // 3) FUNCIONES CRUD que modifican el estado (se pasan a ReservaScreen)
    val addReserva: (Reserva) -> Unit = { nuevaReserva ->
        // Copia la reserva y le asigna el siguiente id
        reservasState.add(nuevaReserva.copy(id = nextId))
        nextId++
    }

    val updateReserva: (Reserva) -> Unit = { reservaActualizada ->
        // Busca por id y reemplaza en la lista
        val index = reservasState.indexOfFirst { it.id == reservaActualizada.id }
        if (index != -1) {
            reservasState[index] = reservaActualizada
        }
    }

    val deleteReserva: (Reserva) -> Unit = { reservaAEliminar ->
        // Remueve la reserva de la lista
        reservasState.remove(reservaAEliminar)
    }

    // Scaffold con BottomBar (NavigationBar)
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
                        icon = { Icon(item.icon, contentDescription = item.label, tint = Color.White) },
                        label = { Text(item.label, color = Color.White) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Contenedor principal que muestra la pantalla según la pestaña seleccionada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (selectedTab) {
                BottomNavItem.Home -> HomeUsuarioScreen()
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

// ------------------------
// HomeUsuarioScreen - Pantalla principal del usuario
// ------------------------
@Composable
fun HomeUsuarioScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF475569)
    val backgroundColor = Color(0xFFF8FAFC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        HeaderUsuario() // Cabecera con nombre
        // Título y búsqueda
        Text("Encuentra tu especialista", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* búsqueda - actualmente mock */ },
            placeholder = { Text("Buscar por nombre, especialidad...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categorías (chips)
        Text("Categorías", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Adultos", "Niños", "Nutrición", "Domicilio").forEach { categoria ->
                AssistChip(
                    onClick = { /* filtrar - mock */ },
                    label = { Text(categoria) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = primaryColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        ServiciosGrid() // Grid con servicios
        Spacer(modifier = Modifier.height(24.dp))

        // Lista rápida de especialistas (mock)
        Text("Especialistas disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(
                listOf(
                    Triple("Ana Pérez", "Cuidadora de Adultos Mayores", "⭐ 4.8"),
                    Triple("Luis Gómez", "Enfermero Calificado", "⭐ 4.5"),
                    Triple("María Torres", "Cuidadora a domicilio", "⭐ 4.9")
                )
            ) { (nombre, especialidad, rating) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = primaryColor, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryColor)
                            Text(especialidad, fontSize = 14.sp, color = secondaryColor)
                            Text(rating, fontSize = 14.sp, color = Color(0xFFFB923C))
                        }
                        Button(
                            onClick = { /* Agendar - mock */ },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text("Agendar", color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Botón de acción rápido (usado si quieres acciones de ancho completo)
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
// Pantalla: Especialistas (lista detallada con fotos)
// ------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecialistasScreen() {
    val primaryColor = Color(0xFF1E3A8A)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Especialistas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            // Recorre la lista mock de cuidadores
            items(cuidadoresList) { cuidador ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen circular del cuidador
                        Image(
                            painter = painterResource(id = cuidador.fotoRes),
                            contentDescription = cuidador.nombre,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = cuidador.nombre,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                            Text(text = cuidador.especialidad)
                            Text(text = "Valoración: ${cuidador.valoracion}")
                            Button(
                                onClick = { /* Agendar cita - mock */ },
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
}

// ------------------------
// Pantalla: Agenda (muestra calendario y reservas del día seleccionado)
// ------------------------
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

        // Composable que dibuja un calendario básico
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtra las reservas que coinciden con la fecha seleccionada
        val reservasDelDia = remember(selectedDate, reservas) {
            reservas.filter { it.fecha == selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
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

// ------------------------
// CalendarView - calendario simple (no dependencias externas)
// ------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val primaryColor = Color(0xFF1E3A8A)
    val currentMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = currentMonth.lengthOfMonth()

    // Determina en qué columna comienza el mes (ajuste para Lunes como inicio)
    val firstDayOfWeek = currentMonth.dayOfWeek.value // 1=Mon .. 7=Sun
    val startOffset = if (firstDayOfWeek == 7) 6 else firstDayOfWeek - 1

    // Etiquetas de día (puedes ajustar idioma/local)
    val dayLabelsFormatted = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Encabezado mes + año
        Text(
            currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.titlecase() } +
                    " ${currentMonth.year}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            dayLabelsFormatted.forEach { day ->
                Text(day, fontWeight = FontWeight.SemiBold, color = primaryColor, fontSize = 12.sp)
            }
        }

        // Días del mes renderizados en filas por semanas
        Column {
            var dayCount = 1
            for (week in 0 until 6) { // hasta 6 filas (algunas celdas quedarán vacías)
                if (dayCount > daysInMonth && week > 0) break

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    for (dayInWeekIndex in 0..6) {
                        val cellIndex = week * 7 + dayInWeekIndex
                        val isDayValid = cellIndex >= startOffset && dayCount <= daysInMonth

                        if (isDayValid) {
                            val currentDate = currentMonth.withDayOfMonth(dayCount)
                            val isSelected = currentDate == selectedDate
                            val isToday = currentDate == LocalDate.now()

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) primaryColor else if (isToday) Color(0xFFE0E7FF) else Color.Transparent)
                                    .clickable { onDateSelected(currentDate) }
                            ) {
                                Text(
                                    text = dayCount.toString(),
                                    color = if (isSelected) Color.White else if (isToday) primaryColor else Color.Black,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            dayCount++
                        } else {
                            // Celdas vacías al inicio/final
                            Spacer(modifier = Modifier.size(36.dp))
                        }
                    }
                }
                if (dayCount > daysInMonth) break
            }
        }
    }
}

// ------------------------
// ChatScreen - Chat básico con mensajes en memoria (mock)
// ------------------------
@Composable
fun ChatScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    // Lista mutable de mensajes inicializada con un mensaje de bienvenida
    val messages = remember { mutableStateListOf(
        Message("¡Bienvenido al chat! ¿En qué podemos ayudarte hoy?", false)
    )}

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Mensajes (lista desplazable)
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

        // Área de input y botón enviar
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
                    messages.add(Message(text, true)) // añade mensaje enviado por usuario
                    text = "" // limpia campo
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = primaryColor)
            }
        }
    }
}

// ------------------------
// ReservaScreen - CRUD de reservas (usa las funciones pasadas desde HomeApp)
// ------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservaScreen(
    reservas: List<Reserva>, // lista de lectura
    onAddReserva: (Reserva) -> Unit, // callback para CREATE
    onUpdateReserva: (Reserva) -> Unit, // callback para UPDATE
    onDeleteReserva: (Reserva) -> Unit // callback para DELETE
) {
    val primaryColor = Color(0xFF1E3A8A)
    var isDialogOpen by remember { mutableStateOf(false) } // controla si el diálogo de crear/editar está abierto
    var selectedReserva by remember { mutableStateOf<Reserva?>(null) } // reserva seleccionada para editar

    Column(modifier = Modifier.padding(16.dp)) {
        // Título y botón nueva reserva
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis Reservas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            Button(
                onClick = {
                    selectedReserva = null // null => creación
                    isDialogOpen = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) {
                Text("Nueva Reserva")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de reservas (READ)
        LazyColumn {
            items(reservas, key = { it.id }) { reserva ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Info de reserva (imagen + texto)
                        Row(verticalAlignment = Alignment.CenterVertically) {
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

                        // Botones acción: editar y eliminar
                        Row {
                            IconButton(onClick = {
                                selectedReserva = reserva
                                isDialogOpen = true // abre diálogo en modo edición
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                            }
                            IconButton(onClick = {
                                onDeleteReserva(reserva) // llama al callback delete
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG para crear / editar reserva
    if (isDialogOpen) {
        ReservaDialog(
            reserva = selectedReserva,
            onDismiss = { isDialogOpen = false },
            onSave = { cuidador, fecha, hora ->
                if (selectedReserva == null) {
                    // CREATE: id se asigna en HomeApp (se pasa id=0 temporal)
                    val newReserva = Reserva(cuidador, fecha, hora, 0)
                    onAddReserva(newReserva)
                } else {
                    // UPDATE: mantiene id original y reemplaza datos
                    val updatedReserva = selectedReserva!!.copy(cuidador = cuidador, fecha = fecha, hora = hora)
                    onUpdateReserva(updatedReserva)
                }
                isDialogOpen = false
            }
        )
    }
}

// ------------------------
// Dialogo para crear/editar Reserva
// ------------------------
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaDialog(
    reserva: Reserva?,
    onDismiss: () -> Unit,
    onSave: (Cuidador, String, String) -> Unit
) {
    // Estado local para cuidador seleccionado y campos de fecha/hora
    var selectedCuidador by remember {
        mutableStateOf(reserva?.cuidador ?: cuidadoresList.first())
    }
    var fechaText by remember { mutableStateOf(reserva?.fecha ?: LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))) }
    var horaText by remember { mutableStateOf(reserva?.hora ?: LocalTime.now().withSecond(0).withNano(0).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))) }
    val dialogTitle = if (reserva == null) "Crear Nueva Reserva" else "Editar Reserva"
    val primaryColor = Color(0xFF1E3A8A)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                // Selector desplegable de cuidadores
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

                // Campo fecha (texto editable) - podrías cambiar por DatePicker
                OutlinedTextField(
                    value = fechaText,
                    onValueChange = { fechaText = it },
                    label = { Text("Fecha (ej: 21/10/2025)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Campo hora (texto editable) - podrías cambiar por TimePicker
                OutlinedTextField(
                    value = horaText,
                    onValueChange = { horaText = it },
                    label = { Text("Hora (ej: 10:00 AM o 10:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (fechaText.isNotBlank() && horaText.isNotBlank()) {
                    onSave(selectedCuidador, fechaText, horaText)
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) { Text("Guardar") }
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

// ------------------------
// HeaderUsuario - Cabecera con saludo y botón de perfil
// ------------------------
@Composable
fun HeaderUsuario(nombre: String = "Emily Rupay") {
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF475569)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("¡Bienvenida!", fontSize = 16.sp, color = secondaryColor)
            Text(nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            Text("Cliente", fontSize = 14.sp, color = Color.Gray)
        }

        // Icono perfil (acción mock)
        IconButton(onClick = { /* Abrir perfil o ajustes - pendiente */ }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Perfil",
                tint = primaryColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ------------------------
// ServiciosGrid - Grid con servicios ofrecidos
// ------------------------
@Composable
fun ServiciosGrid() {
    val primaryColor = Color(0xFF1E3A8A)
    val servicios = listOf(
        Pair("Teleconsulta", Icons.Filled.Phone),
        Pair("Laboratorio", Icons.Filled.Science),
        Pair("Medicinas", Icons.Filled.LocalPharmacy),
        Pair("Seguimiento", Icons.Filled.LocationOn),
        Pair("Vacunas", Icons.Filled.MedicalServices),
        Pair("Seguro", Icons.Filled.Shield)
    )

    Text("Nuestros Servicios", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.height(200.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(servicios) { (titulo, icono) ->
            Card(
                modifier = Modifier.size(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icono, contentDescription = titulo, tint = primaryColor, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(titulo, fontSize = 12.sp, color = primaryColor, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
