package com.sanvalero.rentcarrd.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.OperationsViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import androidx.navigation.NavHostController
import javax.inject.inject

/**
 * Pantalla de operaciones para el operador de RentCarRD.
 * Permite gestionar entregas, devoluciones, diagnósticos y presupuestos.
 */
@Composable
fun OperationsScreen(navController: androidx.navigation.NavController) {
    val viewModel: OperationsViewModel = viewModel()

    val isDark = RentCarRDSettings.isDarkMode()

    Scaffold(
        appBarParameters = ScaffoldAppBarParameters(
            containerColor = if (isDark) Color(0xFF121212) else Color(0xFFFFFFFF)
        ),
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Sección: Gestión de Entregas y Devoluciones
            sectionCard(
                title = "Gestión de Alquileres",
                icon = Icons.Default.ARROW_CHEVRON_RIGHT,
                onClick = {
                    // TODO: Navegar o expandir sección de alquileres
                }
            ) {
                operationsGridItem(
                    icon = Icons.Default.DELIVERY_BIZ,
                    title = "Registro Entrega",
                    onClick = {
                        // TODO: Pantalla de registro de entrega
                    }
                )
                operationsGridItem(
                    icon = Icons.Default.REFRESH,
                    title = "Registro Devolución",
                    onClick = {
                        // TODO: Pantalla de registro de devolución
                    }
                )
            }

            Spacer(height = 24.dp)

            // Sección: Gestión de Taller
            sectionCard(
                title = "Gestión de Taller",
                icon = Icons.Default.ENGINE,
                onClick = {
                    // TODO: Expandir sección de taller
                }
            ) {
                // Botón: Nuevo Diagnóstico
                operationsGridItem(
                    icon = Icons.Default.DIAGNOSE,
                    title = "Nuevo Diagnóstico",
                    onClick = {
                        navController.navigate("maintenance-creation")
                    }
                )
                // Botón: Presupuesto Reparación
                operationsGridItem(
                    icon = Icons.Default.BUDGET,
                    title = "Presupuesto Reparación",
                    onClick = {
                        navController.navigate("maintenance-budget")
                    }
                )
                // Botón: Actualizar Estado
                operationsGridItem(
                    icon = Icons.Default.UPDATE,
                    title = "Actualizar Estado",
                    onClick = {
                        // Navegaría a pantalla de mantenimiento existente
                        // Por ahora, ejemplo con ID 1
                        navController.navigate("maintenance-details/{1}")
                    }
                )
            }

            Spacer(height = 24.dp)

            // Sección: Indicadores Rápidos
            Text(
                text = "Indicadores Rápidos",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Cards de indicadores rápidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                indicatorCard(
                    title = "Entregas Hoy",
                    value = "12",
                    icon = Icons.Default.SCHEDULE,
                    color = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
                )
                indicatorCard(
                    title = "Servicios Taller",
                    value = "8",
                    icon = Icons.Default.ENGINE,
                    color = if (isDark) Color(0xFF3F51B5) else Color(0xFF3F51B5)
                )
                indicatorCard(
                    title = "Ingresos Día",
                    value = "RD$ 2,500",
                    icon = Icons.Default.MONETIZATION_ON,
                    color = if (isDark) Color(0xFF009688) else Color(0xFF009688)
                )
            }
        }
    }
}

/** Composable auxiliar para sección con tarjeta */
@Composable
private fun sectionCard(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (RentCarRDSettings.isDarkMode()) Color(0xFF2C2C2C) else Color(0xFFFAFAFA),
            pressed = if (RentCarRDSettings.isDarkMode()) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
            selected = if (RentCarRDSettings.isDarkMode()) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (RentCarRDSettings.isDarkMode()) Color(0xFFB0B0B0) else Color(0xFF505050),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (RentCarRDSettings.isDarkMode()) Color(0xFFB0B0B0) else Color(0xFF505050)
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.CHEVRON_RIGHT,
                    contentDescription = "Más opciones",
                    tint = if (RentCarRDSettings.isDarkMode()) Color(0xFFB0B0B0) else Color(0xFF505050)
                )
            }

            // Contenido expandido
            content()
        }
    }
}

/** Composable auxiliar para ítem de grid de operaciones */
@Composable
private fun operationsGridItem(
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    val isDark = RentCarRDSettings.isDarkMode()

    Card(
        modifier = Modifier.fillMaxWidth().padding(2.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF222222) else Color(0xFFFFFFFF),
            pressed = if (isDark) Color(0xFF333333) else Color(0xffe0e0e0),
            selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
        )
    ) {
        Modifier.clickable(onClick = onClick) {
            // Efecto visual al hacer clic
        } {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.White : Color.Black,
                    textAlign = Alignment.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/** Composable auxiliar para tarjeta de indicador */
@Composable
private fun indicatorCard(
    title: String,
    value: String,
    icon: Int,
    color: Integer
) {
    val isDark = RentCarRDSettings.isDarkMode()

    Card(
        modifier = Modifier.fillMaxWidth(0.45f).padding(2.dp),
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
            horizontalAlignment = Alignment.Start
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
                fontWeight = androidx.compose.ui.font.Weight.Medium
            )
        }
    }
}