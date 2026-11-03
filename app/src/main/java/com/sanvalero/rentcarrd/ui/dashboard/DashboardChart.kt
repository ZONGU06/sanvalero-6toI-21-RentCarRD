package com.sanvalero.rentcarrd.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings

/**
 * Componente de gráfico de rendimiento usando Canvas nativo de Compose.
 * Muestra un gráfico de barras con indicadores de rendimiento del dashboard.
 */
@Composable
fun DashboardChart(
    totalVehicles: Int,
    activeContracts: Int,
    totalRevenue: Double,
    topVehicles: List<String>,
    topServices: List<String>
) {
    val isDark = RentCarRDSettings.isDarkMode()
    val chartWidth = 350.dp
    val chartHeight = 200.dp

    // Datos para los gráficos (simulados - en producción vendrían del ViewModel)
    val revenueData = generateRevenueData()
    val vehicleData = generateVehicleData()

    Scaffold(
        // Contenedorno del gráfico
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Indicadores de Rendimiento",
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (isDark) Color.White : Color.Black
                    )
                },
                supportingIcon = {
                    Icon(
                        imageVector = Icons.Default.PIE_CHART,
                        contentDescription = "Gráfico de rendimiento",
                        tint = if (isDark) Color(0xFFD32F2F) : Color(0xFFD32F2F)
                    )
                },
                colors = TopAppBarColors(
                    containerColor = if (isDark) Color(0xFF121212) else Color(0xFFFFFFFF)
                )
            )
        }
    ) { padding ->
        BoxWithConstraints(
            maxWidth = 400.dp,
            minWidth = 300.dp,
            maxHeight = 500.dp,
            minHeight = 350.dp
        ) { width, height ->
            // Canvas para dibujar gráficos personalizados
            Canvas(
                modifier = Modifier
                    .size(if (width < 400.dp) width - 40.dp else chartWidth,
                        if (height < 400.dp) height - 80.dp else chartHeight)
                    .background(
                        if (isDark) Color(0xFF1E1E1E) else Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(padding)
            ) {
                drawBackground(isDark)
                drawAxisLabels(isDark, width, height)
                drawBarChart(isDark, width, height, revenueData)
                drawTopVehiclesList(topVehicles, isDark)
                drawTopServicesList(topServices, isDark)
            }
        }
    }
}

/** Genera datos de ejemplo de ingresos para el gráfico */
private fun generateRevenueData(): List<RevenueEntry> {
    return listOf(
        RevenueEntry("Ene", 12500.0),
        RevenueEntry("Feb", 14200.0),
        RevenueEntry("Mar", 11800.0),
        RevenueEntry("Abr", 13500.0),
        RevenueEntry("May", 15200.0),
        RevenueEntry("Jun", 16800.0)
    )
}

/** Genera datos de ejemplo de vehículos para el gráfico */
private fun generateVehicleData(): List<VehicleEntry> {
    return listOf(
        VehicleEntry("Disponibles", 35, Color(0xFF009688)),
        VehicleEntry("Alquilados", 23, Color(0xFFD32F2F)),
        VehicleEntry("Mantenimiento", 8, Color(0xFF3F51B5))
    )
}

/** Datos para gráfico de ingresos */
data class RevenueEntry(
    val month: String,
    val amount: Double
)

/** Datos para gráfico de vehículos */
data class VehicleEntry(
    val label: String,
    val count: Int,
    val color: Int
)

/** Fondo del canvas */
private fun drawBackground(isDark: Boolean) {
    val bgColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFFFFFFF)
    drawRect(
        color = bgColor,
        topStart = Offset(0, 0),
        end = Offset(it.width, it.height)
    )
}

/** Ejes y etiquetas */
private fun drawAxisLabels(isDark: Boolean, width: Float, height: Float) {
    val paint = Paint().apply {
        textSize = 12f
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
        isAntiAlias = true
    }

    // Eje X - meses
    val months = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun")
    val stepX = (it.width - 60) / (months.size - 1).toFloat()

    for ((index, month) in months.withIndex()) {
        val x = 30 + index * stepX
        // Línea del eje
        paint.strokeWidth = 1f
        paint.color = if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0)
        paint.style = Paint.Style.STROKE
        drawLine(
            start = Offset(x, it.height - 40),
            end = Offset(x, it.height - 50),
            paint = paint
        )
        // Etiqueta
        drawText(
            text = month,
            x = x - paint.measureText(month) / 2f,
            y = it.height - 35f,
            paint = paint
        )
    }

    // Eje Y - ingresos
    val paintY = Paint().apply {
        textSize = 12f
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
        isAntiAlias = true
    }

    // Marcar cada 5000
    val maxY = 20000f
    val stepY = (it.height - 80) / 4f
    for (i in 0..4) {
        val y = it.height - 40 - i * stepY
        // Línea
        paintY.strokeWidth = 1f
        paintY.color = if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0)
        paintY.style = Paint.Style.STROKE
        drawLine(
            start = Offset(30, y),
            end = Offset(it.width - 30, y),
            paint = paintY
        )
        // Valor
        val value = (maxY - (i * maxY / 4)).toInt()
        drawText(
            text = RDFormatter.formatRD$(value).replace("RD$ ", ""),
            x = 15f,
            y = y + 5f,
            paint = paintY
        )
    }
}

/** Dibuja el gráfico de barras de ingresos */
private fun drawBarChart(
    isDark: Boolean,
    width: Float,
    height: Float,
    data: List<RevenueEntry>
) {
    val barColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
    val barPaint = Paint().apply {
        color = barColor
        isAntiAlias = true
    }

    val maxValue = data.maxByOrNull { it.amount }?.amount ?: 1.0
    val stepHeight = (it.height - 80) / maxValue
    val startX = 30f
    val barWidth = ((it.width - 80) / data.size) / 3f // Espaciado para múltiples barras

    for ((index, entry) in data.withIndex()) {
        val x = startX + index * barWidth * 3 + barWidth
        val barHeight = entry.amount * stepHeight
        val y = it.height - 40 - barHeight

        // Barra
        drawRect(
            color = barColor.copy(alpha = 0.8f),
            topStart = Offset(x, y),
            end = Offset(x + barWidth, it.height - 40)
        )

        // Etiqueta del mes
        val monthPaint = Paint().apply {
            textSize = 11f
            color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
            isAntiAlias = true
        }
        drawText(
            text = entry.month,
            x = x + barWidth / 2f - monthPaint.measureText(entry.month) / 2f,
            y = it.height - 25f,
            paint = monthPaint
        )

        // Valor de la barra
        val valuePaint = Paint().apply {
            textSize = 11f
            color = if (isDark) Color(0xFFFFFFFF) else Color.Black
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        drawText(
            text = "${RDFormatter.formatRD$(entry.amount).replace("RD$ ", "")}",
            x = x + barWidth / 2f - valuePaint.measureText("${RDFormatter.formatRD$(entry.amount).replace("RD$ ", "")}") / 2f,
            y = y - 10f,
            paint = valuePaint
        )
    }
}

/** Lista de vehículos destacados */
private fun drawTopVehiclesList(topVehicles: List<String>, isDark: Boolean) {
    val paint = Paint().apply {
        textSize = 12f
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
        isAntiAlias = true
    }

    val startY = 30f
    var currentY = startY

    for (vehicle in topVehicles) {
        drawText(
            text = "• $vehicle",
            x = 50f,
            y = currentY,
            paint = paint
        )
        currentY += 25f
    }
}

/** Lista de servicios destacados */
private fun drawTopServicesList(topServices: List<String>, isDark: Boolean) {
    val paint = Paint().apply {
        textSize = 12f
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
        isAntiAlias = true
    }

    val startY = it.height - 180f
    var currentY = startY

    for (service in topServices) {
        drawText(
            text = "• $service",
            x = it.width - 30f - paint.measureText(service),
            y = currentY,
            paint = paint
        )
        currentY += 20f
    }
}