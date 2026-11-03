package com.sanvalero.rentcarrd.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.di.HiltViewModel
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.AuthViewModel

/**
 * Pantalla de registro para nueva cuenta de usuario RentCarRD.
 * Permite registrarse como cliente u operador.
 */
@Composable
fun RegisterScreen(navController: androidx.navigation.NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var identificationCard by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("CLIENT") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(height = 32.dp)

        // Título
        Text(
            text = "RentCarRD",
            style = MaterialTheme.typography.headlineLarge,
            color = RentCarRDSettings.isDarkMode() ? Color.White : Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Subtítulo
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.titleMedium,
            color = RentCarRDSettings.isDarkMode() ? Color(0xFFB0B0B0) : Color(0xFF505050),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Formulario de registro
        Card(
            modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp),
            elevation = 4.dp,
            colors = CardColors(
                defaultColor = if (RentCarRDSettings.isDarkMode()) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
                pressed = if (RentCarRDSettings.isDarkMode()) Color(0xFF2C2C2C) else Color(0xFFE0E0E0),
                selected = if (RentCarRDSettings.isDarkMode()) Color(0xFF3A3A3A) else Color(0xFFBBDEFB)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Campo Nombre completo
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nombre Completo") },
                    placeholder = { Text("Ejemplo: Juan Pérez") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("ejemplo@dominicana.do") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Teléfono
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    placeholder = { Text("+1 809-123-4567") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Cédula/RNC
                OutlinedTextField(
                    value = identificationCard,
                    onValueChange = { identificationCard = it },
                    label = { Text("Cédula / RNC") },
                    placeholder = { Text("123-4567890-1") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    placeholder = { Text("••••••••") },
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )

                // Selector de rol
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rol:", modifier = Modifier.width(80.dp), color = if (RentCarRDSettings.isDarkMode()) Color.White else Color.Black)
                    OutlinedButton(
                        selected = role == "CLIENT",
                        onClick = { role = "CLIENT" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cliente")
                    }
                    OutlinedButton(
                        selected = role == "OPERATOR",
                        onClick = { role = "OPERATOR" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Operador")
                    }
                }

                // Mensaje de error
                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Botón de registro
                Button(
                    onClick = {
                        // TODO: Registrar nuevo usuario con validación
                        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            error = "Por favor complete todos los campos obligatorios"
                        } else {
                            // Navegar al login
                            navController.navigate("login")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(
                        defaultColor = if (RentCarRDSettings.isDarkMode()) Color(0xFFD32F2F) else Color(0xFFD32F2F),
                        enabled = if (RentCarRDSettings.isDarkMode()) Color(0xFFD32F2F) else Color(0xFFD32F2F),
                        pressed = if (RentCarRDSettings.isDarkMode()) Color(0xFFB71C1C) else Color(0xFFAB4B4D),
                        disabled = if (RentCarRDSettings.isDarkMode()) Color(0xFF424242) else Color(0xFFBBDEFB)
                    )
                ) {
                    Text(
                        text = "Registrarse",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                Spacer(height = 24.dp)

                // Enlace de login
                Button(
                    onClick = {
                        navController.navigate("login")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(
                        defaultColor = Color.Transparent,
                        enabled = Color.Transparent,
                        pressed = if (RentCarRDSettings.isDarkMode()) Color(0xFF37474F) else Color(0xFF90A4AE),
                        disabled = Color.Transparent
                    )
                ) {
                    Text(
                        text = "Ya tiene una cuenta? Iniciar sesión",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (RentCarRDSettings.isDarkMode()) Color(0xFFB0B0B0) else Color(0xFF505050)
                    )
                }
            }
        }

        Spacer(height = 32.dp)
    }
}