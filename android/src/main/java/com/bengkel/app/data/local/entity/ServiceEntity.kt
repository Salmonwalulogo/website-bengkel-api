package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String? = null,
    val harga: Double,
    val duration: String? = null,
    val status: String = "active", // "active", "inactive"
    val isSynced: Boolean = true
)
