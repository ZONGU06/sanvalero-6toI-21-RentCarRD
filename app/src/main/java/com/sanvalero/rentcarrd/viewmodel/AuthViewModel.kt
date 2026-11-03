package com.sanvalero.rentcarrd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanvalero.rentcarrd.di.HiltViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de autenticación y sesión de usuario.
 * Maneja el estado de login/registro y transiciones de navegación.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado observable de la autenticación
    private val _authStatus = mutableStateOf<AuthStatus>(AuthStatus.Uninitialized)
    val authStatus = _authStatus.asStateFlow()

    // Estado observable del usuario actual
    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Flujo de errores
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        // Verificar si ya existe una sesión activa al iniciar
        viewModelScope.launch {
            checkExistingSession()
        }
    }

    /**
     * Verifica si hay una sesión existente en la base de datos.
     */
    private fun checkExistingSession() {
        // TODO: Implementar validación de sesión existente
        // Por ahora simulamos un retraso y establecemos estado inicial
        Delay(500)
        _authStatus.value = AuthStatus.Ready
    }

    /**
     * Inicia sesión con las credenciales proporcionadas.
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authStatus.value = AuthStatus.Loading
            _errorMessage.value = null

            // TODO: Validar credenciales contra el repositorio/DB
            // Simulación exitosa por ahora
            if (validateCredentials(email, password)) {
                val user = User(
                    id = 1,
                    fullName = "Usuario Demo",
                    email = email,
                    role = Role.CLIENT
                )
                _currentUser.value = user
                _authStatus.value = AuthStatus.Authenticated
                _errorMessage.value = null
            } else {
                _authStatus.value = AuthStatus.Failed
                _errorMessage.value = "Correo o contraseña incorrectos"
            }
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param fullName Nombre completo del usuario
     * @param email Correo electrónico
     * @param phone Teléfono
     * @param identificationCard Cédula/RNC
     * @param password Contraseña (se tendrá que hashear antes de guardar)
     * @param role Rol del usuario (CLIENT, OPERATOR, ADMIN)
     */
    fun register(
        fullName: String,
        email: String,
        phone: String,
        identificationCard: String,
        password: String,
        role: String
    ) {
        viewModelScope.launch {
            _authStatus.value = AuthStatus.Loading
            _errorMessage.value = null

            // TODO: Hashear la contraseña antes de guardar (SHA-256 o BCrypt)
            // TODO: Validar que el email no esté ya registrado
            // TODO: Guardar usuario en la base de datos

            // Simulación exitosa
            val user = User(
                id = 2,
                fullName = fullName,
                email = email,
                role = when (role) {
                    "OPERATOR" -> Role.OPERATOR
                    "ADMIN" -> Role.ADMIN
                    else -> Role.CLIENT
                }
            )
            _currentUser.value = user
            _authStatus.value = AuthStatus.Authenticated
            _errorMessage.value = null
        }
    }

    /**
     * Cierra la sesión actual.
     */
    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _authStatus.value = AuthStatus.Uninitialized
            // TODO: Limpiar preferencias de sesión
        }
    }

    /**
     * Valida las credenciales simples (placeholder).
     */
    private fun validateCredentials(email: String, password: String): Boolean {
        // TODO: Implementar validación real
        // Por ahora accept any non-empty credentials
        return email.isNotEmpty() && password.isNotEmpty()
    }

    /**
     * Enum que representa los estados de autenticación.
     */
    enum class AuthStatus {
        Uninitialized,
        Ready,
        Loading,
        Authenticated,
        Failed
    }

    /**
     * Datos del usuario actual.
     */
    data class User(
        val id: Int,
        val fullName: String,
        val email: String,
        val role: Role
    )

    /**
     * Enum que representa los roles de usuario.
     */
    enum class Role {
        CLIENT,
        OPERATOR,
        ADMIN
    }

    /**
     * Repositorio de autenticación (inyección).
     */
    object AuthRepository
}