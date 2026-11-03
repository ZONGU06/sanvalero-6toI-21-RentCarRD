package com.sanvalero.rentcarrd.ui.client

import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.PaymentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Pagos.
 * Administra el historial de pagos y el formulario de registro.
 */
@viewModel
class PaymentsViewModel @Inject constructor(
    // Inyección de base de datos mediante Hilt en producción
) {

    // Estado observable
    private val _payments = MutableStateFlow<List<PaymentEntity>>(emptyList())
    val payments: StateFlow<List<PaymentEntity>> = _payments

    init {
        // Cargar pagos en inicialización
        loadPayments()
    }

    /** Carga el historial de pagos desde la base de datos */
    private fun loadPayments() {
        launch {
            // TODO: En producción: RentCarRDDatabase.getInstance(context).paymentDao.getAllPayments()
            // Por ahora datos placeholder
            val placeholderPayments = listOf(
                PaymentEntity(
                    id = 1,
                    contractId = 1,
                    amount = 1500.0,
                    paymentDate = "15/12/2024",
                    paymentMethod = "CASH",
                    status = "PAID"
                ),
                PaymentEntity(
                    id = 2,
                    contractId = 1,
                    amount = 750.0,
                    paymentDate = "01/12/2024",
                    paymentMethod = "TRANSFER",
                    status = "PENDING"
                )
            )
            _payments.value = placeholderPayments
        }
    }

    /** Registra un nuevo pago */
    fun registerPayment(contractId: Int, amount: Double, paymentMethod: String) {
        launch {
            // TODO: Crear PaymentEntity y guardar en BD
            // TODO: Actualizar contrato si está pendiente
            // TODO: Navegar de vuelta o mostrar confirmación
        }
    }
}