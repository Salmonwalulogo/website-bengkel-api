package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val name: String,
    val description: String? = null,
    val harga: Double,
    val stok: Int,
    val status: String = "active",
    val photo: String? = null,
    val isSynced: Boolean = true
)
