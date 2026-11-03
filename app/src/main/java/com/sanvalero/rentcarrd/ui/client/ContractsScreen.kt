package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Semantics
import androidx.compose.ui.Unit
import androidx.compose.ui.Unit.top
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.size
import androidx.compose.ui.unit.dp.dp
import androidx.compose.ui.unit.text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navGraph
import androidx.navigation.compose.navigation
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.ContractsViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import javax.inject.inject

/**
 * Pantalla Mis Contratos para clientes.
 * Muestra el historial de alquileres y mantenimientos del usuario.
 * Incluye compatibilidad de accesibilidad (TalkBack, alto contraste).
 */
@Composable
fun ContractsScreen(navController: NavController) {
    val viewModel: ContractsViewModel = viewModel()

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
            // Header
            contractsHeader()

            Spacer(height = 32.dp)

            // Filtros
            contractFilters(viewModel, navController)

            Spacer(height = 24.dp)

            // Lista de contratos
            if (viewModel.contracts.isEmpty()) {
                emptyState()
            } else {
                contractList(viewModel)
            }
        }

        // Semántics para mejorar la experiencia de TalkBack
        Semantics {
            // Marca el inicio de la lista de contratos
            list {
                // Cada ítem de contrato es seleccionable
                foreach {
                    // Announce the contract status
                    announceForAccessibility {
                        "Contrato ${it.contractNumber}, estado ${it.status}"
                    }
                }
            }
        }
    }
}

/** Encabezado de la pantalla Mis Contratos */
@Composable
private fun contractsHeader() {
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Accesible: título principal de la pantalla
            Semantics {
                announceForAccessibility("Pantalla Mis Contratos de RentCarRD")
            }
            Text(
                text = "Mis Contratos",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
                    // Tamaño de texto escalable
                    .semantics { size = textUnit(32, TextUnit.Sp) }
            )

            Text(
                text = "Historial de tus alquileres y servicios de mantenimiento",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
                    // Contraste adecuado
                    .semantics { mergeDescendants = false }
            )
        }
    }
}

/** Filtros de contratos */
@Composable
private fun contractFilters(
    viewModel: ContractsViewModel,
    navController: NavController
) {
    val isDark = RentCarRDSettings.isDarkMode()
    var selectedFilter by remember { mutableStateOf("all") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Filtro: Todos
        OutlinedButton(
            selected = selectedFilter == "all",
            onClick = { selectedFilter = "all"; viewModel.filterContracts("all") },
            // Accesible: botón con descripción semántica
            semantics = { desc(it) {
                "Filtro aplicar, Todos los contratos"
            }
            }
        ) {
            Text("Todos", modifier = Modifier.align(Alignment.Center))
                // Tamaño escalable
                .semantics { size = textUnit(17, TextUnit.Sp) }
        }

        // Filtro: Activos
        OutlinedButton(
            selected = selectedFilter == "active",
            onClick = { selectedFilter = "active"; viewModel.filterContracts("active") },
            semantics = { desc(it) {
                "Filtro aplicar, Contratos activos"
            }
            }
        ) {
            Text("Activos", modifier = Modifier.align(Alignment.Center))
                .semantics { size = textUnit(17, TextUnit.Sp) }
        }

        // Filtro: Completados
        OutlinedButton(
            selected = selectedFilter == "completed",
            onClick = { selectedFilter = "completed"; viewModel.filterContracts("completed") },
            semantics = { desc(it) {
                "Filtro aplicar, Contratos completados"
            }
            }
        ) {
            Text("Completados", modifier = Modifier.align(Alignment.Center))
                .semantics { size = textUnit(17, TextUnit.Sp) }
        }

        // Filtro: Cancelados
        OutlinedButton(
            selected = selectedFilter == "cancelled",
            onClick = { selectedFilter = "cancelled"; viewModel.filterContracts("cancelled") },
            semantics = { desc(it) {
                "Filtro aplicar, Contratos cancelados"
            }
            }
        ) {
            Text("Cancelados", modifier = Modifier.align(Alignment.Center))
                .semantics { size = textUnit(17, TextUnit.Sp) }
        }
    }
}

/** Lista de contratos */
@Composable
private fun contractList(viewModel: ContractsViewModel) {
    val isDark = RentCarRDSettings.isDarkMode()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = Padding(16.dp, 0.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(viewModel.contracts) { contract ->
            // Cada ítem de contrato está envuelto en semantics para TalkBack
            semantics {
                // El ítem es seleccionable con enfoque
                clickable {
                    // Announce when selected
                    announceForAccessibility(
                        "Contrato ${it.contractNumber}, ${it.contractType == "RENTAL" ? "alquiler" : "mantenimiento"}, ${it.status}"
                    )
                }
            }
            contractItem(contract, viewModel, navController)
        }
    }
}

/** Ítem de contrato */
@Composable
private fun contractItem(
    contract: ContractEntity,
    viewModel: ContractsViewModel,
    navController: NavController
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
        Modifier.clickable(onClick = {
            // Navegar a detalle del contrato
            navController.navigate("contract-detail", contractId = contract.id.toString())
        }) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Información principal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Datos del contrato
                    Column(
                        verticalArrangement = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Contrato #${contract.contractNumber}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.White : Color.Black,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                                // Accesible: anunciar el número del contrato
                                .semantics {
                                    mergeDescendants = false
                                    announceForAccessibility("Número de contrato ${contract.contractNumber}")
                                }
                        )

                        Text(
                            text = "${contract.contractType == "RENTAL" ? "Alquiler" : "Mantenimiento"} • ${contract.status}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                                // Accesible: describir el tipo y estado
                                .semantics {
                                    mergeDescendants = false
                                    announceForAccessibility(
                                        "Tipo ${contract.contractType == "RENTAL" ? "alquiler" : "mantenimiento"}, estado ${contract.status}"
                                    )
                                }
                        )
                    }

                    // Flecha derecha - accesible
                    Icon(
                        imageVector = Icons.Default.CHEVRON_RIGHT,
                        contentDescription = "Ver detalles del contrato",
                        tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
                            // Contraste adecuado
                            .semantics { mergeDescendants = false }
                    )
                }

                // Detalles adicionales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Vehículo - accesible
                    DetailItem(
                        label: "Vehículo:",
                        value: "${contract.vehicleId ?: 0 > 0 ? "Vehículo" : "N/A"}",
                        isDark: isDark
                            // Anunciar el estado del vehículo
                            .semantics {
                                mergeDescendants = false
                                if (contract.vehicleId != null) {
                                    announceForAccessibility("Vehículo disponible")
                                } else {
                                    announceForAccessibility("No hay vehículo asignado")
                                }
                            }
                    )
                    DetailItem(
                        label: "F. Inicio:",
                        value: contract.startDate,
                        isDark: isDark
                            // Anunciar la fecha de inicio
                            .semantics {
                                mergeDescendants = false
                                announceForAccessibility("Fecha de inicio ${contract.startDate}")
                            }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fecha de fin - accesible
                    DetailItem(
                        label: "F. Fin:",
                        value: contract.actualEndDate ?: contract.expectedEndDate,
                        isDark: isDark
                            // Anunciar la fecha de fin
                            .semantics {
                                mergeDescendants = false
                                announceForAccessibility(
                                    "Fecha de finalización ${contract.actualEndDate ?: contract.expectedEndDate}"
                                )
                            }
                    )
                    // Total - accesible
                    DetailItem(
                        label: "Total:",
                        value: "${RDFormatter.formatRD$(contract.totalAmount)}",
                        isDark: isDark
                            // Anunciar el monto total
                            .semantics {
                                mergeDescendants = false
                                announceForAccessibility(
                                    "Monto total ${RDFormatter.formatRD$(contract.totalAmount)} pesos dominicanos"
                                )
                            }
                    )
                }
            }
        }
    }
}

/** Ítem de detalle compacto - con accesibilidad */
@Composable
private fun DetailItem(
    label: String,
    value: String,
    isDark: Boolean,
    announceOnFocus: Boolean = false
) {
    Column(
        verticalArrangement = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
                // Accesible: semántica para lector de pantalla
                .semantics {
                    mergeDescendants = false
                    if (announceOnFocus) {
                        announceForAccessibility(label)
                    }
                }
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = if (isDark) Color(0xFFFFFFFF) else Color.Black,
            fontWeight = androidx.compose.ui.font.Weight.Medium
                // Accesible: valor descriptivo
                .semantics {
                    mergeDescendants = false
                    announceForAccessibility(value)
                }
        )
    }
}

/** Estado vacío - con accesibilidad */
@Composable
private fun emptyState() {
    val isDark = RentCarRDSettings.isDarkMode()

    Box(
        modifier = Modifier.fillMaxSize().padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.EXPAND_MORE,
            contentDescription = "Sin contratos",
            tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            size = 48.dp
                // Accesible: describir estado vacío
                .semantics {
                    mergeDescendants = false
                    announceForAccessibility("No tienes contratos, la lista está vacía")
                }
        )

        Text(
            text = "No tienes contratos",
            style = MaterialTheme.typography.headlineSmall,
            color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            textAlign = Alignment.Center
                // Tamaño escalable
                .semantics { size = textUnit(32, TextUnit.Sp) }
        )

        Text(
            text = "Tus alquileres o servicios aparecerán aquí",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) Color(0xFF666666) else Color(0xFF999999),
            textAlign = Alignment.Center
                // Contraste y tamaño escalable
                .semantics {
                    mergeDescendants = false
                    size = textUnit(20, TextUnit.Sp)
                }
        )
    }
}