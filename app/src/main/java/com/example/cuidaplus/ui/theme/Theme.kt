package com.example.cuidaplus.ui.theme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

// ✅ Importa los colores definidos en Color.kt
import com.example.cuidaplus.ui.theme.BluePrimary
import com.example.cuidaplus.ui.theme.BlueSecondary
import com.example.cuidaplus.ui.theme.BlueLight
import com.example.cuidaplus.ui.theme.BlueDark
import com.example.cuidaplus.ui.theme.AccentRed

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AccentRed
)

private val DarkColors = darkColorScheme(
    primary = BlueLight,
    secondary = BlueSecondary,
    background = BlueDark,
    surface = BlueDark,
    onPrimary = BlueDark,
    onSecondary = BlueDark,
    onBackground = Color.White,
    onSurface = Color.White,
    error = AccentRed
)

@Composable
fun CuidaplusTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = CuidaplusTypography,
        content = content
    )
}
