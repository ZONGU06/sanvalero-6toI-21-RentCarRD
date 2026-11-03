package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE identification_card = :identificationCard")
    suspend fun getUserByIdentificationCard(identificationCard: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getUsersByRole(role: String): List<UserEntity>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)

    @Query("UPDATE users SET password_hash = :hash, full_name = :name, email = :email, phone = :phone, identification_card = :identification WHERE id = :id")
    suspend fun updateUser(id: Int, name: String, email: String, phone: String, identification: String, hash: String)

    @Query("UPDATE users SET is_deleted = :isDeleted WHERE id = :id")
    suspend fun softDeleteUser(id: Int, isDeleted: Boolean)
}