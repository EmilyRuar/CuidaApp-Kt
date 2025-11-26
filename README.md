# 🚑 **Cuidaplus — App de Enfermería y Cuidados a Domicilio**
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Status](https://img.shields.io/badge/STATUS-En%20desarrollo-yellow?style=for-the-badge)

---

## 📱 **Descripción del Proyecto**
**Cuidaplus** es una aplicación móvil construida en **Kotlin + Jetpack Compose** diseñada para conectar a usuarios con **enfermeras, especialistas y cuidadoras a domicilio**.

El objetivo del proyecto es ofrecer una forma rápida, segura y fácil de acceder a servicios de salud a domicilio.

---

## 🌟 **Características Principales**

### 🧭 Navegación Moderna
App organizada con **Navigation Compose** en distintas pantallas:
- 🏠 Home  
- 👩‍⚕️ Especialistas  
- 💬 Chat inteligente  
- 📅 Agenda  
- 📝 Reservas

---

### 💬 Chat Inteligente Automático
El chat incluye:
- Respuestas predefinidas sobre los servicios  
- Menús con preguntas frecuentes  
- Interacciones guiadas para ayudar al usuario  
- Flujo natural para resolver dudas sobre Cuidaplus  

---

### 👩‍⚕️ Vista de Especialistas
Muestra un listado limpio con:
- Fotografía  
- Nombre y especialidad  
- Valoración  
- Botón **"Agendar Cita"**  

---

### 🎨 Tema Personalizado (Material 3)
Incluye:
- Paleta de colores corporativa (azules + tonos cálidos)  
- Tipografías personalizadas  
- Light & Dark Theme  

---

## 📂 Estructura del proyecto

```plaintext
com.example.cuidaplus
│
├── data
│   └── modelos, listas y fuentes de datos
│
├── navigation
│   └── NavGraph de la aplicación
│
├── repository
│   └── lógica de acceso a datos
│
├── ui
│   ├── agenda
│   ├── chat
│   ├── especialistas
│   ├── home
│   ├── reserva
│   └── theme (colores, typography, shapes)
│
└── viewmodel

Patrón utilizado:
- **MVVM (Model - View - ViewModel)**  
- State Hoisting  
- Repositorios como fuente única de datos  

---

## 🛠️ **Tecnologías Utilizadas**

- **Kotlin**  
- **Jetpack Compose**  
- **Material 3**  
- **Navigation Compose**  
- **State Management** (`remember`, `mutableStateOf`, `ViewModel`)  

---

## 🚀 **Cómo Ejecutar el Proyecto**

1. Clonar el repositorio:

```bash
git clone https://github.com/EmilyRuar/CuidaApp-Kt.git
