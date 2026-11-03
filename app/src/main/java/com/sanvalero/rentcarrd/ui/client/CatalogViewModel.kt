package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.domain.Model.Vehicle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * ViewModel para la pantalla Catálogo de vehículos.
 * Maneja la lógica de filtrado y carga de vehículos.
 */
@viewModel
class CatalogViewModel {

    // Datos observables
    private val _allVehicles = MutableStateFlow<List<VehicleEntity>>(emptyList())
    val allVehicles: StateFlow<List<VehicleEntity>> = _allVehicles

    private val _filteredVehicles = MutableStateFlow<List<VehicleEntity>>(emptyList())
    val filteredVehicles: StateFlow<List<VehicleEntity>> = _filteredVehicles

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Filtros actuales
    private var currentBrandFilter = ""
    private var currentCategoryFilter = ""
    private var currentStatusFilter = "AVAILABLE"

    init {
        loadVehicles()
    }

    /** Carga todos los vehículos desde la base de datos */
    fun loadVehicles() {
        isLoading.value = true
        launch {
            // TODO: Inyectar VehicleDao y consultar la base de datos
            // Por ejemplo: val vehicles = vehicleDao.getAllVehicles()
            // _allVehicles.value = vehicles
            
            // Datos simulados por mientras
            val mockVehicles = generateMockVehicles()
            _allVehicles.value = mockVehicles
            applyFilters()
            isLoading.value = false
        }
    }

    /** Aplica los filtros actuales a la lista de vehículos */
    private fun applyFilters() {
        val all = _allVehicles.value
        if (all.isEmpty()) return

        _filteredVehicles.value = all.filter { vehicle ->
            (currentBrandFilter.isEmpty() || vehicle.brand.contains(currentBrandFilter, ignoreCase = true)) &&
            (currentCategoryFilter.isEmpty() || vehicle.category == currentCategoryFilter) &&
            (currentStatusFilter.isEmpty() || vehicle.status == currentStatusFilter)
        }
    }

    /** Filtra por marca */
    fun setBrandFilter(brand: String) {
        currentBrandFilter = brand
        applyFilters()
    }

    /** Filtra por categoría */
    fun setCategoryFilter(category: String) {
        currentCategoryFilter = category
        applyFilters()
    }

    /** Filtra por estado */
    fun setStatusFilter(status: String) {
        currentStatusFilter = status
        applyFilters()
    }

    /** Limpia todos los filtros */
    fun clearFilters() {
        currentBrandFilter = ""
        currentCategoryFilter = ""
        currentStatusFilter = "AVAILABLE"
        applyFilters()
    }

    /** Genera vehículos de ejemplo para demostración */
    private fun generateMockVehicles(): List<VehicleEntity> {
        return listOf(
            VehicleEntity(id = 1, brand = "Toyota", model = "Corolla", year = 2020, color = "Blanco", category = "Sedan", transmission = "Automático", passengers = 5, fuelType = "Gasolina", dailyRate = 1500.0, status = "AVAILABLE", imageUri = ""),
            VehicleEntity(id = 2, brand = "Ford", model = "Focus", year = 2019, color = "Negro", category = "Sedan", transmission = "Manual", passengers = 5, fuelType = "Gasolina", dailyRate = 1200.0, status = "RENTED", imageUri = ""),
            VehicleEntity(id = 3, brand = "Chevrolet", model = "Cruze", year = 2021, color = "Plateado", category = "Sedan", transmission = "Automático", passengers = 5, fuelType = "Gasolina", dailyRate = 1350.0, status = "AVAILABLE", imageUri = ""),
            VehicleEntity(id = 4, brand = "Renault", model = "Duster", year = 2022, color = "Rojo", category = "SUV", transmission = "Automático", passengers = 7, fuelType = "Diesel", dailyRate = 2000.0, status = "AVAILABLE", imageUri = ""),
            VehicleEntity(id = 5, brand = "Volkswagen", model = "Golf", year = 2020, color = "Blanco", category = "Hatchback", transmission = "Manual", passengers = 4, fuelType = "Gasolina", dailyRate = 1100.0, status = "MAINTENANCE", imageUri = ""),
            VehicleEntity(id = 6, brand = "Honda", model = "Civic", year = 2021, color = "Negro", category = "Sedan", transmission = "Automático", passengers = 5, fuelType = "Gasolina", dailyRate = 1400.0, status = "AVAILABLE", imageUri = "")
        )
    }
}