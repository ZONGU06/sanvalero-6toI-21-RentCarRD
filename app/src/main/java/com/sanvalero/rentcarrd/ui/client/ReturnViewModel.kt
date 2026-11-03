package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.PaymentEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.domain.usecase.CalculateLateFeesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * ViewModel optimizado para flujo de devolución.
 * Caching, batching de operaciones y reducido número de DB queries.
 */
@viewModel
class ReturnViewModel @Inject constructor(
    calculateLateFeesUseCase: CalculateLateFeesUseCase,
    private val database: RentCarRDDatabase
) {

    // Estado observable
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _returnResult = MutableStateFlow<ReturnResult?>(null)
    val returnResult: StateFlow<ReturnResult?> = _returnResult

    // Cache para datos del contrato
    private var _cachedContract: ContractEntity? = null
    private var _cachedContractTime = 0L
    private val contractCacheTimeout = 30000L // 30 segundos

    // Formato de fecha
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        // Caching inicial
        loadCachedContractIfNeeded()
    }

    /** Caching inteligente - carga contrato solo si está caducado */
    private fun loadCachedContractIfNeeded() {
        val now = System.currentTimeMillis()
        if (_cachedContract == null || now - _cachedContractTime > contractCacheTimeout) {
            // Cargar contrato activo más reciente
            launch {
                _cachedContract = database.contractDao.getAllContracts()
                    .firstOrDefault { it.status == "ACTIVE" && it.isDeleted == false }
                _cachedContractTime = if (_cachedContract != null) System.currentTimeMillis() else 0
            }
        }
    }

    /** Procesar devolución con optimizaciones */
    fun processReturn(returnDateStr: String, onComplete: (success: Boolean) -> Unit) {
        launch {
            _isProcessing.value = true
            _returnResult.value = null

            try {
                // 1. Validar fecha rápidamente
                val returnDate = dateFormat.parse(returnDateStr) ?: run {
                    _isProcessing.value = false
                    onComplete(false)
                    return@launch
                }

                // 2. Usar contrato cacheado si está disponible y es reciente
                val contract = if (System.currentTimeMillis() - _cachedContractTime < contractCacheTimeout) {
                    _cachedContract!!
                } else {
                    // Actualizar cache
                    loadCachedContractIfNeeded()
                    _cachedContract!!
                }

                // 3. Verificar que existe contrato activo
                if (contract.id == 0) {
                    _isProcessing.value = false
                    onComplete(false)
                    return@launch
                }

                // 4. Calcular mora en una sola llamada
                val lateFeeResult = calculateLateFeesUseCase.calculate(
                    contractId = contract.id,
                    actualReturnDate = returnDateStr,
                    expectedEndDate = contract.expectedEndDate
                )

                // 5. Calcular días de retraso
                val expectedEnd = dateFormat.parse(contract.expectedEndDate)!!
                val returnDateObj = dateFormat.parse(returnDateStr)!!
                val delayDays = java.time.temporal.ChronoUnit.DAYS.between(expectedEnd, returnDateObj)
                val isLate = delayDays > 0

                // 6. Obtener vehículo del contrato (una sola query optimizada)
                val vehicle = database.vehicleDao.getAvailableVehicles().firstOrDefault() ?: VehicleEntity()

                // 7. Calcular total base + mora
                var totalAmount = contract.baseAmount.toDouble()
                if (isLate) {
                    totalAmount += lateFeeResult.lateFee
                }

                // 8. Crear pago de una sola transacción
                val payment = PaymentEntity(
                    id = 0,
                    contractId = contract.id,
                    amount = totalAmount,
                    paymentDate = returnDateStr,
                    paymentMethod = "CASH",
                    status if (isLate) "OVERDUE" else "PENDING"
                )
                database.paymentDao.insertPayment(payment)

                // 9. Actualizar vehículo en lote
                database.vehicleDao.updateVehicleStatus(vehicle.id, "AVAILABLE", false)

                // 10. Actualizar contrato
                database.contractDao.updateContract(
                    contract.id,
                    "COMPLETED",
                    returnDateStr,
                    true
                )

                // 11. Actualizar cache
                _cachedContractTime = System.currentTimeMillis()

                // 12. Retornar resultado
                val result = ReturnResult(
                    vehicleBrand = vehicle.brand,
                    vehicleModel = vehicle.model,
                    returnDate = returnDateStr,
                    expectedDate = contract.expectedEndDate,
                    isLate = isLate,
                    delayDays = if (isLate) delayDays else 0,
                    lateFee = lateFeeResult.lateFee,
                    totalAmount = totalAmount
                )

                _returnResult.value = result
                _isProcessing.value = false
                onComplete(true)

            } catch (e: Exception) {
                e.printStackTrace()
                _isProcessing.value = false
                onComplete(false)
            }
        }
    }
}