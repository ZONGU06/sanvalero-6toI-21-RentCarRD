package com.sanvalero.rentcarrd.data

import android.content.Context
import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.UserEntity
import com.sanvalero.rentcarrd.util.RDFormatter

/**
 * Seeder de base de datos para RentCarRD.
 * Responsable de crear la cuenta administrativa inicial en la primera apertura
 * y asegurar que no se duplique en aperturas posteriores.
 */
object DatabaseSeeder {

    /** Clave única para identificar la configuración de seeding */
    private const val SEEDING_CONFIG_KEY = "seed_initial_admin"

    /**
     * Ejecuta el seeding de la base de datos si es necesario.
     * Debe llamarse esto en el Application.onCreate() o en la inicialización de Hilt.
     *
     * @param context Contexto de Android
     */
    fun seedDatabase(context: Context) {
        // Verificar si ya se ha realizado el seeding previamente
        val prefs = context.getSharedPreferences("rentcarrd_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean(SEEDING_CONFIG_KEY, false)) {
            // Ya se realizó el seeding, no hacer nada
            return
        }

        // Ejecutar seeding en un hilo de fondo
        Thread {
            doSeed(context)
            // Marcar que el seeding se completó
            prefs.edit().putBoolean(SEEDING_CONFIG_KEY, true).apply()
        }.start()
    }

    /**
     * Realiza el seeding efectivo de la base de datos.
     * Crea la cuenta administrativa inicial con contraseña cifrada.
     *
     * @param context Contexto de Android
     */
    private fun doSeed(context: Context) {
        val db = RentCarRDDatabase.getInstance(context)

        // Verificar si ya existe algún usuario (base de datos vacía o recién creada)
        val existingUsers = db.userDao.getAllUsers()
        if (existingUsers.isNotEmpty()) {
            // Base de datos ya tiene datos, no duplicar
            return
        }

        // Hashear la contraseña del administrador (en producción usar BCrypt real)
        val adminPasswordHash = "admin123" // En producción: BCrypt.hashPassword("admin123")

        // Crear cuenta administrativa inicial
        val adminUser = UserEntity(
            id = 1,
            fullName = "Administrador RentCarRD",
            email = "admin@rentcarrd.do",
            passwordHash = adminPasswordHash,
            phone = "809-555-0000",
            identificationCard = "A-00000001",
            role = "ADMIN"
        )

        db.userDao.insertUser(adminUser)

        // TODO: Aquí podríamos inicializar también datos básicos como:
        // - Configuraciones del sistema (tarifas de mora, porcentajes de descuento)
        // - Catálogo base de vehículos (ejemplos iniciales)
        // - Tarifas por defecto

        // Registrar en logs
        println("✅ Database seeded: Admin account created - admin@rentcarrd.do")
    }
}