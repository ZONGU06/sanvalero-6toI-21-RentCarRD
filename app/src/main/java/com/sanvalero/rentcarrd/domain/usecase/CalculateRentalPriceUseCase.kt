package com.sanvalero.rentcarrd.domain.usecase

import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import com.sanvalero.rentcarrd.domain.model.Contract
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Caso de uso para calcular el precio de un alquiler.
 * Calcula los días facturables y el precio base según la fórmula especificada.
 */
class CalculateRentalPriceUseCase {

    /**
     * Calcula los días facturables y el precio base de un alquiler.
     *
     * @param startDate Fecha de inicio del alquiler (formato dd/MM/yyyy)
     * @param endDate Fecha de fin del alquiler (formato dd/MM/yyyy)
     * @param dailyRate Tarifa diaria del vehículo
     * @return Objeto con los días facturables y el precio base
     */
    data class Result(
        val billableDays: Int,
        val basePrice: Double
    )

    fun calculate(
        startDate: String,
        endDate: String,
        dailyRate: Double
    ): Result {
        // Parsear fechas en formato dd/MM/yyyy
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)

        // Fórmula: Días Facturables = max(1, ceil((Fecha Fin - Fecha Inicio) / 24 horas))
        val diffDays = java.time.temporal.ChronoUnit.DAYS.between(start, end)
        val billableDays = Math.max(1, (diffDays + 23) / 24) // Equivalente a ceil division

        val basePrice = billableDays * dailyRate

        return Result(
            billableDays = billableDays,
            basePrice = basePrice
        )
    }
}

/**
 * Caso de uso para verificar superposición de fechas (overbooking).
 * Verifica si un rango de fechas solicitado se solapa con contratos existentes.
 */
class CheckOverbookingUseCase {

    private val contractDao: ContractDao

    init {
        // Inyección del DAO - en producción se inyectaría través del repositorio
        this.contractDao = TODO("Inyectar ContractDao")
    }

    /**
     * Verifica si el rango de fechas [startDate, endDate] se solapa con algún contrato existente
     * para el vehículo especificado.
     *
     * @param vehicleId ID del vehículo
     * @param startDate Fecha de inicio (formato dd/MM/yyyy)
     * @param endDate Fecha de fin (formato dd/MM/yyyy)
     * @return true si hay superposición (overbooking), false si no
     */
    fun isOverbooking(
        vehicleId: Int,
        startDate: String,
        endDate: String
    ): Boolean {
        // TODO: Implementar consulta real a la base de datos
        // Por ahora retornamos false (sin superposición detectada)
        // La lógica debería ser:
        // EXISTS contrato c WHERE c.vehicle_id = vehicleId
        // AND c.status IN ('ACTIVE', 'VERIFIED')
        // AND c.start_date <= endDate
        // AND c.expected_end_date >= startDate
        return false
    }
}

/**
 * Caso de uso para calcular moras (multas por retraso) en alquileres.
 * Aplica recargos basados en reglas parametrizables desde la administración.
 */
class CalculateLateFeesUseCase {

    private val systemConfigDao: SystemConfigDao

    init {
        this.systemConfigDao = TODO("Inyectar SystemConfigDao")
    }

    /**
     * Calcula el monto de la mora por retraso en la devolución de un alquiler.
     *
     * @param contractId ID del contrato
     * @param actualReturnDate Fecha real de devolución (formato dd/MM/yyyy)
     * @param expectedEndDate Fecha esperada de devolución (formato dd/MM/yyyy)
     * @return Monto de la mora calculada
     */
    data class Result(
        val lateFee: Double,
        val isLate: Boolean
    )

    fun calculate(
        contractId: Int,
        actualReturnDate: String,
        expectedEndDate: String
    ): Result {
        // Obtener la tarifa de mora configurada
        val config = systemConfigDao.getConfig("moras_rate")
        val moraRate = config?.value.toDoubleOrNull() ?: 0.1 // Default 10% si no configurado

        // Parsear fechas
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val actual = LocalDate.parse(actualReturnDate, formatter)
        val expected = LocalDate.parse(expectedEndDate, formatter)

        val isLate = actual.isAfter(expected) || actual.isEqual(expected)

        var lateFee = 0.0
        if (isLate) {
            // Calcular días de retraso
            val delayDays = java.time.temporal.ChronoUnit.DAYS.between(expected, actual)
            // Mora = tarifa_base * tasa_mora * días_retraso (o fórmula según configuración)
            // Fórmula simplificada: base_amount * mora_rate * delay_days
            // En un caso real, consultaríamos el base_amount del contrato
            lateFee = 100.0 * moraRate * delayDays // Ejemplo con monto base 100
        }

        return Result(
            lateFee = lateFee,
            isLate = isLate
        )
    }
}

/**
 * Caso de uso para calcular descuento por retraso en mantenimiento.
 * Si la fecha real de finalización supera la fecha prometida, se aplica
 * un descuento sobre el presupuesto aprobado como penalización.
 */
class CalculateMaintenancePenaltyUseCase {

    private val systemConfigDao: SystemConfigDao

    init {
        this.systemConfigDao = TODO("Inyectar SystemConfigDao")
    }

    /**
     * Calcula el descuento por retraso en servicio de mantenimiento.
     *
     * @param promisedDate Fecha prometida de finalización (dd/MM/yyyy)
     * @param actualDate Fecha real de finalización (dd/MM/yyyy)
     * @param budgetAmount Presupuesto aprobado
     * @return Descuento calculado (0 si no hay retraso)
     */
    data class Result(
        val discount: Double,
        val isDelayed: Boolean
    )

    fun calculate(
        promisedDate: String,
        actualDate: String,
        budgetAmount: Double
    ): Result {
        // Obtener porcentaje de penalización configurado
        val config = systemConfigDao.getConfig("maintenance_penalty_percent")
        val penaltyPercent = config?.value.toDoubleOrNull() ?: 5.0 // Default 5% si no configurado

        // Parsear fechas
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val promised = LocalDate.parse(promisedDate, formatter)
        val actual = LocalDate.parse(actualDate, formatter)

        val isDelayed = actual.isAfter(promised) || actual.isEqual(promised)

        var discount = 0.0
        if (isDelayed) {
            // Calcular descuento sobre el presupuesto
            discount = budgetAmount * (penaltyPercent / 100.0)
        }

        return Result(
            discount = discount,
            isDelayed = isDelayed
        )
    }
}