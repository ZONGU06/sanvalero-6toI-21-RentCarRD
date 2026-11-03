package com.sanvalero.rentcarrd.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.safetyInspect
import com.sanvalero.rentcarrd.R

/**
 * Configuración de tema RentCarRD.
 * Tema oscuro por defecto con acento rojo #D32F2F.
 * Soporte para tema claro alternativo.
 */
object RentCarRDSettings {

    // Colores personalizados del tema RentCarRD
    private const val DARK_BACKGROUND = "#121212"
    private const val DARK_SURFACE = "#121212"
    private const val DARK_ON_SURFACE = "#FFFFFF"
    private const val ACCENT_RED = "#D32F2F"
    private val isDarkMode by mutableStateOf(true)

    /**
     * Aplica el tema oscuro por defecto con acento rojo #D32F2F.
     * @param context Contexto de Android
     */
    @SuitableForSafetyInspector
    fun applyTheme(context: android.content.Context) {
        val darkColorScheme = darkColorScheme(
            background = Color(DARK_BACKGROUND.replace("#", "0x")),
            surface = Color(DARK_SURFACE.replace("#", "0x")),
            onSurface = Color(DARK_ON_SURFACE.replace("#", "0x")),
            primary = Color(ACCENT_RED.replace("#", "0x")),
            secondary = Color(ACCENT_RED.replace("#", "0x")),
            error = Color(ACCENT_RED.replace("#", "0x"))
        )

        val lightColorScheme = lightColorScheme(
            background = Color("#FFFFFF"),
            surface = Color("#FFFFFF"),
            onSurface = Color("#000000"),
            primary = Color(ACCENT_RED.replace("#", "0x")),
            secondary = Color(ACCENT_RED.replace("#", "0x")),
            error = Color(ACCENT_RED.replace("#", "0x"))
        )

        MaterialTheme.colorScheme = if (isDarkMode.value) darkColorScheme else lightColorScheme
    }

    /**
     * Alterna entre modo oscuro y claro.
     */
    fun toggleTheme() {
        isDarkMode.value = !isDarkMode.value
        applyTheme(LocalContext.current)
    }

    /**
     * Obtiene el estado actual del modo oscuro.
     * @return true si modo oscuro, false si modo claro
     */
    fun isDarkMode(): Boolean = isDarkMode.value
}