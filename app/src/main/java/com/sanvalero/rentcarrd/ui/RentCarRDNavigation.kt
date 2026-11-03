package com.sanvalero.rentcarrd.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.sanvalero.rentcarrd.ui.auth.LoginScreen
import com.sanvalero.rentcarrd.ui.dashboard.DashboardScreen
import com.sanvalero.rentcarrd.ui.client.CatalogScreen
import com.sanvalero.rentcarrd.ui.client.RentalsScreen
import com.sanvalero.rentcarrd.ui.operator.OperationsScreen
import com.sanvalero.rentcarrd.ui.admin.AdminScreen
import com.sanvalero.rentcarrd.ui.auth.RegisterScreen
import com.sanvalero.rentcarrd.ui.components.AppBar

/**
 * Configuración de navegación para RentCarRD usando Navigation Compose.
 */
fun RentCarRDNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login",
        routeSupport = true
    ) {
        // Pantalla de login inicial
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // Dashboard (accesible desde roles apropiados)
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        // Pantalla de catálogo de vehículos (cliente)
        composable("catalog") {
            CatalogScreen(navController = navController)
        }

        // Pantalla de mis alquileres (cliente)
        composable("my-rentals") {
            RentalsScreen(navController = navController)
        }

        // Pantalla de operaciones (operador)
        composable("operations") {
            OperationsScreen(navController = navController)
        }

        // Pantalla de administración (administrador)
        composable("admin") {
            AdminScreen(navController = navController)
        }
    }
}