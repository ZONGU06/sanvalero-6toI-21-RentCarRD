package com.sanvalero.rentcarrd.ui.dashboard

import com.sanvalero.rentcarrd.domain.Model.Contract
import com.sanvalero.rentcarrd.domain.Model.Vehicle
import com.sanvalero.rentcarrd.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewmodel.extending.ViewModelKt.viewModel

/**
 * ViewModel para la pantalla Dashboard.
 * Proporciona datos y estado para el gerente general.
 */
@viewModel
class DashboardViewModel(
    private val userRepository: UserRepository
) : UserRepository by userRepository {

    // Estado observable para el dashboard
    private val _totalVehicles = MutableStateFlow(0)
    val totalVehicles: StateFlow<Int> = _totalVehicles

    private val _totalActiveContracts = MutableStateFlow(0)
    val totalActiveContracts: StateFlow<Int> = _totalActiveContracts

    private val _totalPayments = MutableStateFlow(0)
    val totalPayments: StateFlow<Int> = _totalPayments

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue

    init {
        loadInitialData()
    }

    /** Carga datos iniciales para el dashboard */
    private fun loadInitialData() {
        // TODO: Implementar carga real de datos desde el repositorio
        // Por ahora valores placeholder
        _totalVehicles.value = 50
        _totalActiveContracts.value = 23
        _totalPayments.value = 127
        _totalRevenue.value = 15500.50
    }

    /** Actualiza el indicador de vehículos totales */
    fun updateVehicleCount(count: Int) {
        _totalVehicles.value = count
    }

    /** Actualiza el indicador de contratos activos */
    fun updateActiveContractCount(count: Int) {
        _totalActiveContracts.value = count
    }

    /** Actualiza el indicador de pagos */
    fun updatePaymentCount(count: Int) {
        _totalPayments.value = count
    }

    /** Actualiza el ingreso total */
    fun updateRevenue(amount: Double) {
        _totalRevenue.value = amount
    }
}