package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.domain.Model.Contract
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de detalle de contrato para clientes.
 * Muestra la información completa del contrato y permite acciones
 * como extender el alquiler o cancelarlo si aún no ha comenzado.
 */
@Composable
fun ContractDetailScreen(
    contractId: String,
    navController: androidx.navigation.NavController
) {
    val viewModel: ContractDetailViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    // Cargar contrato por ID
    launch {
        viewModel.loadContract(contractId.toInt())
    }

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
            // Header con información del contrato
            contractHeader(viewModel.contract!!)

            Spacer(height = 32.dp)

            // Información detallada
            detailSections(viewModel)

            Spacer(height = 24.dp)

            // Acciones disponibles
            actionButtons(viewModel, navController)
        }
    }
}

/** Cargar contrato - placeholder */
private fun loadContract(contractId: Int) {
    // En producción consultaría la base de datos
}

/** Encabezado de información del contrato */
@Composable
private fun contractHeader(contract: ContractEntity) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contrato #${contract.contractNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) Color.White : Color.Black
                )
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Cerrar")
                }
            }

            // Tipo y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${contract.contractType == "RENTAL" ? "Alquiler" : "Mantenimiento"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
                )
                Spacer(Modifier.width(16.dp))
                // Indicador de estado
                Container(
                    width = 80,
                    height = 20,
                    background = if (contract.status == "ACTIVE") {
                        if (isDark) Color(0xFF009688) else Color(0xFF4CAF50)
                    } else if (contract.status == "COMPLETED") {
                        if (isDark) Color(0xFF3F51B5) else Color(0xFF9C27B0)
                    } else {
                        if (isDark) Color(0xFFD32F2F) else Color(0xFFf44336)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = contract.status,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White : Color.Black,
                        textAlign = Alignment.Center
                    )
                }
            }

            // Fechas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailItem(label: "F. Inicio:", value: contract.startDate, isDark: isDark)
                DetailItem(label: "F. Fin:", value: contract.actualEndDate ?: contract.expectedEndDate, isDark: isDark)
            }

            // Montos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailItem(label: "Base:", value: "${RDFormatter.formatRD$(contract.baseAmount)}", isDark: isDark)
                DetailItem(label: "Total:", value: "${RDFormatter.formatRD$(contract.totalAmount)}", isDark: isDark)
            }
        }
    }
}

/** Secciones de detalles */
@Composable
private fun detailSections(viewModel: ContractDetailViewModel) {
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
            Text(
                text = "Información del Vehículo",
                style = MaterialTheme.typography.titleSmall,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (viewModel.vehicle != null) {
                val v = viewModel.vehicle!!
                DetailRowCompact(label: "Marca y Modelo:", value: "${v.brand} ${v.model}", isDark: isDark)
                DetailRowCompact(label: "Tarifa Diaria:", value: "RD$ ${v.dailyRate}", isDark: isDark)
                DetailRowCompact(label: "Categoría:", value: v.category, isDark: isDark)
                DetailRowCompact(label: "Estado:", value: v.status, isDark: isDark)
            } else {
                Text(text = "Información no disponible", isDark: isDark)
            }
        }
    }

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
            Text(
                text = "Información del Cliente",
                style = MaterialTheme.typography.titleSmall,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Placeholder - en producción traería datos del cliente
            DetailRowCompact(label: "Nombre:", value: "Por consultar", isDark: isDark)
            DetailRowCompact(label: "Cédula/RNC:", value: "Por consultar", isDark: isDark)
            DetailRowCompact(label: "Teléfono:", value: "Por consultar", isDark: isDark)
        }
    }
}

/** Botones de acción */
@Composable
private fun actionButtons(
    viewModel: ContractDetailViewModel,
    navController: androidx.navigation.NavController
) {
    val isDark = RentCarRDSettings.isDarkMode()

    // Solo mostrar acciones si el contrato está ACTIVE y no ha comenzado
    val canExtend = viewModel.contract!!.status == "ACTIVE"
    val canCancel = viewModel.contract!!.status == "ACTIVE" && /* verificar que no tenga fechas pasadas */

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón: Extender Alquiler
        if (canExtend) {
            OutlinedButton(
                onClick = {
                    // TODO: Navegar a pantalla de extensión o modificar fechas
                    // navController.navigate("extend-rental", contractId = viewModel.contract!!.id.toString())
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Extender Alquiler", modifier = Modifier.align(Alignment.Center))
            }
        }

        // Botón: Cancelar Contrato
        if (canCancel) {
            OutlinedButton(
                onClick = {
                    // TODO: Cancelar contrato y actualizar estado
                    // viewModel.cancelContract()
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedButtonColors(
                    defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
                )
            ) {
                Text("Cancelar", modifier = Modifier.align(Alignment.Center))
                    .copy(
                        contentDescription = "Cancelar contrato",
                        // remove the modifier.align as it conflicts with Modifier.weight
                    )
            }
        }
    }
}