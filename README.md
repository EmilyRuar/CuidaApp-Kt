💙 Cuidaplus App – Kotlin + Jetpack Compose
<p align="center"> <img src="https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin" /> <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-green?logo=android" /> <img src="https://img.shields.io/badge/Architecture-MVVM-orange" /> <img src="https://img.shields.io/badge/Status-In%20Progress-yellow" /> </p>
🐾 CuidaPlus: Tu app para conectar con especialistas y servicios de cuidado

Cuidaplus es una aplicación móvil desarrollada en Kotlin utilizando Jetpack Compose, diseñada para facilitar la búsqueda de especialistas, agendamiento de citas y gestión de servicios para el cuidado de tus seres queridos (personas mayores, pacientes, mascotas u otros).

✨ Características principales
✅ Navegación moderna

Uso de Navigation Compose

Arquitectura limpia con NavGraph y BottomNavigation

👩‍⚕️ Pantalla de Especialistas

Lista de especialistas con:

Foto

Especialidad

Valoración

Botón para agendar cita

📅 Agenda

Vista para revisar citas programadas

Componentes modulares en ui/agenda/components

💬 Chat

Interfaz inicial para mensajería entre paciente y especialista

🎨 Tema personalizado

Implementación completa en Material 3

Paleta de colores propia

Tipografía profesional

🧱 Arquitectura MVVM

Repositorios separados

ViewModels desacoplados

Estructura escalable

🏛 Estructura del Proyecto
com.example.cuidaplus
│
├── data/                   # Modelos y listas locales
│
├── navigation/             # NavGraph y navegación inferior
│   ├── BottomNavItem.kt
│   └── NavGraph.kt
│
├── repository/             # Repositorios (Auth, Pacientes, Servicios)
│
├── ui/
│   ├── agenda/
│   │   └── components/     # UI modular de Agenda
│   │       └── AgendaScreen.kt
│   ├── chat/
│   │   └── ChatScreen.kt
│   ├── especialistas/
│   │   └── EspecialistasScreen.kt
│   ├── home/
│   ├── reserva/
│   └── theme/              # Tema Material 3
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── util/                   # Helpers y extensiones
│
├── viewmodel/              # ViewModels según módulo
│
└── MainActivity.kt         # Punto de entrada con Compose

📱 Tecnologías utilizadas
Tecnología	Uso
Kotlin	Lógica y arquitectura
Jetpack Compose	UI declarativa moderna
Material 3	Tema visual elegante
Navigation Compose	Navegación estructurada
MVVM	Arquitectura limpia y escalable
🚀 Instalación y ejecución
1️⃣ Clonar el repositorio
git clone https://github.com/EmilyRuar/CuidaApp-Kt.git

2️⃣ Abrir en Android Studio

Abrir Android Studio

Seleccionar Open an existing project

Buscar la carpeta Cuidaplus

3️⃣ Ejecutar

Seleccionar un emulador o dispositivo físico

Presionar ▶️ Run

🔧 Actualizar tu proyecto en GitHub
Subir cambios:
git add .
git commit -m "Actualización del proyecto Cuidaplus"
git push origin main

🛠 Próximas mejoras

🔐 Pantalla de login + autenticación real

📍 Ubicación y geolocalización de especialistas

📆 Sistema avanzado de reservas

🔔 Notificaciones push

👤 Autora

Emily Rupay
Community Manager & Mobile Developer
📍 Chile
✨ Construyendo experiencias móviles con Kotlin + Compose
