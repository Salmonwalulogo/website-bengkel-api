package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val localId: String = UUID.randomUUID().toString(),
    val serverId: Long? = null,
    val userId: Long,
    val customerId: Long? = null,
    val customerName: String? = null,
    val subtotal: Double,
    val discount: Double = 0.0,
    val tax: Double = 0.0,
    val totalPayment: Double,
    val paidAmount: Double,
    val changeAmount: Double,
    val status: String = "completed",
    val paymentMethod: String = "cash", // "cash", "transfer", "qris"
    val createdAt: String,
    val isSynced: Boolean = false
)
