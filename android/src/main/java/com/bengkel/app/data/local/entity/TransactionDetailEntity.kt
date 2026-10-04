package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transaction_details")
data class TransactionDetailEntity(
    @PrimaryKey val localId: String = UUID.randomUUID().toString(),
    val transactionLocalId: String,
    val productId: Long? = null,
    val itemName: String,
    val quantity: Int,
    val price: Double,
    val subtotal: Double
)
