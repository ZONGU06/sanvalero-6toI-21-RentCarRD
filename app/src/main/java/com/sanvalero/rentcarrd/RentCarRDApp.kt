package com.sanvalero.rentcarrd

import android.app.Application
import com.sanvalero.rentcarrd.data.DatabaseSeeder
import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RentCarRDApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar Hilt - los módulos se configuran en el componente Hilt
        // Hilt.init()

        // Ejecutar seeding de base de datos en la primera apertura
        // Esto creará la cuenta administrativa inicial y configuraciones básicas
        DatabaseSeeder.seedDatabase(this)

        // Inicializar la base de datos para asegurar que existe
        val db = RentCarRDDatabase.getInstance(this)
    }
}