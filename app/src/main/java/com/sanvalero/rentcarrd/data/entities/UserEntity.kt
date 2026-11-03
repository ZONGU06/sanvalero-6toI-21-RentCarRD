package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "full_name")
    val fullName: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "password_hash")
    val passwordHash: String = "",

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "identification_card")
    val identificationCard: String = "",

    @ColumnInfo(name = "role")
    val role: String = "CLIENT"
)