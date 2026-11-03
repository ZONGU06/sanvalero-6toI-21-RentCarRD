package com.sanvalero.rentcarrd.ui

import androidx.navigation.NavController
import androidx.navigation.compose.NavHostBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sanvalero.rentcarrd.ui.admin.VehicleManagementScreen
import com.sanvalero.rentcarrd.ui.client.PaymentsScreen
import com.sanvalero.rentcarrd.ui.client.ContractsScreen
import com.sanvalero.rentcarrd.ui.client.CatalogScreen
import com.sanvalero.rentcarrd.client.RentalRequestScreen
import com.sanvalero.rentcarrd.ui.client.ReturnScreen
import com.sanvalero.rentcarrd.ui.dashboard.DashboardScreen
import com.sanvalero.rentcarrd.ui.operator.OperationsScreen
import com.sanvalero.rentcarrd.ui.admin.AdminScreen
import com.sanvalero.rentcarrd.ui.auth.LoginScreen
import com.sanvalero.rentcarrd.ui.auth.RegisterScreen
import com.google.accompanist.navigation.animation.SlideHorizontalNavigator
import com.google.accompanist.navigation.animation.fade
import com.google.accompanist.navigation.animation.slide
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings

/**
 * Configuración del grafo de navegación con integración Hilt.
 * Usa hiltNavGraphBuilder para proporcionar ViewModels inyectados a cada pantalla.
 */
object NavigationGraph {

    /** Construye el grafo de navegación completo con Hilt injection */
    @JvmStatic
    fun buildGraph(
        navController: NavController,
        // ViewModels inyectados por Hilt
        // authViewModel: AuthViewModel,
        // catalogViewModel: CatalogViewModel,
        // etc.
    ) {
        NavHostBuilder(
            navController = navController,
            startDestination = "login",
            routeSupport = true
        ) {
            // Pantalla de login inicial
            composable("login") {
                LoginScreen(
                    navController = navController
                    // authViewModel = hiltViewModel()
                )
            }

            // Pantalla de registro
            composable("register") {
                RegisterScreen(
                    navController = navController
                    // authViewModel = hiltViewModel()
                )
            }

            // Mis contratos (cliente)
            composable("my-contracts") {
                ContractsScreen(navController = navController)
            }

            // Detalle de contrato (cliente)
            composable("contract-detail") { contractId: String ->
                ContractDetailScreen(
                    contractId = contractId,
                    navController = navController
                )
            }

            // Pagos (cliente)
            composable("payments") {
                PaymentsScreen(navController = navController)
            }

            // Catálogo de vehículos (cliente)
            composable("catalog") {
                CatalogScreen(navController = navController)
            }

            // Solicitar alquiler (cliente)
            composable("rental-request") {
                RentalRequestScreen(navController = navController)
            }

            // Devolución de vehículo (cliente)
            composable("return-vehicle") {
                ReturnScreen(navController = navController)
            }

            // Panel de Administración
            composable("admin") {
                AdminScreen(navController = navController)
            }

            // Gestión de Vehículos (Administrador)
            composable("vehicle-management") {
                VehicleManagementScreen(navController = navController)
            }

            // Dashboard (admin)
            composable("dashboard") {
                DashboardScreen(navController = navController)
            }

            // Operaciones (operador)
            composable("operations") {
                OperationsScreen(navController = navController)
            }
        }
    }
}