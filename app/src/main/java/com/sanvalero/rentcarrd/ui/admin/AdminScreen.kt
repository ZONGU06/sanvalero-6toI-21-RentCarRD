package com.sanvalero.rentcarrd.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.AdminViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import javax.inject.inject

/**
 * Pantalla de Administración para RentCarRD.
 * Acceso completo al dashboard gerencial con CRUD de vehículos, clientes, operadores y tarifas.
 */
@Composable
fun AdminScreen(navController: androidx.navigation.NavController) {
    val viewModel: AdminViewModel = viewModel()

    val isDark = RentCarRDSettings.isDarkMode()

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

            // Sección: CRUD de Vehículos
            adminSection(
                title = "Catálogo de Vehículos",
                icon = Icons.Default.AUTOMOTIVE,
                color = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
            ) {
                adminCrudGrid()
            }

            Spacer(height = 24.dp)

            // Sección: CRUD de Clientes
            adminSection(
                title = "Gestión de Clientes",
                icon = Icons.Default.PERSON,
                color = if (isDark) Color(0xFF3F51B5) else Color(0xFF3F51B5)
            ) {
                clientManagementGrid()
            }

            Spacer(height = 24.dp)

            // Sección: CRUD de Operadores
            adminSection(
                title = "Gestión de Operadores",
                icon = Icons.Default.BUSINESS,
                color = if (isDark) Color(0xFF009688) else Color(0xFF009688)
            ) {
                operatorManagementGrid()
            }

            Spacer(height = 24.dp)

            // Sección: Configuración de Tarifas
            adminSection(
                title = "Configuración de Tarifas",
                icon = Icons.Default.SETTINGS,
                color = if (isDark) Color(0xFF8BC34A) else Color(0xFF8BC34A)
            ) {
                tariffConfiguration()
            }

            Spacer(height = 24.dp)

            // Sección: Reportes
            adminSection(
                title = "Reportes y Exportación",
                icon = Icons.Default.INSERT_CHART,
                color = if (isDark) Color(0xFFFFC107) else Color(0xFF FF C107)
            ) {
                reportOptions()
            }
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

/** Composable auxiliar para sección de admin */
@Composable
private fun adminSection(
    title: String,
    icon: Int,
    color: Integer,
    content: @Composable () -> Unit
) {
    val isDark = RentCarRDSettings.isDarkMode()

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFFAFAFA),
            pressed = if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
            selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
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
                    tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.CHEVRON_RIGHT,
                    contentDescription = "Expandir",
                    tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
                )
            }

            // Espacio para contenido expandido
            content()
        }
    }
}

/** Grid de opciones CRUD para vehículos */
@Composable
private fun adminCrudGrid() {
    val isDark = RentCarRDSettings.isDarkMode()

    val gridItems = listOf(
        ("Agregar Vehículo", Icons.Default.ADD),
        ("Editar Vehículo", Icons.Default.EDIT),
        ("Eliminar Vehículo", Icons.Default.DELETE),
        ("Ver Historial", Icons.Default.HISTORY)
    )

    GridRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gridItems) { (title, icon) ->
            Card(
                modifier = Modifier.weight(1f).padding(2.dp),
                elevation = 2.dp,
                colors = CardColors(
                    defaultColor = if (isDark) Color(0xFF222222) else Color(0xFFFFFFFF),
                    pressed = if (isDark) Color(0xFF333333) else Color(0xffe0e0e0),
                    selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
                )
            ) {
                Modifier.clickable {
                    // TODO: Manejar clic en opción CRUD
                } {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterStart
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
    }
}

/** Grid de gestión de clientes */
@Composable
private fun clientManagementGrid() {
    // Similar structure for client management
    Text(
        text = "Gestión de Clientes - Próximamente",
        style = MaterialTheme.typography.bodyMedium,
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
        modifier = Modifier.padding(16.dp),
        textAlign = Alignment.Center
    )
}

/** Grid de gestión de operadores */
@Composable
private fun operatorManagementGrid() {
    // Similar structure for operator management
    Text(
        text = "Gestión de Operadores - Próximamente",
        style = MaterialTheme.typography.bodyMedium,
        color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
        modifier = Modifier.padding(16.dp),
        textAlign = Alignment.Center
    )
}

/** Configuración de tarifas */
@Composable
private fun tariffConfiguration() {
    val isDark = RentCarRDSettings.isDarkMode()

    OutlinedTextField(
        value = "",
        onValueChange = { },
        label = { Text("Tarifa mora (%)") },
        placeholder = { Text("Ej: 5.00") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            capitalization = KeyboardCapitalization.NONE,
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    OutlinedTextField(
        value = "",
        onValueChange = { },
        label = { Text("Porcentaje descuento") },
        placeholder = { Text("Ej: 10.00") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            capitalization = KeyboardCapitalization.NONE,
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    Button(
        onClick = {
            // TODO: Guardar configuración de tarifas
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
        )
    ) {
        Text("Guardar Configuración", modifier = Modifier.align(Alignment.Center))
    }
}

/** Opciones de reportes */
@Composable
private fun reportOptions() {
    val isDark = RentCarRDSettings.isDarkMode()

    val reportOptions = listOf(
        ("Listado General", Icons.Default.LIST),
        ("Reporte por Período", Icons.Default.DATE_RANGE),
        ("Resumen Gerencial", Icons.Default.PIE_CHART)
    )

    GridRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(reportOptions) { (title, icon) ->
            Card(
                modifier = Modifier.weight(1f).padding(2.dp),
                elevation = 2.dp,
                colors = CardColors(
                    defaultColor = if (isDark) Color(0xFF222222) else Color(0xFFFFFFFF),
                    pressed = if (isDark) Color(0xFF333333) else Color(0xffe0e0e0),
                    selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
                )
            ) {
                Modifier.clickable {
                    // TODO: Manejar selección de reporte
                } {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterStart
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
    }
}