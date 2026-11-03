package com.sanvalero.rentcarrd.ui.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.data.entities.MaintenanceServiceEntity
import com.sanvalero.rentcarrd.domain.Model.MaintenanceService
import com.sanvalero.rentcarrd.domain.usecase.CalculateMaintenancePenaltyUseCase
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.OperationsViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de creación de servicio de mantenimiento para operadores.
 * Permita registrar diagnósticos, calcular presupuestos y asignar vehículos.
 */
@Composable
fun MaintenanceCreationScreen(navController: androidx.navigation.NavController) {
    val viewModel: OperationsViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    var vehicleId by remember { mutableStateOf<Int?>(null) }
    var licensePlate by remember { mutableStateOf("") }
    var mileageBy remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("") }
    var laborCostBy remember { mutableStateOf("0.0") }
    var partsCostBy remember { mutableStateOf("0.0") }
    var promisedDate by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

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
            maintenanceHeader()

            Spacer(height = 32.dp)

            // Formulario de diagnóstico
            maintenanceForm(
                vehicleId = vehicleId,
                licensePlate = licensePlate,
                mileage = mileageBy,
                issueDescription = issueDescription,
                serviceType = serviceType,
                laborCost = laborCostBy,
                partsCost = partsCostBy,
                promisedDate = promisedDate,
                onVehicleSelected = { vehicle ->
                    vehicleId = vehicle.id
                    licensePlate = vehicle.licensePlate
                    mileageBy = vehicle.mileage.toString()
                    // Lógica adicional al seleccionar vehículo
                }
            )

            Spacer(height = 24.dp)

            // Botones de acción
            actionButtons(viewModel, promisedDate, error)
        }
    }
}

/** Encabezado de la pantalla de mantenimiento */
@Composable
private fun maintenanceHeader() {
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
                text = "Nuevo Diagnóstico de Mantenimiento",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Registre el diagnóstico del vehículo para iniciar el servicio de taller",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
            )
        }
    }
}

/** Formulario de diagnóstico */
@Composable
private fun maintenanceForm(
    vehicleId: Int?,
    licensePlate: String,
    mileage: String,
    issueDescription: String,
    serviceType: String,
    laborCost: String,
    partsCost: String,
    promisedDate: String,
    onVehicleSelected: (VehicleEntity) -> Unit
) {
    val isDark = RentCarRDSettings.isDarkMode()

    // Selección de vehículo
    OutlinedTextField(
        value = vehicleId?.let { "Vehículo ID: it" } ?: "Seleccione vehículo",
        onValueChange = { },
        label = { Text("Vehículo") },
        modifier = Modifier.fillMaxWidth(),
        enabled = false
    )

    OutlinedTextField(
        value = licensePlate,
        onValueChange = { licensePlate = it },
        label = { Text("Placa") },
        placeholder = { Text("Ej: ABC-1234") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = mileage,
        onValueChange = { mileage = it },
        label = { Text("Kilometraje") },
        placeholder = { Text("Ej: 45000.5") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    OutlinedTextField(
        value = issueDescription,
        onValueChange = { issueDescription = it },
        label = { Text("Descripción del Problema") },
        placeholder = { Text("Ej: Fallo en el sistema eléctrico") },
        modifier = Modifier.fillMaxWidth(),
        multiline = true,
        minLines = 3,
        maxLines = 5
    )

    // Tipo de servicio
    OutlinedTextField(
        value = serviceType,
        onValueChange = { serviceType = it },
        label = { Text("Tipo de Servicio") },
        placeholder = { Text("Ej: Reparación Eléctrica") },
        modifier = Modifier.fillMaxWidth()
    )

    // Costos
    OutlinedTextField(
        value = laborCost,
        onValueChange = { laborCost = it },
        label = { Text("Costo Mano de Obra (RD$)") },
        placeholder = { Text("Ej: 1500.00") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    OutlinedTextField(
        value = partsCost,
        onValueChange = { partsCost = it },
        label = { Text("Costo Repuestos (RD$)") },
        placeholder = { Text("Ej: 800.50") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    // Fecha prometida
    OutlinedTextField(
        value = promisedDate,
        onValueChange = { promisedDate = it },
        label = { Text("Fecha Prometida Entrega (dd/MM/yyyy)") },
        placeholder = { Text("dd/MM/yyyy") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Date
        )
    )

    // Mensajes de error
    if (error.isNotEmpty()) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/** Botones de acción */
@Composable
private fun actionButtons(
    viewModel: OperationsViewModel,
    promisedDate: String,
    error: String
) {
    val isDark = RentCarRDSettings.isDarkMode()

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón Diagnóstico
        OutlinedButton(
            onClick = {
                // Validar campos obligatorios
                if (licensePlate.isEmpty() || issueDescription.isEmpty() || promisedDate.isEmpty()) {
                    // Error handling
                    return@OutlinedButton
                }

                // Crear servicio de mantenimiento
                viewModel.startDiagnosis(
                    vehicleId = vehicleId!!,
                    vehicleBrand = "Toyota", // Obtenería de la base de datos
                    vehicleModel = "Corolla",
                    licensePlate = licensePlate,
                    issueDescription = issueDescription,
                    mileage = mileage.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Crear Diagnóstico", modifier = Modifier.align(Alignment.Center))
        }

        // Botón Presupuesto
        OutlinedButton(
            onClick = {
                // Validar que los costos sean números válidos
                val labor = laborCost.toDoubleOrNull()
                val parts = partsCost.toDoubleOrNull()

                if (labor == null || labor < 0 || parts == null || parts < 0) {
                    error = "Por favor ingrese costos válidos"
                    return@OutlinedButton
                }

                // Elaborar presupuesto
                viewModel.prepareBudget(
                    laborCost = labor,
                    partsCost = parts,
                    estimatedDays = 5, // días estimados
                    warrantyMonths = 6 // garantía en meses
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Presupuestar", modifier = Modifier.align(Alignment.Center))
        }
    }
}