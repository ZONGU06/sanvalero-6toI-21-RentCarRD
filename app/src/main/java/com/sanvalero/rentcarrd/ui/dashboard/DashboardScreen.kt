package com.sanvalero.rentcarrd.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.ui.dashboard.DashboardChart
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.DashboardViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import javax.inject.inject

/**
 * Pantalla Dashboard para el gerente/admin de RentCarRD.
 * Muestra indicadores gerenciales, gráficos y estadísticas principales.
 */
@Composable
fun DashboardScreen(navController: androidx.navigation.NavController) {
    val viewModel: DashboardViewModel = viewModel()

    val isDark = RentCarRDSettings.isDarkMode()

    // Datos para el gráfico (obtenidos del ViewModel o estado actual)
    val totalVehicles = viewModel.totalVehicles.value
    val activeContracts = viewModel.totalActiveContracts.value
    val totalRevenue = viewModel.totalRevenue.value
    val topVehicles = listOf(
        "Toyota Corolla (12 alquilados)",
        "Ford Focus (8 alquilados)",
        "Chevrolet Cruze (10 alquilados)",
        "Renault Duster (6 alquilados)"
    )
    val topServices = listOf(
        "Diagnóstico Eléctrico",
        "Mantenimiento Preventivo",
        "Cambio de Aceite",
        "Reparación de Frenos"
    )

    Scaffold(
        appBarParameters = ScaffoldAppBarParameters(
            containerColor = if (isDark) Color(0xFF121212) else Color(0xFFFFFFFF)
        ),
        bottomBar = BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            // TODO: Cerrar sesión de administrador
                        },
                        modifier = Modifier padding (8.dp)
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        )
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Header bienvenido
            welcomeHeader()

            Spacer(height = 32.dp)

            // Sección de indicadores principales - diseño en grid
            // Row de tarjetas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Tarjeta: Total de vehículos
                cards(
                    title = "Vehículos Totales",
                    value = "$totalVehicles",
                    icon = Icons.Default.CAR,
                    color = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
                )

                // Tarjeta: Contratos activos
                cards(
                    title = "Contratos Activos",
                    value = "$activeContracts",
                    icon = Icons.Default.CONTRACT,
                    color = if (isDark) Color(0xFF3F51B5) else Color(0xFF3F51B5)
                )

                // Tarjeta: Pagos totales
                cards(
                    title = "Pagos Totales",
                    value = "${viewModel.totalPayments.value}",
                    icon = Icons.Default.PAYMENTS,
                    color = if (isDark) Color(0xFF009688) else Color(0xFF009688)
                )

                // Tarjeta: Ingresos totales
                cards(
                    title = "Ingresos Totales",
                    value = "${RDFormatter.formatRD$(totalRevenue)}",
                    icon = Icons.Default.MONETIZATION_ON,
                    color = if (isDark) Color(0xFF009688) else Color(0xFF009688)
                )
            }

            Spacer(height = 32.dp)

            // Sección de gráficos (Canvas de Compose)
            Text(
                text = "Indicadores Gerenciales",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Componente de gráfico Canvas
            DashboardChart(
                totalVehicles = totalVehicles,
                activeContracts = activeContracts,
                totalRevenue = totalRevenue,
                topVehicles = topVehicles,
                topServices = topServices
            )
        }
    }
}

/** Composable auxiliar para encabezado de bienvenida */
@Composable
private fun welcomeHeader() {
    val isDark = RentCarRDSettings.isDarkMode()

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 4.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
            pressed = if (isDark) Color(0xFF2C2C2C) else Color(0xFFE0E0E0),
            selected = if (isDark) Color(0xFF3A3A3A) else Color(0xFFBBDEFB)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Bienvenido, Administrador",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "RentCarRD - Panel de Control",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
            )
        }
    }
}

/** Composable auxiliar para sección de tarjetas de indicadores */
@Composable
private fun cards(
    title: String,
    value: String,
    icon: Int,
    color: Integer
) {
    val isDark = RentCarRDSettings.isDarkMode()

    Card(
        modifier = Modifier.fillMaxWidth(0.45f).padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFFAFAFA),
            pressed = if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
            selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = if (isDark) Color.White : Color.Black,
                fontWeight = androidx.compose.ui.font.Weight.Normal
            )
        }
    }
}