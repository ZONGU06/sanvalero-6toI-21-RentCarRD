package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.recyclerview.selection.*
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.domain.Model.Contract
import com.sanvalero.rentcarrd.domain.usecase.CheckOverbookingUseCase
import com.sanvalero.rentcarrd.domain.usecase.CalculateRentalPriceUseCase
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.CatalogViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import com.sanvalero.rentcarrd.viewmodel.RentalViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de solicitud de alquiler para clientes.
 * Permite seleccionar vehículo, fechas y generar el contrato de alquiler.
 */
@Composable
fun RentalRequestScreen(navController: androidx.navigation.NavController) {
    val viewModel: RentalViewModel = viewModel()

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
            rentalHeader()

            Spacer(height = 32.dp)

            // Selección de vehículo
            if (viewModel.isLoading.value) {
                loadingState()
            } else if (viewModel.selectedVehicle.value != null) {
                vehicleSelectionState(viewModel)
            } else {
                vehicleSelectionScreen(viewModel, navController)
            }

            Spacer(height = 32.dp)

            // Selección de fechas
            if (viewModel.vehicleSelected.value) {
                dateSelectionScreen(viewModel, navController)
            }

            Spacer(height = 24.dp)

            // Resumen y botón de confirmar
            if (viewModel.rentalReady.value) {
                confirmRentalSummary(viewModel)
            }
        }
    }
}

/** Composable auxiliar para encabezado del rental */
@Composable
private fun rentalHeader() {
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
                text = "Solicitar Nuevo Alquiler",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Seleccione un vehículo y las fechas para su alquiler",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
            )
        }
    }
}

/** Pantalla de selección de vehículo */
@Composable
private fun vehicleSelectionScreen(
    viewModel: RentalViewModel,
    navController: androidx.navigation.NavController
) {
    val isDark = RentCarRDSettings.isDarkMode()
    val vehicles = viewModel.availableVehicles

    if (vehicles.isEmpty()) {
        EmptyState(
            icon = Icons.Default.EXPAND_MORE,
            title = "No hay vehículos disponibles",
            description = "Intente más tarde o contacte con un operador"
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = Padding(16.dp, 0.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vehicles) { vehicle ->
            rentalVehicleItem(vehicle, viewModel)
        }
    }
}

/** Ítem de vehículo para rental */
@Composable
private fun rentalVehicleItem(
    vehicle: VehicleEntity,
    viewModel: RentalViewModel
) {
    val isDark = RentCarRDSettings.isDarkMode()
    val isSelected = viewModel.selectedVehicle.value?.id == vehicle.id

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) {
                if (isSelected) Color(0xFF3A3A3A) else Color(0xFF2C2C2C)
            } else {
                if (isSelected) Color(0xFFD3F7D3) else Color(0xFFFFFFFF)
            },
            pressed = if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
            selected = if (isSelected) Color(0xFF4A4A4A) else Color(0xFFBBDEFB)
        )
    ) {
        Modifier.clickable {
            viewModel.selectVehicle(vehicle)
        } {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Información del vehículo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Marca y modelo
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(40.dp)
                            .background(
                                if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Text(
                            text = "${vehicle.brand} ${vehicle.model}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.White : Color.Black,
                            textAlign = Alignment.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Estado
                    Container(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(
                                if (vehicle.status == "AVAILABLE") {
                                    if (isDark) Color(0xFF009688) else Color(0xFF4CAF50)
                                } else {
                                    if (isDark) Color(0xFFD32F2F) else Color(0xFFf44336)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Text(
                            text = vehicle.status,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color.White : Color.Black,
                            textAlign = Alignment.Center
                        )
                    }
                }

                // Detalles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailItem(label: "Tarifa:", value: "RD$ ${vehicle.dailyRate}", isDark: isDark)
                    DetailItem(label: "Pasajeros:", value: "${vehicle.passengers}", isDark: isDark)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailItem(label: "Combustible:", value: vehicle.fuelType, isDark: isDark)
                    DetailItem(label: "Categoría:", value: vehicle.category, isDark: isDark)
                }
            }
        }
    }
}

/** Pantalla de selección de fechas */
@Composable
private fun dateSelectionScreen(
    viewModel: RentalViewModel,
    navController: androidx.navigation.NavController
) {
    val isDark = RentCarRDSettings.isDarkMode()
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    OutlinedTextField(
        value = startDate,
        onValueChange = { startDate = it },
        label = { Text("Fecha de Inicio") },
        placeholder = { Text("dd/MM/yyyy") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Date
        )
    )

    OutlinedTextField(
        value = endDate,
        onValueChange = { endDate = it },
        label = { Text("Fecha de Fin") },
        placeholder = { Text("dd/MM/yyyy") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Date
        )
    )

    if (error.isNotEmpty()) {
        Text(text = error, style = MaterialTheme.typography.bodySmall, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
    }

    Button(
        onClick = {
            // Validar fechas y verificar superposición
            viewModel.checkOverbookingAndCalculate(startDate, endDate) { success ->
                if (success) {
                    navController.navigate("confirm-rental")
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
        )
    ) {
        Text(
            text = "Calcular Precio y Verificar Disponibilidad",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

/** Resumen antes de confirmar el alquiler */
@Composable
private fun confirmRentalSummary(viewModel: RentalViewModel) {
    val isDark = RentCarRDSettings.isDarkMode()
    val rental = viewModel.rentalSummary.value!!

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
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Resumen del Alquiler",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Detalles del vehículo
            DetailRowCompact(label: "Vehículo:", value: "${rental.vehicle.brand} ${rental.vehicle.model}", isDark: isDark)
            DetailRowCompact(label: "Tarifa Diaria:", value: "RD$ ${rental.vehicle.dailyRate}", isDark: isDark)

            // Fechas y cálculo
            DetailRowCompact(label: "Fecha Inicio:", value: rental.startDate, isDark: isDark)
            DetailRowCompact(label: "Fecha Fin:", value: rental.endDate, isDark: isDark)

            // Cálculo de precio
            DetailRowCompact(
                label: "Días Facturables:",
                value: "${rental.billableDays} días",
                isDark: isDark
            )
            DetailRowCompact(
                label: "Precio Base:",
                value: "${RDFormatter.formatRD$(rental.basePrice)}",
                isDark: isDark
            )

            // Descuentos si aplica
            if (rental.discounts > 0) {
                DetailRowCompact(
                    label: "Descuentos:",
                    value: "-${RDFormatter.formatRD$(rental.discounts)}",
                    isDark: isDark
                )
            }

            // Total
            DetailRowCompact(
                label: "TOTAL:",
                value: "${RDFormatter.formatRD$(rental.totalAmount)}",
                isDark: isDark,
                isBold: true
            )

            Spacer(height = 24.dp)

            // Botón confirmar
            Button(
                onClick = {
                    viewModel.confirmRental()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F),
                    enabled = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
                )
            ) {
                Text(
                    text = "Confirmar Alquiler",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}

/** Componentes auxiliares para el rental flow */

@Composable
private fun DetailRowCompact(
    label: String,
    value: String,
    isDark: Boolean,
    isBold: Boolean = false
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
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.body1,
            color = if (isDark) Color(0xFFFFFFFF) else Color.Black,
            fontWeight = if (isBold) androidx.compose.ui.font.Weight.Bold else androidx.compose.ui.font.Weight.Normal
        )
    }
}

@Composable
private fun Container(modifier: Modifier) = androidx.compose.foundation.layout.Container(modifier = modifier)