package com.sanvalero.rentcarrd.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.MaintenanceServiceEntity
import com.sanvalero.rentcarrd.domain.Model.MaintenanceService
import com.sanvalero.rentcarrd.domain.usecase.CalculateMaintenancePenaltyUseCase
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.OperationsViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.inject

/**
 * Pantalla de detalle y actualización de servicio de mantenimiento.
 * Permite al operador actualizar el estado, fecha de completión y ver penalizaciones.
 */
@Composable
fun MaintenanceDetailsScreen(
    maintenanceId: Int,
    navController: androidx.navigation.NavController
) {
    val viewModel: OperationsViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    // Cargar datos del servicio (en producción vendrían del dao)
    val (promisedCompletionDate, actualCompletionDate, maintenanceStatus, laborCost, partsCost) = loadMaintenanceData(maintenanceId)

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
            // Header con información del mantenimiento
            maintenanceHeaderInfo(maintenanceId, promisedCompletionDate, maintenanceStatus)

            Spacer(height = 32.dp)

            // Formulario de actualización
            updateForm(
                promisedDate = promisedCompletionDate,
                actualDate = actualCompletionDate ?: "",
                status = maintenanceStatus,
                onStatusSelected = { newStatus ->
                    // Actualizar estado en la base de datos
                    viewModel.updateMaintenanceStatus(
                        maintenanceId = maintenanceId,
                        newStatus = newStatus,
                        actualDate = actualDate.value ?: ""
                    )
                }
            )

            Spacer(height = 24.dp)

            // Información de penalización si está retrasado
            penaltyInfo(laborCost, partsCost, promisedCompletionDate, actualCompletionDate, isDark)

            Spacer(height = 32.dp)

            // Botón de completar servicio
            completeServiceButton(maintenanceId, isDark, navController)
        }
    }
}

/** Cargar datos del mantenimiento - placeholder */
private fun loadMaintenanceData(maintenanceId: Int): Triple<String, String, String, String, String> {
    // En producción consultaría la base de datos
    return "25/12/2024".toTriple(
        "28/12/2024".toTriple(
            "IN_PROGRESS".toTriple(
                "850.0".toTriple("350.0".toTriple())
            )
        )
    )
}

/** Encabezado de información del mantenimiento */
@Composable
private fun maintenanceHeaderInfo(
    maintenanceId: Int,
    promisedDate: String,
    status: String
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mantenimiento #$maintenanceId",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) Color.White : Color.Black
                )
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Ver Historial")
                }
            }

            // Fechas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailItem(label: "Fecha Prometida:", value: promisedDate, isDark: isDark)
                if (status != "COMPLETED") {
                    DetailItem(label: "Estado:", value: status, isDark: isDark, isWarning: true)
                } else {
                    DetailItem(label: "Estado:", value: status, isDark: isDark)
                }
            }

            // Costos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailItem(label: "Mano de Obra:", value: "RD$ 850.00", isDark: isDark)
                DetailItem(label: "Repuestos:", value: "RD$ 350.00", isDark: isDark)
            }
        }
    }
}

/** Formulario de actualización */
@Composable
private fun updateForm(
    promisedDate: String,
    actualDate: String,
    status: String,
    onStatusSelected: (String) -> Unit
) {
    val isDark = RentCarRDSettings.isDarkMode()
    var selectedStatus by remember { mutableStateOf(status) }
    var actualDateValue by remember { mutableStateOf(actualDate) }

    OutlinedTextField(
        value = actualDateValue,
        onValueChange = { actualDateValue = it },
        label = { Text("Fecha de Devolución/Completación (dd/MM/yyyy)") },
        placeholder = { Text("dd/MM/yyyy") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Date
        )
    )

    // Selector de estado
    OutlinedButtonGroup(
        value = selectedStatus,
        onValueChange = { selectedStatus = it },
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        OutlinedButtonGroupItem(
            value = "PENDING",
            onClick = { onStatusSelected("PENDING") }
        ) {
            Text("Pendiente")
        }
        OutlinedButtonGroupItem(
            value = "IN_PROGRESS",
            onClick = { onStatusSelected("IN_PROGRESS") }
        ) {
            Text("En Progreso")
        }
        OutlinedButtonGroupItem(
            value = "COMPLETED",
            onClick = { onStatusSelected("COMPLETED") }
        ) {
            Text("Completado")
        }
        OutlinedButtonGroupItem(
            value = "DELAYED",
            onClick = { onStatusSelected("DELAYED") }
        ) {
            Text("Retrasado")
        }
    }
}

/** Información de penalización */
@Composable
private fun penaltyInfo(
    laborCost: String,
    partsCost: String,
    promisedDate: String,
    actualDate: String,
    isDark: Boolean
) {
    // Calcular si hay penalización
    val penaltyUseCase = CalculateMaintenancePenaltyUseCase()

    val result = penaltyUseCase.calculate(
        promisedDate = promisedDate,
        actualDate = actualDate,
        budgetAmount = (laborCost.toDouble() + partsCost.toDouble()).coerceAtLeast(0.0)
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFFFF0F0),
            pressed = if (isDark) Color(0xFF3A3A3A) else Color(0xFFF0E0E0),
            selected = if (isDark) Color(0xFF4A4A4A) else Color(0xFFFFC107)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Información de Penetración",
                style = MaterialTheme.typography.titleSmall,
                color = if (isDark) Color(0xFFFFFFFF) else Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mostrar retraso
            if (result.isDelayed) {
                Text(
                    text = "⚠️ RETRASO DETECTADO",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFFFF0000) else Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Descuento aplicado: ${RDFormatter.formatRD$(result.discount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color(0xFFFFFFFF) else Color.Black
                )
            } else {
                Text(
                    text: "✅ Servicio dentro del plazo acordado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFF009688) else Color.Green
                )
            }

            // Detalles
            DetailItem(label: "Presupuesto Base:", value: "${RDFormatter.formatRD$(laborCost.toDouble() + partsCost.toDouble())}", isDark: isDark)
            DetailItem(label: "Penalización %:", value: "${result.discount.toInt() > 0 ? "${result.discount * 100}%" : "0%"}", isDark: isDark)
        }
    }
}

/** Botón de completar servicio */
@Composable
private fun completeServiceButton(
    maintenanceId: Int,
    isDark: Boolean,
    navController: androidx.navigation.NavController
) {
    val totalCost = 850.0 + 350.0 // sería obtenido del sistema

    Button(
        onClick = {
            // Marcar como completado y navegar
            // viewModel.updateMaintenanceStatus(...)
            navController.popBackStack()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
        )
    ) {
        Text(
            text = "Marcar Servicio como Completado",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}