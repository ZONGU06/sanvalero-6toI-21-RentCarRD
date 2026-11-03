package com.sanvalero.rentcarrd.util

import androidx.compose.runtime.State
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Formatter para formatear valores en Pesos Dominicanos (RD$).
 * Formato: RD$ 1,234.56
 */
object RDFormatter {

    private val decimalFormat = DecimalFormat("#,##0.00")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-DO"))

    /**
     * Formatea un monto Double en Pesos Dominicanos.
     * @param amount Monto a formatear
     * @return Cadena formateada (ej: "RD$ 1,234.56")
     */
    fun formatRD$(amount: Double): String {
        return "RD$ ${decimalFormat.format(amount)}"
    }

    /**
     * Formatea un monto Double sin el prefijo RD$.
     * @amount Monto a formatear
     * @return Cadena formateada (ej: "1,234.56")
     */
    fun formatWithoutPrefix(amount: Double): String {
        return decimalFormat.format(amount)
    }

    /**
     * Formatea una fecha en formato dd/MM/yyyy.
     * @param date Date object to format
     * @return Cadena formateada (ej: "25/12/2024")
     */
    fun formatDate(date: java.util.Date?): String {
        return date != null ? dateFormat.format(date) : ""
    }

    /**
     * Formatea una fecha en formato dd/MM/yyyy.
     * @param calendar Calendar object to format
     * @return Cadena formateada (ej: "25/12/2024")
     */
    fun formatDate(calendar: java.util.Calendar?): String {
        return calendar != null ? dateFormat.format(calendar.time) : ""
    }

    /**
     * Convierte una cadena en objeto Date.
     * @param dateStr Cadena en formato dd/MM/yyyy
     * @return Objeto Date o null si el formato es inválido
     */
    fun parseDate(dateStr: String?): java.util.Date? {
        return dateStr?.let {
            try {
                dateFormat.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Convierte una cadena en objeto Calendar.
     * @param dateStr Cadena en formato dd/MM/yyyy
     * @return Objeto Calendar o null si el formato es inválido
     */
    fun parseDateToCalendar(dateStr: String?): java.util.Calendar? {
        return dateStr?.let {
            try {
                val date = dateFormat.parse(it)
                val calendar = java.util.Calendar.getInstance()
                calendar.time = date
                return calendar
            } catch (e: Exception) {
                null
            }
        }
    }
}

/**
 * Formatter para formatear cantidades con separadores de miles.
 */
object NumberFormatter {

    private val decimalFormat = DecimalFormat("#,##0.###")

    /** Formatea un número con separadores de miles. */
    fun format(number: Double): String {
        return decimalFormat.format(number)
    }

    /** Formatea un número entero. */
    fun formatInt(number: Int): String {
        return decimalFormat.format(number.toDouble())
    }
}

/**
 * Formatter para validaciones de entrada.
 */
object InputValidator {

    /** Verifica si una cadena es un número válido. */
    fun isValidNumber(input: String): Boolean {
        return input.isNotEmpty() && input.all { it.isDigit() || it == '.' || it == ',' }
    }

    /** Verifica si una cadena tiene un formato de fecha válido (dd/MM/yyyy). */
    fun isValidDate(input: String): Boolean {
        return input.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))
    }

    /** Elimina caracteres no numéricos de una cadena. */
    fun cleanNumericInput(input: String): String {
        return input.filter { it.isDigit() || it == '.' || it == ',' }
    }
}