package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.data.daos.ContractDao
import com.sanvalero.rentcarrd.data.daos.VehicleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de contrato.
 * Administra la carga de datos y las acciones del contrato.
 */
@viewModel
class ContractDetailViewModel @Inject constructor(
    private val contractDao: ContractDao,
    private val vehicleDao: VehicleDao
) {

    // Estado observable
    private val _contract = MutableStateOf<ContractEntity?>(null)
    val contract: StateFlow<ContractEntity?> = _contract

    private val _vehicle = MutableStateOf<VehicleEntity?>(null)
    val vehicle: StateFlow<VehicleEntity?> = _vehicle

    init {
        // Se cargaría el contrato por ID en la inicialización
        // Por ahora deja placeholder
    }

    /** Carga un contrato por su ID */
    fun loadContract(contractId: Int) {
        launch {
            // TODO: En producción: contractDao.getContractById(contractId)
            // Por ahora datos placeholder
            _contract.value = ContractEntity(
                id = contractId,
                contractNumber = "RC-2024-00${contractId}",
                contractType = "RENTAL",
                clientId = 1,
                vehicleId = 1,
                startDate = "01/12/2024",
                expectedEndDate = "31/12/2024",
                actualEndDate = null,
                baseAmount = 1500.0,
                extraCharges = 0.0,
                discounts = 0.0,
                totalAmount = 1500.0,
                status = "ACTIVE",
                isDeleted = false
            )

            // Cargar información del vehículo
            _vehicle.value = VehicleEntity(
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
            )
        }
    }
}