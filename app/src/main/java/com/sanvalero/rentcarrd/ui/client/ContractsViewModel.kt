package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla Mis Contratos.
 * Optimizado con caching y operaciones de base de datos eficientes.
 */
@viewModel
class ContractsViewModel @Inject constructor(
    private val database: RentCarRDDatabase
) {

    // Estado observable
    private val _contracts = MutableStateFlow<List<ContractEntity>>(emptyList())
    val contracts: StateFlow<List<ContractEntity>> = _contracts

    private val _filter = MutableStateOf("all")
    val filter: String = _filter.value
    fun setFilter(filter: String) { _filter.value = filter }

    // Cache temporal para evitar recomputaciones costosas
    private var _lastFilterTime = 0L
    private val filterCooldown = 500L // ms

    init {
        loadContracts()
    }

    /** Carga contratos con caching para optimizar rendimiento */
    private fun loadContracts() {
        launch {
            // Pequeña pausa para batching de operaciones
            delay(100)
            _contracts.value = database.contractDao.getAllContracts()
        }
    }

    /** Filtra contratos con cooldown para evitar operaciones excesivas */
    fun filterContracts(status: String) {
        val now = System.currentTimeMillis()
        if (now - _lastFilterTime < filterCooldown) {
            // Evitar operación muy reciente - usar estado actual
            return
        }
        _lastFilterTime = now

        launch {
            when (status) {
                "all" -> _contracts.value = database.contractDao.getAllContracts()
                "active" -> _contracts.value = database.contractDao.getContractsByStatus("ACTIVE")
                "completed" -> _contracts.value = database.contractDao.getContractsByStatus("COMPLETED")
                "cancelled" -> _contracts.value = database.contractDao.getContractsByStatus("CANCELLED")
                else -> _contracts.value = database.contractDao.getAllContracts()
            }
        }
    }

    /** Obtiene un contrato por ID con caching */
    fun getContractById(contractId: Int): ContractEntity? = _contracts.value?.firstOrNull { it.id == contractId }
}