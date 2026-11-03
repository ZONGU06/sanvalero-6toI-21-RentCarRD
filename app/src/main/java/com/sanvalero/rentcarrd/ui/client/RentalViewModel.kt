package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.domain.Model.Contract
import com.sanvalero.rentcarrd.domain.usecase.CalculateRentalPriceUseCase
import com.sanvalero.rentcarrd.domain.usecase.CheckOverbookingUseCase
import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.daos.ContractDao
import com.sanvalero.rentcarrd.data.daos.VehicleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * ViewModel para el flujo de solicitud de alquiler.
 * Coordina la selección de vehículo, fechas, cálculo de precio,
 * verificación de superposición y creación del contrato.
 */
@viewModel
class RentalViewModel @Inject constructor(
    private val calculateRentalPriceUseCase: CalculateRentalPriceUseCase,
    private val checkOverbookingUseCase: CheckOverbookingUseCase,
    private val vehicleDao: VehicleDao,
    private val contractDao: ContractDao,
    private val database: RentCarRDDatabase
) {

    // Estado del flujo de rental
    private val _availableVehicles = MutableStateFlow<List<VehicleEntity>>(emptyList())
    val availableVehicles: StateFlow<List<VehicleEntity>> = _availableVehicles

    private val _selectedVehicle = MutableStateFlow<VehicleEntity?>(null)
    val selectedVehicle: StateFlow<VehicleEntity?> = _selectedVehicle

    private val _vehicleSelected = MutableStateFlow(false)
    val vehicleSelected: StateFlow<Boolean> = _vehicleSelected

    private val _rentalReady = MutableStateFlow(false)
    val rentalReady: StateFlow<Boolean> = _rentalReady

    private val _rentalSummary = MutableStateFlow<RentalData?>(null)
    val rentalSummary: StateFlow<RentalData?> = _rentalSummary

    // Datos temporales del rental
    private var _startDate: String = ""
    private var _endDate: String = ""
    private var _billableDays: Int = 1
    private var _basePrice: Double = 0.0
    private var _discounts: Double = 0.0
    private var _totalAmount: Double = 0.0

    // Referencia al usuario actual (inyección en producción)
    @Inject
    lateinit var currentUser: com.sanvalero.rentcarrd.domain.Model.User

    init {
        loadAvailableVehicles()
    }

    /** Carga vehículos disponibles para alquiler */
    private fun loadAvailableVehicles() {
        launch {
            val vehicles = vehicleDao.getAvailableVehicles()
            _availableVehicles.value = vehicles
        }
    }

    /** Selecciona un vehículo para el alquiler */
    fun selectVehicle(vehicle: VehicleEntity) {
        _selectedVehicle.value = vehicle
        _vehicleSelected.value = true
        _rentalReady.value = false
        _rentalSummary.value = null
        // Reset fechas
        _startDate = ""
        _endDate = ""
    }

    /** Verifica superposición y calcula precio */
    fun checkOverbookingAndCalculate(
        startDate: String,
        endDate: String,
        onComplete: (success: Boolean) -> Unit
    ) {
        launch {
            _rentalReady.value = false

            // 1. Verificar superposición (overbooking)
            val isOverbooking = checkOverbookingUseCase.isOverbooking(
                vehicleId = selectedVehicle.value?.id ?: 0,
                startDate = startDate,
                endDate = endDate
            )

            if (isOverbooking) {
                // Mostrar error de superposición
                error = "El vehículo ya está alquilado para las fechas solicitadas"
                _rentalReady.value = false
                onComplete(false)
                return
            }

            // 2. Calcular precio de alquiler
            val priceResult = calculateRentalPriceUseCase.calculate(
                startDate = startDate,
                endDate = endDate,
                dailyRate = selectedVehicle.value?.dailyRate ?: 0.0
            )

            // Almacenar resultados
            _startDate = startDate
            _endDate = endDate
            _billableDays = priceResult.billableDays
            _basePrice = priceResult.basePrice

            // 3. Aplicar descuentos por fidelidad o promociones (por ahora 0)
            _discounts = 0.0

            // 4. Calcular total
            _totalAmount = _basePrice + _discounts

            // Crear resumen de rental
            val rentalData = RentalData(
                vehicle = selectedVehicle.value!!,
                startDate = startDate,
                endDate = endDate,
                billableDays = _billableDays,
                basePrice = _basePrice,
                discounts = _discounts,
                totalAmount = _totalAmount
            )

            _rentalSummary.value = rentalData
            _rentalReady.value = true
            onComplete(true)
        }
    }

    /** Confirma y crea el contrato de alquiler */
    fun confirmRental() {
        if (_rentalSummary.value == null) return

        val rental = _rentalSummary.value!!

        launch {
            // Crear contrato en la base de datos
            val contract = ContractEntity(
                id = 0,
                contractNumber = generateContractNumber(),
                contractType = "RENTAL",
                clientId = currentUser.id,
                vehicleId = rental.vehicle.id,
                startDate = rental.startDate,
                expectedEndDate = rental.endDate,
                actualEndDate = null,
                baseAmount = rental.basePrice,
                extraCharges = 0.0,
                discounts = rental.discounts,
                totalAmount = rental.totalAmount,
                status = "ACTIVE",
                isDeleted = false
            )

            // Insertar contrato
            contractDao.insertContract(contract)

            // Actualizar estado del vehículo a RENTED
            vehicleDao.updateVehicleStatus(rental.vehicle.id, "RENTED")

            // Navegar de vuelta o mostrar confirmación
            _rentalReady.value = false
            _rentalSummary.value = null

            // TODO: En producción, navegar a "Mis Contratos" o pantalla de confirmación
        }
    }

    /** Genera un número de contrato único */
    private fun generateContractNumber(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val datePart = LocalDate.now().format(formatter)
        val randomPart = (1000 + java.util.Random().nextInt(8999)).toString()
        return "RC${datePart}$randomPart"
    }

    /** Obtiene el número de días facturables */
    fun getBillableDays(): Int = _billableDays

    /** Obtiene el precio base */
    fun getBasePrice(): Double = _basePrice

    /** Obtiene los descuentos */
    fun getDiscounts(): Double = _discounts

    /** Obtiene el total */
    fun getTotalAmount(): Double = _totalAmount

    /** Obtiene la fecha de inicio */
    fun getStartDate(): String = _startDate

    /** Obtiene la fecha de fin */
    fun getEndDate(): String = _endDate
}

/** Datos del rental para el resumen */
data class RentalData(
    val vehicle: VehicleEntity,
    val startDate: String,
    val endDate: String,
    val billableDays: Int,
    val basePrice: Double,
    val discounts: Double,
    val totalAmount: Double
)