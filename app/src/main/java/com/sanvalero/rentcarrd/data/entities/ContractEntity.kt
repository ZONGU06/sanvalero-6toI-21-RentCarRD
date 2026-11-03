package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contracts")
data class ContractEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "contract_number")
    val contractNumber: String = "",

    @ColumnInfo(name = "contract_type")
    val contractType: String = "RENTAL", // RENTAL o MAINTENANCE

    @ColumnInfo(name = "client_id")
    val clientId: Int = 0,

    @ColumnInfo(name = "vehicle_id")
    val vehicleId: Int?, // nullable

    @ColumnInfo(name = "start_date")
    val startDate: String = "", // dd/MM/yyyy

    @ColumnInfo(name = "expected_end_date")
    val expectedEndDate: String = "", // dd/MM/yyyy

    @ColumnInfo(name = "actual_end_date")
    val actualEndDate: String?, // nullable

    @ColumnInfo(name = "base_amount")
    val baseAmount: Double = 0.0,

    @ColumnInfo(name = "extra_charges")
    val extraCharges: Double = 0.0,

    @ColumnInfo(name = "discounts")
    val discounts: Double = 0.0,

    @ColumnInfo(name = "total_amount")
    val totalAmount: Double = 0.0,

    @ColumnInfo(name = "status")
    val status: String = "ACTIVE",

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false
)