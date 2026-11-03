package com.sanvalero.rentcarrd.data.daos

import com.sanvalero.rentcarrd.data.entities.PaymentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertPayment(payment: PaymentEntity)

    @Insert(onConflict = OnConflictStrategy.INSERT)
    suspend fun insertPayments(payments: List<PaymentEntity>)

    @query("SELECT * FROM payments WHERE contract_id = :contractId")
    suspend fun getPaymentsByContract(contractId: Int): List<PaymentEntity>

    @query("SELECT * FROM payments")
    suspend fun getAllPayments(): List<PaymentEntity>

    @query("UPDATE payments SET status = :status WHERE id = :id")
    suspend fun updatePaymentStatus(id: Int, status: String)

    @query("SELECT COUNT(*) FROM payments WHERE contract_id = :contractId")
    suspend fun countPaymentsByContract(contractId: Int): Int
}