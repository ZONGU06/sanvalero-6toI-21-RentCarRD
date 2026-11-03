package com.sanvalero.rentcarrd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.compose.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sanvalero.rentcarrd.ui.NavigationGraph
import com.sanvalero.rentcarrd.ui.theme.RentCarRDSettings

/**
 * Actividad principal de RentCarRD con navegación Hilt-integrada.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aplicar tema RentCarRD (oscuro por defecto con acento rojo)
        RentCarRDSettings.applyTheme(this)

        setContent {
            val navController = rememberNavController()

            // Construir el grafo de navegación con integración Hilt
            NavigationGraph.buildGraph(navController = navController)

            // Aplicar tema a la aplicación
            RentCarRDSettings.applyTheme(this)
        }
    }
}