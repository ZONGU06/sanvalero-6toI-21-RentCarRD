package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_configs")
data class SystemConfigEntity(
    @PrimaryKey
    val key: String = "",

    @ColumnInfo(name = "value")
    val value: String = ""
)