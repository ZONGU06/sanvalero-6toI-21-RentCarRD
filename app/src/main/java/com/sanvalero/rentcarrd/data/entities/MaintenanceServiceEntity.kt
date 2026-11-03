package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_services")
data class MaintenanceServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "contract_id")
    val contractId: Int = 0,

    @ColumnInfo(name = "vehicle_brand")
    val vehicleBrand: String = "",

    @ColumnInfo(name = "vehicle_model")
    val vehicleModel: String = "",

    @ColumnInfo(name = "vehicle_year")
    val vehicleYear: Int = 0,

    @ColumnInfo(name = "vehicle_color")
    val vehicleColor: String = "",

    @ColumnInfo(name = "license_plate")
    val licensePlate: String = "",

    @ColumnInfo(name = "mileage")
    val mileage: Double = 0.0,

    @ColumnInfo(name = "issue_description")
    val issueDescription: String = "",

    @ColumnInfo(name = "service_type")
    val serviceType: String = "",

    @ColumnInfo(name = "labor_cost")
    val laborCost: Double = 0.0,

    @ColumnInfo(name = "parts_cost")
    val partsCost: Double = 0.0,

    @ColumnInfo(name = "promised_completion_date")
    val promisedCompletionDate: String = "", // dd/MM/yyyy

    @ColumnInfo(name = "actual_completion_date")
    val actualCompletionDate: String?, // nullable

    @ColumnInfo(name = "maintenance_status")
    val maintenanceStatus: String = "PENDING"
)