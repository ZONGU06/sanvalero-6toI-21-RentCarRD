package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "contract_id")
    val contractId: Int = 0,

    @ColumnInfo(name = "amount")
    val amount: Double = 0.0,

    @ColumnInfo(name = "payment_date")
    val paymentDate: String = "", // dd/MM/yyyy

    @ColumnInfo(name = "payment_method")
    val paymentMethod: String = "", // CASH, CARD, TRANSFER

    @ColumnInfo(name = "status")
    val status: String = "PENDING"
)