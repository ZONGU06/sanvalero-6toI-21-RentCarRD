package com.sanvalero.rentcarrd.data.repositories

import com.sanvalero.rentcarrd.domain.Model.User
import com.sanvalero.rentcarrd.domain.repositories.UserRepository
import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import com.sanvalero.rentcarrd.data.entities.UserEntity
import com.sanvalero.rentcarrd.data.daos.UserDao
import javax.inject.Inject
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val database: RentCarRDDatabase
) : UserRepository {

    // Estado observable del usuario actual
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser

    override fun login(email: String, password: String) {
        isActive.launch {
            val userEntity = userDao.getUserByEmail(email) ?: also {
                _currentUser.value = null
                return
            }

            // Verificar contraseña (comparar hash)
            if (userEntity.passwordHash == hashPassword(password)) {
                val user = User(
                    id = userEntity.id,
                    fullName = userEntity.fullName,
                    email = userEntity.email,
                    phone = userEntity.phone,
                    identificationCard = userEntity.identificationCard,
                    role = userEntity.role,
                    passwordHash = userEntity.passwordHash
                )
                _currentUser.value = user
            } else {
                _currentUser.value = null
            }
        }
    }

    override fun register(fullName: String, email: String, phone: String, identificationCard: String, password: String, role: String) {
        isActive.launch {
            // Hashear contraseña antes de guardar
            val passwordHash = hashPassword(password)

            val userEntity = UserEntity(
                id = 0,
                fullName = fullName,
                email = email,
                passwordHash = passwordHash,
                phone = phone,
                identificationCard = identificationCard,
                role = role
            )

            userDao.insertUser(userEntity)
            _currentUser.value = User(
                id = userEntity.id,
                fullName = userEntity.fullName,
                email = userEntity.email,
                phone = userEntity.phone,
                identificationCard = userEntity.identificationCard,
                role = userEntity.role,
                passwordHash = userEntity.passwordHash
            )
        }
    }

    override fun logout() {
        _currentUser.value = null
    }

    override fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.let { entity ->
            User(
                id = entity.id,
                fullName = entity.fullName,
                email = entity.email,
                phone = entity.phone,
                identificationCard = entity.identificationCard,
                role = entity.role,
                passwordHash = entity.passwordHash
            )
        }
    }

    override fun getUserByIdentificationCard(identificationCard: String): User? {
        return userDao.getUserByIdentificationCard(identificationCard)?.let { entity ->
            User(
                id = entity.id,
                fullName = entity.fullName,
                email = entity.email,
                phone = entity.phone,
                identificationCard = entity.identificationCard,
                role = entity.role,
                passwordHash = entity.passwordHash
            )
        }
    }

    override fun isAdmin(user: User): Boolean {
        return user.role == "ADMIN"
    }

    override fun isOperator(user: User): Boolean {
        return user.role == "OPERATOR"
    }

    override fun isClient(user: User): Boolean {
        return user.role == "CLIENT"
    }

    /** Convierte una cadena en hash SHA-256 */
    private fun hashPassword(password: String): String {
        // TODO: Implementar hash real con BCrypt o SHA-256 adecuado
        // Por ahora retornamos el password tal como está (para demostración)
        // En producción: usar BCryptPasswordEncoder o MessageDigest SHA-256
        // Hash simulado para demostración
        return "hash_$password" // Placeholder - en producción debe ser un hash real
    }
}