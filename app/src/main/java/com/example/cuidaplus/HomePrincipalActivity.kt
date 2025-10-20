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
// Modelo de datos modificado
// ------------------------
data class Cuidador(val nombre: String, val especialidad: String, val valoracion: Float, val fotoRes: Int)
data class Message(val text: String, val isSentByUser: Boolean)
// Añadido 'id' para facilitar las operaciones CRUD (Actualizar y Eliminar)
data class Reserva(val cuidador: Cuidador, val fecha: String, val hora: String, val id: Int)

// ------------------------
// Lista de cuidadores con foto (Asume que tienes los drawables R.drawable.cuidadorX)
// ------------------------
// NOTA: Para que este código funcione, debes asegurarte de tener las imágenes
// nombradas cuidador1, cuidador9, cuidador3, etc., en tu carpeta res/drawable.
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

    object Equipo : BottomNavItem("Equipo", Icons.Filled.Person)
    object Agenda : BottomNavItem("Agenda", Icons.Filled.CalendarToday)

    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Chat : BottomNavItem("Chat", Icons.Filled.Chat)
    object Reserva : BottomNavItem("Reserva", Icons.Filled.List)
}

// ------------------------
// HomePrincipalActivity
// ------------------------
class HomePrincipalActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeApp()
        }
    }
}

// ------------------------
// Composable principal (Manejo de Estado CRUD)
// ------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeApp() {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val primaryColor = Color(0xFF1E3A8A)

    // 1. Estado mutable para el CRUD de Reservas
    val reservasState = remember {
        mutableStateListOf(
            Reserva(cuidadoresList[0], "21/10/2025", "10:00 AM", 1),
            Reserva(cuidadoresList[1], "22/10/2025", "11:00 AM", 2),
            Reserva(cuidadoresList[2], "23/10/2025", "09:00 AM", 3)
        )
    }
    // 2. Contador para generar IDs únicos
    var nextId by remember { mutableStateOf(4) }

    // 3. Funciones CRUD que modifican el estado (pasadas a ReservaScreen)
    val addReserva: (Reserva) -> Unit = { nuevaReserva ->
        // Asigna el siguiente ID antes de añadir
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
            .background(Color.White)) {
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
// 1️⃣ HomeUsuarioScreen
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
        HeaderUsuario()
        // 🔹 Saludo y búsqueda
        Text("Encuentra tu especialista", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* búsqueda */ },
            placeholder = { Text("Buscar por nombre, especialidad...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Categorías
        Text("Categorías", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Adultos", "Niños", "Nutrición", "Domicilio").forEach { categoria ->
                AssistChip(
                    onClick = { /* filtrar */ },
                    label = { Text(categoria) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = primaryColor
                    )
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))
        ServiciosGrid()
        Spacer(modifier = Modifier.height(24.dp))

// 🔹 Lista de especialistas
        Text("Especialistas disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)


        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Lista de especialistas
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
                            onClick = { /* Agendar */ },
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
            items(cuidadoresList) { cuidador ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White) // fondo limpio
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                onClick = { /* Agendar cita */ },
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

        // ** Calendario Básico Funcional **
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Simulación de citas programadas para la fecha seleccionada
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
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val primaryColor = Color(0xFF1E3A8A)
    val currentMonth = selectedDate.withDayOfMonth(1)
    val daysInMonth = currentMonth.lengthOfMonth()

    // El calendario comienza en Lunes para este ejemplo (1=Lunes, 7=Domingo)
    // Se ajusta para que la primera celda vacía sea el desfase.
    val firstDayOfWeek = currentMonth.dayOfWeek.value // 1 (Lunes) a 7 (Domingo)
    val startOffset = if (firstDayOfWeek == 7) 6 else firstDayOfWeek - 1 // Desfase para empezar en la posición correcta

    val dayLabels = DayOfWeek.entries.map {
        it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    // Cambiar la lista de días para que empiece en Lunes (si deseas Lunes-Domingo)
    // Para el ejemplo, mantendremos la semana tal como la devuelve DayOfWeek (Lunes a Domingo)
    // PERO si se requiere Domingo-Sábado:
    val dayLabelsFormatted = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Encabezado del Mes y Año
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

        // Días del mes (Grid)
        Column {
            var dayCount = 1
            for (week in 0 until 6) { // 6 semanas máx
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
                            // Celdas vacías del principio o final del mes
                            Spacer(modifier = Modifier.size(36.dp))
                        }
                    }
                }
                if (dayCount > daysInMonth) break
            }
        }
    }
}



@Composable
fun ChatScreen() {
    val primaryColor = Color(0xFF1E3A8A)
    val messages = remember { mutableStateListOf(
        Message("¡Bienvenido al chat! ¿En qué podemos ayudarte hoy?", false)
    )}

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservaScreen(
    reservas: List<Reserva>, // READ (Lectura)
    onAddReserva: (Reserva) -> Unit, // CREATE (Crear)
    onUpdateReserva: (Reserva) -> Unit, // UPDATE (Actualizar)
    onDeleteReserva: (Reserva) -> Unit // DELETE (Eliminar)
) {
    val primaryColor = Color(0xFF1E3A8A)
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedReserva by remember { mutableStateOf<Reserva?>(null) } // Para edición (UPDATE)

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis Reservas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            // Botón para CREAR una nueva reserva
            Button(
                onClick = {
                selectedReserva = null // Indica que es una operación de creación
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

        // READ: Lista de reservas
        LazyColumn {
            items(reservas, key = { it.id }) { reserva ->
                Card(modifier = Modifier
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
                        // Información de la reserva
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

                        // Botones de acción (UPDATE y DELETE)
                        Row {
                            // UPDATE: Abrir diálogo para edición
                            IconButton(onClick = {
                                selectedReserva = reserva
                                isDialogOpen = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                            }
                            // DELETE: Eliminar la reserva
                            IconButton(onClick = {
                                onDeleteReserva(reserva)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }


    if (isDialogOpen) {
        ReservaDialog(
            reserva = selectedReserva,
            onDismiss = { isDialogOpen = false },
            onSave = { cuidador, fecha, hora ->
                if (selectedReserva == null) {
                    // CREATE: id = 0 es temporal, se asigna en HomeApp
                    val newReserva = Reserva(cuidador, fecha, hora, 0)
                    onAddReserva(newReserva)
                } else {
                    // UPDATE: mantiene el id original
                    val updatedReserva = selectedReserva!!.copy(cuidador = cuidador, fecha = fecha, hora = hora)
                    onUpdateReserva(updatedReserva)
                }
                isDialogOpen = false
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaDialog(
    reserva: Reserva?,
    onDismiss: () -> Unit,
    onSave: (Cuidador, String, String) -> Unit
) {
    var selectedCuidador by remember {
        mutableStateOf(reserva?.cuidador ?: cuidadoresList.first())
    }
    // Inicialización de campos con fecha/hora actual o datos de la reserva
    var fechaText by remember { mutableStateOf(reserva?.fecha ?: LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))) }
    var horaText by remember { mutableStateOf(reserva?.hora ?: LocalTime.now().withSecond(0).withNano(0).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))) }
    val dialogTitle = if (reserva == null) "Crear Nueva Reserva" else "Editar Reserva"

    val primaryColor = Color(0xFF1E3A8A)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                // Selector de Cuidador
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
                // Campo de Fecha
                OutlinedTextField(
                    value = fechaText,
                    onValueChange = { fechaText = it },
                    label = { Text("Fecha (ej: 21/10/2025)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                // Campo de Hora
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

        // Icono de perfil o configuración
        IconButton(onClick = { /* Abrir perfil o ajustes */ }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Perfil",
                tint = primaryColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

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
                modifier = Modifier
                    .size(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
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