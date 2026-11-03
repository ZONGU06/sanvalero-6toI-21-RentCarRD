package com.sanvalero.rentcarrd.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanvalero.rentcarrd.di.HiltViewModel
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings
import com.sanvalero.rentcarrd.viewmodel.AuthViewModel
import com.symbol.k2.*

/**
 * Pantalla de login para la aplicación RentCarRD.
 * Permite a clientes y operadores iniciar sesión.
 */
@Composable
fun LoginScreen(navController: androidx.navigation.NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

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
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleMedium,
            color = RentCarRDSettings.isDarkMode() ? Color(0xFFB0B0B0) : Color(0xFF505050),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Formulario de login
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
                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("ejemplo@dominicana.do") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (!email.isEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar email",
                                tint = RentCarRDSettings.isDarkMode() ? Color.White : Color.Black
                            )
                        } else {
                            EmptyField()
                        }
                    }
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    placeholder = { Text("••••••••") },
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true,
                    trailingIcon = {
                        if (!password.isEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar contraseña",
                                tint = RentCarRDSettings.isDarkMode() ? Color.White : Color.Black
                            )
                        } else {
                            EmptyField()
                        }
                    }
                )

                // Mensaje de error
                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Botón de login
                Button(
                    onClick = {
                        // TODO: Validar credenciales e iniciar sesión
                        if (email.isEmpty() || password.isEmpty()) {
                            error = "Por favor complete todos los campos"
                        } else {
                            // Navegar al dashboard
                            navController.navigate("dashboard")
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
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                Spacer(height = 24.dp)

                // Enlace de registro
                Button(
                    onClick = {
                        navController.navigate("register")
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
                        text = "No tiene una cuenta? Regístrate",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (RentCarRDSettings.isDarkMode()) Color(0xFFB0B0B0) else Color(0xFF505050)
                    )
                }
            }
        }

        Spacer(height = 32.dp)
    }
}

/**
 * Composable auxiliar para campo vacío con icono.
 */
@Composable
private fun EmptyField() {
    Box(modifier = Modifier.size(20.dp)) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Estado del campo",
            tint = if (RentCarRDSettings.isDarkMode()) Color.White else Color.Black,
        )
    }
}