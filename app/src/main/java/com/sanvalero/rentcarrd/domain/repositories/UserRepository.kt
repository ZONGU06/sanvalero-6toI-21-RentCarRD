package com.sanvalero.rentcarrd.domain.repositories

import com.sanvalero.rentcarrd.domain.Model.User
import com.sanvalero.rentcarrd.domain.usecase.CalculateRentalPriceUseCase.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Repositorio para operaciones de usuario.
 * Define el contrato para el acceso a datos a nivel de dominio.
 */
interface UserRepository {

    /** Flujo observable del usuario actual */
    val currentUser: StateFlow<User?>

    /** Inicia sesión con email y contraseña */
    fun login(email: String, password: String)

    /** Registra un nuevo usuario */
    fun register(fullName: String, email: String, phone: String, identificationCard: String, password: String, role: String)

    /** Cierra la sesión */
    fun logout()

    /** Obtiene usuario por email */
    fun getUserByEmail(email: String): User?

    /** Obtiene usuario por cédula */
    fun getUserByIdentificationCard(identificationCard: String): User?

    /** Verifica si el usuario tiene rol de administrador */
    fun isAdmin(user: User): Boolean

    /** Verifica si el usuario tiene rol de operador */
    fun isOperator(user: User): Boolean

    /** Verifica si el usuario tiene rol de cliente */
    fun isClient(user: User): Boolean
}