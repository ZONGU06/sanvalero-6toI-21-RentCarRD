package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.recyclerview.selection.*
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.CatalogViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import javax.inject.inject

/**
 * Pantalla de catálogo de vehículos para clientes.
 * Muestra la lista de vehículos disponibles con filtros.
 */
@Composable
fun CatalogScreen(navController: androidx.navigation.NavController) {
    val viewModel: CatalogViewModel = viewModel()

    val isDark = RentCarRDSettings.isDarkMode()

    var filterBrand by remember { mutableStateOf("") }
    var filterCategory by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf("AVAILABLE") }

    val vehicleList = viewModel.filteredVehicles

    Scaffold(
        appBar = {
            AppBar(
                title = "Catálogo de Vehículos",
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("my-rentals") }) {
                        Icon(imageVector = Icons.Default.ARROW_BACK, contentDescription: "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // TODO: Navegar a pantalla de nuevo alquiler
            }) {
                Icon(Icons.Default.ADD, contentDescription: "Nuevo alquiler")
                // TODO: Change the color based on theme
                // todo
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Filtros de búsqueda
            SearchFilters(
                filterBrand = { filterBrand = it },
                filterCategory = { filterCategory = it },
                filterStatus = { filterStatus = it },
                selectedBrand = filterBrand,
                selectedCategory = filterCategory,
                selectedStatus = filterStatus
            )

            Spacer(height = 16.dp)

            // Lista de vehículos
            if (vehicleList.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.EXPAND_MORE,
                    title = "No hay vehículos disponibles",
                    description = "Intente modificar los filtros de búsqueda"
                )
            } else {
                VehicleList(
                    vehicles = vehicleList,
                    onVehicleSelected = { vehicle ->
                        // TODO: Navegar a detalles del vehículo o iniciar alquiler
                        // navController.navigate("vehicle-details", vehicleId = vehicle.id)
                    }
                )
            }
        }
    }
}

/**
 * Componente de filtros de búsqueda para el catálogo.
 */
@Composable
private fun SearchFilters(
    filterBrand: (String) -> Unit,
    filterCategory: (String) -> Unit,
    filterStatus: (String) -> Unit,
    selectedBrand: String,
    selectedCategory: String,
    selectedStatus: String
) {
    OutlinedTextField(
        value = selectedBrand,
        onValueChange = filterBrand,
        label = { Text("Marca") },
        placeholder = { Text("Ej: Toyota") },
        modifier = Modifier.weight(1f)
    )

    OutlinedTextField(
        value = selectedCategory,
        onValueChange = filterCategory,
        label = { Text("Categoría") },
        placeholder = { Text("Ej: Sedan") },
        modifier = Modifier.weight(1f)
    )

    OutlinedTextField(
        value = selectedStatus,
        onValueChange = filterStatus,
        label = { Text("Estado") },
        placeholder = { Text("Ej: Disponible") },
        modifier = Modifier.weight(1f)
    )
}

/**
 * Componente de lista de vehículos.
 */
@Composable
private fun VehicleList(vehicles: List<VehicleEntity>, onVehicleSelected: (VehicleEntity) -> Unit) {
    val isDark = RentCarRDSettings.isDarkMode()

    // Colección LazyColumn para lista eficiente
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = Padding(16.dp, 0.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vehicles) { vehicle ->
            VehicleCard(
                vehicle = vehicle,
                onSelect = { onVehicleSelected(vehicle) }
            )
        }
    }
}

/**
 * Tarjeta individual de vehículo.
 */
@Composable
private fun VehicleCard(vehicle: VehicleEntity, onSelect: () -> Unit) {
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
        Modifier.clickable(onClick = onSelect) {
            // Efecto de clic
        } {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Imagen del vehículo (placeholder)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            if (isDark) Color(0xFF3A3A3A) else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.AUTOMOTIVE,
                        contentDescription = "${vehicle.brand} ${vehicle.model}",
                        tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
                        contentDescription = "Vehículo"
                    )
                }

                Text(
                    text = "${vehicle.brand} ${vehicle.model}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) Color.White : Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailRow(
                        label = "Año:",
                        value = "${vehicle.year}",
                        isDark = isDark
                    )
                    DetailRow(
                        label = "Color:",
                        value = vehicle.color,
                        isDark = isDark
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailRow(
                        label = "Tarifa:",
                        value = "RD$ ${vehicle.dailyRate}",
                        isDark = isDark
                    )
                    DetailRow(
                        label = "Estado:",
                        value = vehicle.status,
                        isDark = isDark
                    )
                }
            }
        }
    }
}

/**
 * Fila de detalles compacta.
 */
@Composable
private fun DetailRow(label: String, value: String, isDark: Boolean) {
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
            style = MaterialTheme.typography.body1,
            color = if (isDark) Color(0xFFFFFFFF) : Color.Black,
            fontWeight = androidx.compose.ui.font.Weight.Medium
        )
    }
}

/**
 * Estado vacío cuando no hay resultados.
 */
@Composable
private fun EmptyState(icon: Int, title: String, description: String) {
    val isDark = RentCarRDSettings.isDarkMode()

    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            size = 48.dp
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            textAlign = Alignment.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) Color(0xFF666666) else Color(0xFF999999),
            textAlign = Alignment.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}