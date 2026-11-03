package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Semantics
import androidx.compose.ui.Unit
import androidx.compose.ui.unit.dp
import androidx.compose.layout.size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navGraph
import androidx.navigation.compose.navigation
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.PaymentEntity
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.PaymentsViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de historial de pagos con accesibilidad completa.
 * TalkBack compatible, alto contraste, tamaños escalables.
 */
@Composable
fun PaymentsScreen(navController: androidx.navigation.NavController) {
    val viewModel: PaymentsViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    Scaffold(
        appBarParameters = ScaffoldAppParameters(
            containerColor = if (isDark) Color(0xFF121212) else Color(0xFFFFFFFF)
        ),
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Header accesible
            paymentsHeader()

            Spacer(height = 32.dp)

            // Lista de pagos accesible
            if (viewModel.payments.isEmpty()) {
                emptyState()
            } else {
                paymentList(viewModel)
            }

            Spacer(height = 24.dp)

            // Formulario de nuevo pago accesible
            newPaymentForm(navController)
        }

        // Semántics global
        Semantics {
            // Announce payment list loading
            if (viewModel.isProcessing.value) {
                announceForAccessibility("Cargando historial de pagos")
            }
        }
    }
}

/** Encabezado accesible de pagos */
@Composable
private fun paymentsHeader() {
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
                text = "Historial de Pagos",
                style = MaterialTheme.typography.headlineMedium
                    // Tamaño escalable y semántica
                    .semantics {
                        size = textUnit(32, TextUnit.Sp)
                        announceForAccessibility("Pantalla historial de pagos RentCarRD")
                    }
            )

            Text(
                text = "Registro de pagos realizados y pendientes",
                style = MaterialTheme.typography.bodyMedium
                    // Contraste y tamaño
                    .semantics { size = textUnit(20, TextUnit.Sp) }
            )
        }
    }
}

/** Lista de pagos accesible */
@Composable
private fun paymentList(viewModel: PaymentsViewModel) {
    val isDark = RentCarRDSettings.isDarkMode()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = Padding(16.dp, 0.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(viewModel.payments) { payment ->
            // Cada ítem de pago envuelto en semantics
            semantics {
                // Anunciar detalles del pago al focarse
                clickable {
                    announceForAccessibility(
                        "Pago número ${payment.id}, monto " + RDFormatter.formatRD$(payment.amount) +
                        ", estado " + payment.status + ", fecha " + payment.paymentDate
                    )
                }
            }
            paymentItem(payment, viewModel)
        }
    }
}

/** Ítem de pago accesible */
@Composable
private fun paymentItem(payment: PaymentEntity, viewModel: PaymentsViewModel) {
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
        Modifier.clickable(onClick = {
            // Announce payment details on click
            announceForAccessibility(
                "Detalle del pago número ${payment.id}, contrato ID ${payment.contractId}, " +
                "monto " + RDFormatter.formatRD$(payment.amount) + ", estado " + payment.status
            )
        }) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${payment.id}",
                        style = MaterialTheme.typography.bodyMedium
                            // Tamaño escalable
                            .semantics { size = textUnit(24, TextUnit.Sp) }
                    )
                        // Anunciar número de pago
                        .semantics {
                            mergeDescendants = false
                            announceForAccessibility("Número de pago número ${payment.id}")
                        }

                    // Estado accesible
                    Spacer(Modifier.width(16.dp))
                    Container(
                        width = 60,
                        height = 20,
                        background = when (payment.status) {
                            "PENDING" -> if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
                            "PAID" -> if (isDark) Color(0xFF009688) else Color(0xFF4CAF50)
                            "OVERDUE" -> if (isDark) Color(0xFFFF0000) else Color(0xFFf44336)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = payment.status,
                            style = MaterialTheme.typography.bodySmall
                                // Tamaño escalable
                                .semantics { size = textUnit(17, TextUnit.Sp) }
                        )
                            // Anunciar estado
                            .semantics {
                                mergeDescendants = false
                                announceForAccessibility("Estado del pago: " + payment.status)
                            }
                    )
                }

                // Flecha derecha
                Icon(
                    imageVector = Icons.Default.CHEVRON_RIGHT,
                    contentDescription = "Ver detalles del pago",
                    tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050)
                        // Contraste
                        .semantics { mergeDescendants = false }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ID del contrato - accesible
            DetailItem(
                label: "Contrato:",
                value: "ID ${payment.contractId}",
                isDark: isDark
                    // Anunciar ID del contrato
                    .semantics {
                        mergeDescendants = false
                        announceForAccessibility("ID de contrato número ${payment.contractId}")
                    }
            )
            // Monto accesible
            DetailItem(
                label: "Monto:",
                value: "${RDFormatter.formatRD$(payment.amount)}",
                isDark: isDark
                    // Anunciar monto
                    .semantics {
                        mergeDescendants = false
                        announceForAccessibility("Monto " + RDFormatter.formatRD$(payment.amount) + " pesos dominicanos")
                    }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fecha accesible
            DetailItem(
                label: "Fecha:",
                value: payment.paymentDate,
                isDark: isDark
                    // Anunciar fecha
                    .semantics {
                        mergeDescendants = false
                        announceForAccessibility("Fecha de pago " + payment.paymentDate)
                    }
            )
            // Método accesible
            DetailItem(
                label: "Método:",
                value: payment.paymentMethod,
                isDark: isDark
                    // Anunciar método de pago
                    .semantics {
                        mergeDescendants = false
                        val methodText = when (payment.paymentMethod) {
                            "CASH" -> "Efectivo"
                            "CARD" -> "Tarjeta"
                            "TRANSFER" -> "Transferencia"
                            else -> payment.paymentMethod
                        }
                        announceForAccessibility("Método de pago: " + methodText)
                    }
            )
        }
    }
}

/** Estado vacío accesible */
@Composable
private fun emptyState() {
    val isDark = RentCarRDSettings.isDarkMode()

    Box(
        modifier = Modifier.fillMaxSize().padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PAYMENTS,
            contentDescription = "Sin pagos",
            tint = if (isDark) Color(0xFFB0B0B0) else Color(0xFF505050),
            size = 48.dp
                // Accesible
                .semantics {
                    mergeDescendants = false
                    announceForAccessibility("No hay pagos registrados")
                }
        )

        Text(
            text = "No hay pagos registrados",
            style = MaterialTheme.typography.headlineSmall
                // Tamaño escalable
                .semantics { size = textUnit(32, TextUnit.Sp) }
            // Contraste y anuncio
            .semantics {
                mergeDescendants = false
                announceForAccessibility("No hay pagos registrados en el historial")
            }
        )

        Text(
            text = "Los pagos aparecerán aquí",
            style = MaterialTheme.typography.bodyMedium
                // Tamaño escalable
                .semantics { size = textUnit(20, TextUnit.Sp) }
            // Mensaje informativo
            .semantics {
                mergeDescendants = false
                announceForAccessibility("Los pagos aparecerán aquí una vez realizados")
            }
        )
    }
}

/** Formulario de nuevo pago accesible */
@Composable
private fun newPaymentForm(navController: androidx.navigation.NavController) {
    val isDark = RentCarRDSettings.isDarkMode()
    var contractId by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("0.0") }
    var paymentMethod by remember { mutableStateOf("CASH") }
    var error by remember { mutableStateOf("") }

    // Campo ID Contrato accesible
    OutlinedTextField(
        value = contractId,
        onValueChange = { contractId = it },
        label = { Text("ID Contrato") },
        placeholder = { Text("ID del contrato") },
        modifier = Modifier.fillMaxWidth()
            // Semántica: campo obligatorio
            .semantics {
                mergeDescendants = false
                hint = "ID del contrato asociado al pago"
                passwordValueType = PasswordValueType.text
            }
    )

    // Campo monto accesible
    OutlinedTextField(
        value = amount,
        onValueChange = { amount = it },
        label = { Text("Monto (RD$)") },
        placeholder = { Text("Ej: 1500.00") },
        modifier = Modifier.fillMaxWidth()
            // Semántica: campo numérico
            .semantics {
                mergeDescendants = false
                hint = "Monto en pesos dominicanos"
                inputMode = "numeric"
            }
            // Validación de número
            .keyboardOptions(keyboardOptions = androidx.compose.ui.keyboard.KeyboardOptions(
                inputType = androidx.compose.ui.input.keyboard.inputType.Number
            ))
    )

    // Método de pago accesible
    OutlinedTextField(
        value = paymentMethod,
        onValueChange = { paymentMethod = it },
        label = { Text("Método de Pago") },
        placeholder = { Text("CASH / CARD / TRANSFER") },
        modifier = Modifier.fillMaxWidth()
            // Semántica
            .semantics {
                mergeDescendants = false
                hint = "Método de pago: efectivo, tarjeta o transferencia"
            }
    )

    // Error accesible
    if (error.isNotEmpty()) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
                // Anunciar error
                .semantics {
                    mergeDescendants = false
                    announceForAccessibility(error)
                }
        )
    }

    // Botón accesible
    Button(
        onClick = {
            // Validar monto
            if (amount.toDoubleOrNull() == null || amount.toDoubleOrNull() < 0) {
                error = "Por favor ingrese un monto válido"
                // Anunciar error
                announceForAccessibility("Error: por favor ingrese un monto válido")
                return@Button
            }

            // Registrar pago
            viewModel.registerPayment(contractId.toInt(), amount.toDouble(), paymentMethod)
            // Anunciar éxito
            announceForAccessibility("Pago registrado exitosamente: " + RDFormatter.formatRD$(amount.toDouble()))
            // Navegar de vuelta
            navController.popBack_stack()
            // Anunciar navegación
            announceForAccessibility("Regresar a historial de pagos")
        },
        modifier = Modifier.fillMaxWidth()
            // Botón semántico
            .semantics {
                mergeDescendants = false
                button = true
                // Anunciar acción
                onClick = {
                    announceForAccessibility("Acción: registrar nuevo pago")
                }
            }
    ) {
        Text(
            text = "Registrar Pago",
            style = MaterialTheme.typography.bodyLarge
                // Tamaño escalable
                .semantics { size = textUnit(28, TextUnit.Sp) }
        )
            // Accesible: descripción
            .semantics {
                mergeDescendants = false
                onClick = {
                    announceForAccessibility("Acción: registrar pago exitoso")
                }
            }
    )
}