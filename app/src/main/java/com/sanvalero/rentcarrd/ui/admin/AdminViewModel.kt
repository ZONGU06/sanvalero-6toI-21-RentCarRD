package com.sanvalero.rentcarrd.ui.admin

import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

/**
 * ViewModel para la gestión de vehículos en la administración.
 * Maneja el CRUD de vehículos y el estado del formulario.
 */
@viewModel
class AdminViewModel @Inject constructor(
    // En producción: private val vehicleDao: VehicleDao
) {

    // Estado observable de vehículos
    private val _vehicles = MutableStateFlow<List<VehicleEntity>>(emptyList())
    val vehicles: Flow<List<VehicleEntity>> = _vehicles

    /** Carga la lista de vehículos desde la base de datos */
    fun loadVehicles() {
        launch {
            // TODO: En producción: RentCarRDDatabase.getInstance(context).vehicleDao.getAllVehicles()
            // Por ahora datos placeholder
            val placeholderVehicles = listOf(
                VehicleEntity(
                    id = 1,
                    brand = "Toyota",
                    model = "Corolla",
                    year = 2020,
                    color = "Blanco",
                    category = "Sedan",
                    transmission = "Automático",
                    passengers = 5,
                    fuelType = "Gasolina",
                    dailyRate = 1500.0,
                    status = "AVAILABLE",
                    imageUri = ""
                ),
                VehicleEntity(
                    id = 2,
                    brand = "Ford",
                    model = "Focus",
                    year = 2019,
                    color = "Negro",
                    category = "Sedan",
                    transmission = "Manual",
                    passengers = 5,
                    fuelType = "Gasolina",
                    dailyRate = 1200.0,
                    status = "RENTED",
                    imageUri = ""
                ),
                VehicleEntity(
                    id = 3,
                    brand = "Chevrolet",
                    model = "Cruze",
                    year = 2021,
                    color = "Plateado",
                    category = "Sedan",
                    transmission = "Automático",
                    passengers = 5,
                    fuelType = "Gasolina",
                    dailyRate = 1350.0,
                    status = "AVAILABLE",
                    imageUri = ""
                )
            )
            _vehicles.value = placeholderVehicles
        }
    }

    /** Guarda un nuevo vehículo */
    fun saveVehicle(brand: String, model: String, year: Int,
                    color: String, category: String,
                    transmission: String, passengers: Int,
                    fuelType: String, dailyRate: Double, status: String) {
        launch {
            // TODO: Crear VehicleEntity y guardar en BD
            // TODO: Refrescar lista de vehículos
            loadVehicles()
        }
    }

    /** Actualiza un vehículo existente */
    fun updateVehicle(id: Int, brand: String, model: String, year: Int,
                      color: String, category: String,
                      transmission: String, passengers: Int,
                      fuelType: String, dailyRate: Double, status: String) {
        launch {
            // TODO: Actualizar vehículo en la BD
            // TODO: Refrescar lista
            loadVehicles()
        }
    }

    /** Elimina (lógica) un vehículo */
    fun deleteVehicle(id: Int) {
        launch {
            // TODO: Lógica soft delete en la BD
            // TODO: Refrescar lista
            loadVehicles()
        }
    }
}