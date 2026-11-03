package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.VehicleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Query("SELECT * FROM vehicles WHERE status = 'AVAILABLE'")
    suspend fun getAvailableVehicles(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE status = 'RENTED'")
    suspend fun getRentedVehicles(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE status = 'MAINTENANCE'")
    suspend fun getMaintenanceVehicles(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE brand LIKE :brand OR model LIKE :model")
    suspend fun searchVehicles(brand: String, model: String): List<VehicleEntity>

    @Query("SELECT * FROM vehicles")
    suspend fun getAllVehicles(): List<VehicleEntity>

    @Query("UPDATE vehicles SET status = :status, is_deleted = :isDeleted WHERE id = :id")
    suspend fun updateVehicleStatus(id: Int, status: String, isDeleted: Boolean)

    @Query("SELECT * FROM vehicles ORDER BY brand, model")
    suspend fun getVehiclesOrdered(): List<VehicleEntity>

    @Query("SELECT COUNT(*) FROM vehicles WHERE status = :status")
    suspend fun countVehiclesByStatus(status: String): Int

    @Query("SELECT * FROM vehicles WHERE id IN (:vehicleIds)")
    suspend fun getVehiclesByIds(vehicleIds: List<Int>): List<VehicleEntity>
}