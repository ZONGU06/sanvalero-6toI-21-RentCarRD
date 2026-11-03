package com.sanvalero.rentcarrd.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Semantics
import androidx.compose.ui.Unit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.size
import androidx.compose.ui.text.textOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navGraph
import androidx.navigation.compose.navigation
import com.sanvalero.rentcarrd.R
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.ReturnViewModel
import com.sanvalero.rentcarrd.viewmodel.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.inject

/**
 * Pantalla de devolución de vehículo para clientes.
 * Incluye compatibilidad de accesibilidad (TalkBack, alto contraste).
 */
@Composable
fun ReturnScreen(navController: androidx.navigation.NavController) {
    val viewModel: ReturnViewModel = viewModel()
    val isDark = RentCarRDSettings.isDarkMode()

    var returnDate by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

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
            returnHeader()

            Spacer(height = 32.dp)

            // Formulario accesible
            returnForm(returnDate, error, viewModel, navController)

            Spacer(height = 24.dp)

            // Resultado accesible
            if (viewModel.isProcessing.value == false && viewModel.returnResult.value != null) {
                resultCard(viewModel.returnResult.value!!, isDark)
            }
        }

        // Semántics global para TalkBack
        Semantics {
            // Anunciar el estado principal de la pantalla
            if (viewModel.isProcessing.value) {
                announceForAccessibility("Procesando devolución de vehículo")
            } else if (viewModel.returnResult.value != null) {
                val r = viewModel.returnResult.value!!
                announceForAccessibility(
                    "Devolución completada, ${r.vehicleBrand} ${r.vehicleModel}, " +
                    "días retraso ${r.delayDays}"
                )
            }
        }
    }
}

/** Encabezado accesible de la pantalla de devolución */
@Composable
private fun returnHeader() {
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
            // Título accesible
            Semantics {
                announceForAccessibility("Pantalla registro de devolución de vehículo RentCarRD")
            }
            Text(
                text = "Registro de Devolución",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isDark) Color.White : Color.Black
                    // Tamaño escalable
                    .semantics { size = textUnit(32, TextUnit.Sp) }
            )

            Texto(
                text = "Ingrese la fecha real de devolución del vehículo",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFB0B0B0) : Color(0xFF505050)
                    // Contraste y tamaño
                    .semantics { size = textUnit(20, TextUnit.Sp) }
            )
        }
    }
}

/** Formulario accesible de devolución */
@Composable
private fun returnForm(
    returnDate: String,
    error: String,
    viewModel: ReturnViewModel,
    navController: androidx.navigation.NavController
) {
    val isDark = RentCarRDSettings.isDarkMode()

    // Fecha accesible
    OutlinedTextField(
        value = returnDate,
        onValueChange = { returnDate = it },
        label = { Text("Fecha de Devolución Real (dd/MM/yyyy)") },
        placeholder = { Text("dd/MM/yyyy") },
        modifier = Modifier.fillMaxWidth()
            // Semántica: campo de fecha obligatorio
            .semantics {
                mergeDescendants = false
                hint = "Fecha de devolución del vehículo en formato dd/MM/yyyy"
                passwordValueType = PasswordValueType.text
            }
    )

    // Mensaje de error accesible
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
            // Validar fecha
            if (returnDate.isEmpty()) {
                error = "Por favor ingrese la fecha de devolución"
                // Anunciar error
                announceForAccessibility("Error: por favor ingrese la fecha de devolución")
                return@Button
            }

            // Procesar devolución
            viewModel.processReturn(returnDate) { success ->
                if (success) {
                    // Navegar de vuelta
                    navController.popBack_stack()
                    // Anunciar éxito
                    announceForAccessibilidad("Devolución registrada exitosamente")
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
            // Botón semántico
            .semantics {
                mergeDescendants = false
                button = true
                // Anunciar acción del botón
                onClick = {
                    announceForAccessibility("Botón registrar devolución presionado")
                }
            }
    ) {
        Text(
            text = "Registrar Devolución",
            style = MaterialTheme.typography.bodyLarge
                // Tamaño escalable
                .semantics { size = textUnit(28, TextUnit.Sp) }
        )
            // Accesible: describir la acción del botón
            .semantics {
                mergeDescendants = false
                onClick = {
                    announceForAccessibility("Acción: registrar devolución de vehículo")
                }
            }
    )
}

/** Card de resultado accesible */
@Composable
private fun resultCard(result: ReturnResult, isDark: Boolean) {
    val penaltyInfo = result.isLate
        ? "Mora aplicada: ${RDFormatter.formatRD$(result.lateFee)} (${result.delayDays} días retraso)"
        : "Devolución a tiempo - No hay moras"

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        elevation = 2.dp,
        colors = CardColors(
            defaultColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
            pressed = if (isDark) Color(0xFF2C2C2C) else Color(0xFFE0E0E0),
            selected = if (isDark) Color(0xFF3A3A3A) else Color(0xFFBBDEFB)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Título accesible
            Semantics {
                announceForAccessibility("Resultado del registro de devolución")
            }
            Text(
                text = "Resultado del Registro",
                style = MaterialTheme.typography.headlineSmall
                    // Tamaño escalable
                    .semantics { size = textUnit(36, TextUnit.Sp) }
            )

            // Información del vehículo
            Text(
                text = "${result.vehicleBrand} ${result.vehicleModel}",
                style = MaterialTheme.typography.bodyMedium
                    // Contraste y tamaño
                    .semantics { size = textUnit(24, TextUnit.Sp) }
            )

            // Fecha de devolución
            Text(
                text = "Fecha Devolución: ${result.returnDate}",
                style = MaterialTheme.typography.bodySmall
                    // Anunciar fecha
                    .semantics {
                        mergeDescendants = false
                        announceForAccessibility("Fecha de devolución ${result.returnDate}")
                    }
            )

            // Fecha esperada
            Text(
                text = "Fecha Esperada: ${result.expectedDate}",
                style = MaterialTheme.typography.bodySmall
                    // Anunciar fecha esperada
                    .semantics {
                        mergeDescendants = false
                        announceForAccessibility("Fecha esperada ${result.expectedDate}")
                    }
            )

            // Información de mora
            Text(
                text = penaltyInfo,
                style = MaterialTheme.typography.bodyMedium
                    // Color según estado
                    .color = if (result.isLate) Color.Red else Color.Green
                    // Anunciar información de mora
                    .semantics {
                        mergeDescendants = false
                        if (result.isLate) {
                            announceForAccessibility("Mora aplicada: " + RDFormatter.formatRD$(result.lateFee) + " pesos dominicanos")
                        } else {
                            announceForAccessibility("Devolución dentro del plazo acordado")
                        }
                    }
            )

            // Total
            Text(
                text = "Total: ${RDFormatter.formatRD$(result.totalAmount)}",
                style = MaterialTheme.typography.titleMedium
                    // Tamaño escalable
                    .semantics { size = textUnit(32, TextUnit.Sp) }
            )

            // Días de retraso (si aplica)
            if (result.isLate) {
                Text(
                    text = "Días de retraso: ${result.delayDays}",
                    style = MaterialTheme.typography.bodySmall
                        .color = if (isDark) Color(0xFFFFFFFF) else Color.Red
                        // Anunciar días de retraso
                        .semantics {
                            mergeDescendants = false
                            announceForAccessibility("Días de retraso: ${result.delayDays}")
                        }
                )
            }

            // Botón cerrar accesible
            Button(
                onClick = {
                    navController.popBack_stack()
                    // Anunciar cierre
                    announceForAccessibility("Cerrar pantalla de devolución")
                },
                modifier = Modifier.fillMaxWidth()
                    .semantics {
                        mergeDescendants = false
                        button = true
                        onClick = {
                            announceForAccessibility("Acción: cerrar pantalla de devolución")
                        }
                    }
            ) {
                Text("Cerrar", modifier = Modifier.align(Alignment.Center)
                    // Tamaño escalable
                    .semantics { size = textUnit(24, TextUnit.Sp) }
                )
                    // Accesible: descripción del botón
                    .semantics {
                        mergeDescendants = false
                        onClick = {
                            announceForAccessibility("Acción: cerrar pantalla")
                        }
                    }
            )
        }
    }
}