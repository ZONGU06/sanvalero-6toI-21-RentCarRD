package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.SystemConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SystemConfigDao {

    @query("SELECT * FROM system_configs WHERE key = :key")
    suspend fun getConfig(key: String): SystemConfigEntity?

    @query("SELECT * FROM system_configs")
    suspend fun getAllConfigs(): List<SystemConfigEntity>

    @query("UPDATE system_configs SET value = :value WHERE key = :key")
    suspend fun updateConfig(key: String, value: String)

    @query("INSERT OR REPLACE INTO system_configs (key, value) VALUES (:key, :value)")
    suspend fun insertOrReplaceConfig(key: String, value: String)
}