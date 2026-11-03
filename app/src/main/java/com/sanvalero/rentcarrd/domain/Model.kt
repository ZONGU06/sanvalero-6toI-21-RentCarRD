package com.sanvalero.rentcarrd.domain

/**
 * Modelo de datos para un contrato de alquiler o mantenimiento.
 * Define la estructura de negocio para los contratos.
 */
data class Contract(
    val id: Int,
    val contractNumber: String,
    val contractType: String, // "RENTAL" o "MAINTENANCE"
    val clientId: Int,
    val vehicleId: Int?, // nullable
    val startDate: String, // formato dd/MM/yyyy
    val expectedEndDate: String, // formato dd/MM/yyyy
    var actualEndDate: String?, // nullable
    val baseAmount: Double,
    val extraCharges: Double,
    val discounts: Double,
    val totalAmount: Double,
    val status: String, // "ACTIVE", "VERIFIED", "COMPLETED", "CANCELLED"
    var isDeleted: Boolean = false
)

/**
 * Modelo de datos para un vehículo.
 */
data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val category: String,
    val transmission: String,
    val passengers: Int,
    val fuelType: String,
    val dailyRate: Double,
    val status: String, // "AVAILABLE", "RENTED", "MAINTENANCE", "INACTIVE"
    val imageUri: String = ""
)

/**
 * Modelo de datos para un servicio de mantenimiento.
 */
data class MaintenanceService(
    val id: Int,
    val contractId: Int,
    val vehicleBrand: String,
    val vehicleModel: String,
    val vehicleYear: Int,
    val vehicleColor: String,
    val licensePlate: String,
    val mileage: Double,
    val issueDescription: String,
    val serviceType: String,
    val laborCost: Double,
    val partsCost: Double,
    val promisedCompletionDate: String, // formato dd/MM/yyyy
    var actualCompletionDate: String?, // nullable
    val maintenanceStatus: String // "PENDING", "IN_PROGRESS", "COMPLETED", "DELAYED"
)

/**
 * Modelo de datos para un pago.
 */
data class Payment(
    val id: Int,
    val contractId: Int,
    val amount: Double,
    val paymentDate: String, // formato dd/MM/yyyy
    val paymentMethod: String, // "CASH", "CARD", "TRANSFER"
    val status: String // "PENDING", "PAID", "OVERDUE"
)

/**
 * Modelo de datos para un usuario.
 */
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val identificationCard: String, // Cédula/RNC
    val role: String, // "CLIENT", "OPERATOR", "ADMIN"
    val passwordHash: String
)

/**
 * Modelo de datos para configuración del sistema.
 */
data class SystemConfig(
    val key: String,
    val value: String
)