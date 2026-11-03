package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.ContractEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContractDao {

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertContract(contract: ContractEntity)

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertContracts(contracts: List<ContractEntity>)

    @Query("SELECT * FROM contracts WHERE client_id = :clientId")
    suspend fun getContractsByClient(clientId: Int): List<ContractEntity>

    @query("SELECT * FROM contracts WHERE vehicle_id = :vehicleId")
    suspend fun getContractsByVehicle(vehicleId: Int): List<ContractEntity>

    @query("SELECT * FROM contracts WHERE contract_type = :type")
    suspend fun getContractsByType(type: String): List<ContractEntity>

    @query("SELECT * FROM contracts WHERE status = :status")
    suspend fun getContractsByStatus(status: String): List<ContractEntity>

    @query("SELECT * FROM contracts")
    suspend fun getAllContracts(): List<ContractEntity>

    @query("SELECT * FROM contracts WHERE is_deleted = :isDeleted")
    suspend fun getDeletedContracts(isDeleted: Boolean): List<ContractEntity>

    @query("UPDATE contracts SET status = :status, actual_end_date = :actualEndDate, is_deleted = :isDeleted WHERE id = :id")
    suspend fun updateContract(id: Int, status: String, actualEndDate: String, isDeleted: Boolean)

    @query("SELECT COUNT(*) FROM contracts WHERE client_id = :clientId")
    suspend fun countContractsByClient(clientId: Int): Int

    @query("SELECT * FROM contracts WHERE status = 'ACTIVE' AND is_deleted = FALSE")
    suspend fun getActiveContracts(): List<ContractEntity>
}