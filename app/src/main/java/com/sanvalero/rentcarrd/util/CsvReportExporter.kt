package com.sanvalero.rentcarrd.util

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import android.annotation.SuppressLint

/**
 * Exportador de reportes CSV para RentCarRD.
 * Soporta exportación de alquileres, mantenimientos y datos generales
 * en formato CSV compatible con Excel y otras herramientas de análisis.
 */
object CsvReportExporter {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    /**
     * Exporta un listado general de usuarios a CSV.
     * @param context Contexto de Android
     * @param users Lista de usuarios exportar
     * @return Ruta del archivo CSV generado
     */
    @Suppress("RedundantSynchronization")
    fun exportUsersCsv(context: Context, users: List<Map<String, String>>): String? {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "RentCarRD_Usuarios_${dateFormat.format(Calendar.getInstance().time)}.csv"
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)

            // Cabecera
            writer.write("ID,Nombre,Email,Cédula,Rol,Teléfono\n")

            // Datos
            for (user in users) {
                writer.write("${user["id"]},${user["fullName"]},${user["email"]},${user["identificationCard"]},${user["role"]},${user["phone"]}\n")
            }

            writer.flush()
            writer.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Exporta un listado general de vehículos a CSV.
     * @param context Contexto de Android
     * @param vehicles Lista de vehículos exportar
     * @return Ruta del archivo CSV generado
     */
    fun exportVehiclesCsv(context: Context, vehicles: List<VehicleEntity>): String? {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "RentCarRD_Vehiculos_${dateFormat.format(Calendar.getInstance().time)}.csv"
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)

            // Cabecera
            writer.write("ID,Brand,Model,Year,Color,Category,Transmission,Passengers,FuelType,DailyRate,Status\n")

            // Datos
            for (vehicle in vehicles) {
                writer.write("${
                    vehicle.id
                },${vehicle.brand},${vehicle.model},${vehicle.year},${vehicle.color},${vehicle.category},${vehicle.transmission},${vehicle.passengers},${vehicle.fuelType},${vehicle.dailyRate},${vehicle.status}\n")
            }

            writer.flush()
            writer.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Exporta un reporte por período de alquileres y mantenimientos a CSV.
     * @param context Contexto de Android
     * @param startDate Fecha inicio (dd/MM/yyyy)
     * @param endDate Fecha fin (dd/MM/yyyy)
     * @param contracts Lista de contratos del período
     * @param maintenanceServices Lista de servicios de mantenimiento del período
     * @return Ruta del archivo CSV generado
     */
    fun exportPeriodReportCsv(
        context: Context,
        startDate: String,
        endDate: String,
        contracts: List<Map<String, String>>,
        maintenanceServices: List<Map<String, String>>
    ): String? {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "RentCarRD_Periodo_${startDate}_${endDate}.csv"
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)

            // Cabecera - Sección Alquileres
            writer.write("=== ALQUILERES PERÍODO ===\n")
            writer.write("Número Contrato,Cliente,Vehículo,Fecha Inicio,Fecha Fin,Días Facturables,Monto Base,Descuentos,Total\n")

            // Datos de alquileres
            for (contract in contracts) {
                writer.write("${
                    contract["contractNumber"]
                },${contract["clientName"]},${contract["vehicleBrand"]} ${contract["vehicleModel"]},${contract["startDate"]},${contract["endDate"]},${contract["billableDays"]},${contract["baseAmount"]},${contract["discounts"]},${contract["totalAmount"]}\n")
            }

            // Línea separadora
            writer.write("\n=== MANTENIMIENTOS PERÍODO ===\n")
            writer.write("ID Servicio,Vehículo,Tipo,Costos Labor,Costos Repuestos,Total,Fecha Prometida,Fecha Real\n")

            // Datos de mantenimiento
            for (service in maintenanceServices) {
                writer.write"${
                    service["serviceId"]
                },${service["vehicleBrand"]} ${service["vehicleModel"]},${service["serviceType"]},${service["laborCost"]},${service["partsCost"]},${service["totalAmount"]},${service["promisedCompletionDate"]},${service["actualCompletionDate"]}\n"
            }

            writer.flush()
            writer.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Exporta un reporte de pagos a CSV.
     * @param context Contexto de Android
     * @param payments Lista de pagos exportar
     * @return Ruta del archivo CSV generado
     */
    fun exportPaymentsCsv(context: Context, payments: List<Map<String, String>>): String? {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "RentCarRD_Pagos_${dateFormat.format(Calendar.getInstance().time)}.csv"
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)

            // Cabecera
            writer.write("ID,Contrato,Monto,Fecha Pago,Metodo,Pago\n")

            // Datos
            for (payment in payments) {
                writer.write"${
                    payment["id"]
                },${payment["contractId"]},${payment["amount"]},${payment["paymentDate"]},${payment["paymentMethod"]},${payment["status"]}\n"
            }

            writer.flush()
            writer.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Genera archivo CSV con configuración del sistema (tarifas, porcentajes).
     * @param context Contexto de Android
     * @param configs Lista de configuraciones sistema
     * @return Ruta del archivo CSV generado
     */
    fun exportSystemConfigCsv(context: Context, configs: List<Map<String, String>>): String? {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "RentCarRD_Configuracion_${dateFormat.format(Calendar.getInstance().time)}.csv"
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)

            // Cabecera
            writer.write("Key,Value,Descripción\n")

            // Datos
            for (config in configs) {
                writer.write"${
                    config["key"]
                },${config["value"]},${config["description"]}\n"
            }

            writer.flush()
            writer.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /** Convierte un objeto VehicleEntity a mapa para exportación CSV */
    @Suppress("DEPRECATION")
    private fun vehicleToMap(vehicle: VehicleEntity): Map<String, String> {
        return mapOf(
            "id" to vehicle.id.toString(),
            "brand" to vehicle.brand,
            "model" to vehicle.model,
            "year" to vehicle.year.toString(),
            "color" to vehicle.color,
            "category" to vehicle.category,
            "transmission" to vehicle.transmission,
            "passengers" to vehicle.passengers.toString(),
            "fuelType" to vehicle.fuelType,
            "dailyRate" to vehicle.dailyRate.toString(),
            "status" to vehicle.status
        )
    }

    /** Convierte un objeto ContractEntity a mapa para exportación CSV */
    @Suppress("DEPRECATION")
    private fun contractToMap(contract: ContractEntity): Map<String, String> {
        return mapOf(
            "id" to contract.id.toString(),
            "contractNumber" to contract.contractNumber,
            "contractType" to contract.contractType,
            "clientId" to contract.clientId.toString(),
            "vehicleId" to (contract.vehicleId ?: "0").toString(),
            "startDate" to contract.startDate,
            "expectedEndDate" to contract.expectedEndDate,
            "actualEndDate" to (contract.actualEndDate ?: "").toString(),
            "baseAmount" to contract.baseAmount.toString(),
            "extraCharges" to contract.extraCharges.toString(),
            "discounts" to contract.discounts.toString(),
            "totalAmount" to contract.totalAmount.toString(),
            "status" to contract.status,
            "isDeleted" to contract.isDeleted.toString()
        )
    }

    /** Convierte un objeto MaintenanceServiceEntity a mapa para exportación CSV */
    @Suppress("DEPRECATION")
    private fun maintenanceToMap(service: MaintenanceServiceEntity): Map<String, String> {
        return mapOf(
            "id" to service.id.toString(),
            "contractId" to service.contractId.toString(),
            "vehicleBrand" to service.vehicleBrand,
            "vehicleModel" to service.vehicleModel,
            "vehicleYear" to service.vehicleYear.toString(),
            "vehicleColor" to service.vehicleColor,
            "licensePlate" to service.licensePlate,
            "mileage" to service.mileage.toString(),
            "issueDescription" to service.issueDescription,
            "serviceType" to service.serviceType,
            "laborCost" to service.laborCost.toString(),
            "partsCost" to service.partsCost.toString(),
            "promisedCompletionDate" to service.promisedCompletionDate,
            "actualCompletionDate" to (service.actualCompletionDate ?: "").toString(),
            "maintenanceStatus" to service.maintenanceStatus
        )
    }

    /** Convierte un objeto PaymentEntity a mapa para exportación CSV */
    @Suppress("DEPRECATION")
    private fun paymentToMap(payment: PaymentEntity): Map<String, String> {
        return mapOf(
            "id" to payment.id.toString(),
            "contractId" to payment.contractId.toString(),
            "amount" to payment.amount.toString(),
            "paymentDate" to payment.paymentDate,
            "paymentMethod" to payment.paymentMethod,
            "status" to payment.status
        )
    }
}