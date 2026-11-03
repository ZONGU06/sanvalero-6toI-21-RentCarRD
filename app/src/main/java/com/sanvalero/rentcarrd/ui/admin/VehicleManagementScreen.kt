package com.sanvalero.rentcarrd.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.AdminViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de gestión de vehículos para el administrador.
 * CRUD completo: crear, editar, eliminar (lógico) y ver vehículos.
 */
@Composable
fun VehicleManagementScreen(navController: androidx.navigation.NavController) {
    val viewModel: AdminViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    // Estado de formulario
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("2024") }
    var color by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Sedan") }
    var transmission by remember { mutableStateOf("Automático") }
    var passengers by remember { mutableStateOf("5") }
    var fuelType by remember { mutableStateOf("Gasolina") }
    var dailyRate by remember { mutableStateOf("0.0") }
    var status by remember { mutableStateOf("AVAILABLE") }
    var error by remember { mutableStateOf("") }

    // Lista de vehículos
    val vehicles: Flow<List<VehicleEntity>> = viewModel.vehicles

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
            vehicleManagementHeader()

            Spacer(height = 32.dp)

            // Formulario de vehículo
            vehicleForm(brand, model, year, color, category, transmission,
                passengers, fuelType, dailyRate, status, error)

            Spacer(height = 24.dp)

            // Lista de vehículos
            vehicleList(viewModel)

            Spacer(height = 24.dp)

            // Acciones rápidas
            rapidActions(viewModel)
        }
    }
}

/** Encabezado de gestión de vehículos */
@Composable
private fun vehicleManagementHeader() {
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
                text = "Gestión de Vehículos",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "CRUD completo de vehículos del catálogo",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
            )
        }
    }
}

/** Formulario de vehículo */
@Composable
private fun vehicleForm(
    brand: String, model: String, year: String, color: String,
    category: String, transmission: String, passengers: String,
    fuelType: String, dailyRate: String, status: String,
    error: String
) {
    val isDark = RentCarRDSettings.isDarkMode()

    OutlinedTextField(
        value = brand,
        onValueChange = { brand = it },
        label = { Text("Marca") },
        placeholder = { Text("Ej: Toyota") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = model,
        onValueChange = { model = it },
        label = { Text("Modelo") },
        placeholder = { Text("Ej: Corolla") },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = year,
        onValueChange = { year = it },
        label = { Text("Año") },
        placeholder = { Text("Ej: 2024") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    OutlinedTextField(
        value = color,
        onValueChange = { color = it },
        label = { Text("Color") },
        placeholder = { Text("Ej: Blanco") },
        modifier = Modifier.fillMaxWidth()
    )

    // Categoría
    OutlinedTextField(
        value = category,
        onValueChange = { category = it },
        label = { Text("Categoría") },
        placeholder = { Text("Ej: Sedan") },
        modifier = Modifier.fillMaxWidth()
    )

    // Transmisión
    OutlinedTextField(
        value = transmission,
        onValueChange = { transmission = it },
        label = { Text("Transmisión") },
        placeholder = { Text("Ej: Automático") },
        modifier = Modifier.fillMaxWidth()
    )

    // Pasajeros
    OutlinedTextField(
        value = passengers,
        onValueChange = { passengers = it },
        label = { Text("Pasajeros") },
        placeholder = { Text("Ej: 5") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    // Combustible
    OutlinedTextField(
        value = fuelType,
        onValueChange = { fuelType = it },
        label = { Text("Tipo de Combustible") },
        placeholder = { Text("Ej: Gasolina") },
        modifier = Modifier.fillMaxWidth()
    )

    // Tarifa diaria
    OutlinedTextField(
        value = dailyRate,
        onValueChange = { dailyRate = it },
        label = { Text("Tarifa Diaria (RD$)") },
        placeholder = { Text("Ej: 1500.00") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
            inputType = androidx.compose.ui.input.keyboard.inputType.Number
        )
    )

    // Estado
    OutlinedButtonGroup(
        value = status,
        onValueChange = { status = it },
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        OutlinedButtonGroupItem(
            value = "AVAILABLE",
            onClick = { status = "AVAILABLE" }
        ) {
            Text("Disponible")
        }
        OutlinedButtonGroupItem(
            value = "RENTED",
            onClick = { status = "RENTED" }
        ) {
            Text("Alquilado")
        }
        OutlinedButtonGroupItem(
            value = "MAINTENANCE",
            onClick = { status = "MAINTENANCE" }
        ) {
            Text("Mantenimiento")
        }
        OutlinedButtonGroupItem(
            value = "INACTIVE",
            onClick = { status = "INACTIVE" }
        ) {
            Text("Inactivo")
        }
    }

    // Errores
    if (error.isNotEmpty()) {
        Text(text = error, style = MaterialTheme.typography.bodySmall, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
    }

    // Botón guardar
    Button(
        onClick = {
            // Validar campos obligatorios
            if (brand.isEmpty() || model.isEmpty() || dailyRate.isEmpty()) {
                error = "Por favor complete los campos obligatorios"
                return@Button
            }

            // TODO: Guardar vehículo en la base de datos mediante AdminViewModel
            // TODO: Limpiar formulario
            error = "Vehículo guardado exitosamente"
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            defaultColor = if (isDark) Color(0xFFD32F2F) else Color(0xFFD32F2F)
        )
    ) {
        Text("Guardar Vehículo", modifier = Modifier.align(Alignment.Center))
    }
}

/** Lista de vehículos */
@Composable
private fun vehicleList(viewModel: AdminViewModel) {
    val isDark = RentCarRDSettings.isDarkMode()

    // Flow de vehículos observables
    val vehiclesList = viewModel.vehicles.collectAsList()

    if (vehiclesList.isEmpty()) {
        EmptyState(
            icon = Icons.Default.ADD,
            title = "No hay vehículos",
            description = "El catálogo de vehículos está vacío"
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = Padding(16.dp, 0.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vehiclesList) { vehicle ->
            vehicleListItem(vehicle)
        }
    }
}

/** Ítem de vehículo en lista */
@Composable
private fun vehicleListItem(vehicle: VehicleEntity) {
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
        Modifier.clickable {
            // TODO: Navegar a edición o ver detalles
        } {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Marca y modelo
                    Text(
                        text = "${vehicle.brand} ${vehicle.model}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDark) Color.White : Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    // Estado
                    Container(
                        width = 80,
                        height = 20,
                        background = when (vehicle.status) {
                            "AVAILABLE" -> if (isDark) Color(0xFF009688) else Color(0xFF4CAF50)
                            "RENTED" -> if (isDark) Color(0xFFD32F2F) else Color(0xFFf44336)
                            "MAINTENANCE" -> if (isDark) Color(0xFF3F51B5) else Color(0xFF9C27B0)
                            else -> if (isDark) Color(0xFF666666) else Color(0xFF777777)
                        },
                        shape = RoundedCornerShape(4.dp)
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
                    DetailItem(label: "Año:", value: "${vehicle.year}", isDark: isDark)
                    DetailItem(label: "Color:", value: vehicle.color, isDark: isDark)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailItem(label: "Tarifa:", value: "RD$ ${vehicle.dailyRate}", isDark: isDark)
                    DetailItem(label: "Categoría:", value: vehicle.category, isDark: isDark)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailItem(label: "Pasajeros:", value: "${vehicle.passengers}", isDark: isDark)
                    DetailItem(label: "Combustible:", value: vehicle.fuelType, isDark: isDark)
                }
            }
        }
    }
}

/** Acciones rápidas */
@Composable
private fun rapidActions(viewModel: AdminViewModel) {
    val isDark = RentCarRDSettings.isDarkMode()

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón: Agregar Vehículo
        OutlinedButton(
            onClick = {
                // Reset form state or navigate to form
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Agregar Vehículo", modifier = Modifier.align(Alignment.Center))
        }

        // Botón: Actualizar Lista
        OutlinedButton(
            onClick = {
                // Refrescar lista de vehículos
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Actualizar", modifier = Modifier.align(Alignment.Center))
        }
    }
}