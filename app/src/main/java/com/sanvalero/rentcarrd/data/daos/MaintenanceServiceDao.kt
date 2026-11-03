package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.MaintenanceServiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MaintenanceServiceDao {

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertMaintenanceService(service: MaintenanceServiceEntity)

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertMaintenanceServices(services: List<MaintenanceServiceEntity>)

    @query("SELECT * FROM maintenance_services WHERE contract_id = :contractId")
    suspend fun getMaintenanceServicesByContract(contractId: Int): List<MaintenanceServiceEntity>

    @query("SELECT * FROM maintenance_services WHERE maintenance_status = :status")
    suspend fun getMaintenanceServicesByStatus(status: String): List<MaintenanceServiceEntity>

    @query("SELECT * FROM maintenance_services")
    suspend fun getAllMaintenanceServices(): List<MaintenanceServiceEntity>

    @query("UPDATE maintenance_services SET maintenance_status = :status, actual_completion_date = :actualDate WHERE id = :id")
    suspend fun updateMaintenanceStatus(id: Int, status: String, actualDate: String)

    @query("SELECT COUNT(*) FROM maintenance_services WHERE contract_id = :contractId")
    suspend fun countServicesByContract(contractId: Int): Int

    @query("SELECT * FROM maintenance_services WHERE maintenance_status != 'COMPLETED'")
    suspend fun getActiveMaintenanceServices(): List<MaintenanceServiceEntity>
}