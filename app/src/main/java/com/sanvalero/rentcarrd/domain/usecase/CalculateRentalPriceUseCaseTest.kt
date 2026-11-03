package com.sanvalero.rentcarrd.domain.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Pruebas unitarias para CalculateRentalPriceUseCase.
 */
class CalculateRentalPriceUseCaseTest {

    private lateinit var useCase: CalculateRentalPriceUseCase

    @BeforeEach
    fun setUp() {
        useCase = CalculateRentalPriceUseCase()
    }

    @Test
    fun `calculate rental price with standard dates`() {
        // Arrange
        val startDate = "01/12/2024"
        val endDate = "05/12/2024"
        val dailyRate = 1000.0

        // Act
        val result = useCase.calculate(startDate, endDate, dailyRate)

        // Assert
        // Días facturables: diciembre tiene 31 días, del 1 al 5 son 4 días
        // Fórmula: max(1, ceil((Fecha Fin - Fecha Inicio) / 24 horas))
        // Del 1 al 5 son 4 días completos, así que días facturables = 4
        assertEquals(4, result.billableDays)
        // Precio base = 4 × 1000 = 4000
        assertEquals(4000.0, result.basePrice)
    }

    @Test
    fun `calculate rental price with single day`() {
        // Arrange
        val startDate = "10/12/2024"
        val endDate = "11/12/2024"
        val dailyRate = 500.0

        // Act
        val result = useCase.calculate(startDate, endDate, dailyRate)

        // Assert
        // Mismo día debería dar 1 día facturable (mínimo)
        assertEquals(1, result.billableDays)
        assertEquals(500.0, result.basePrice)
    }

    @Test
    fun `calculate rental price crosses month boundary`() {
        // Arrange
        val startDate = "30/11/2024"
        val endDate = "03/12/2024"
        val dailyRate = 1500.0

        // Act
        val result = useCase.calculate(startDate, endDate, dailyRate)

        // Assert
        // Del 30 de noviembre al 3 de diciembre son 3 días
        assertEquals(3, result.billableDays)
        assertEquals(4500.0, result.basePrice)
    }

    @Test
    fun `calculate rental price with very short interval`() {
        // Arrange
        val startDate = "01/12/2024"
        val endDate = "01/12/2024 14:00" // Mismo día, 14 horas después
        // Note: El formato es dd/MM/yyyy, así que esto sería el mismo día

        // Act
        val result = useCase.calculate(startDate, startDate, dailyRate = 1000.0)

        // Assert
        // Mínimo 1 día facturable
        assertEquals(1, result.billableDays)
    }
}