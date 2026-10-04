package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val localId: String = UUID.randomUUID().toString(),
    val serverId: Long? = null,
    val userId: Long,
    val ownerName: String? = null,
    val plateNumber: String,
    val brand: String,
    val model: String,
    val year: Int = 2024,
    val color: String = "-",
    val photoPath: String? = null,
    val isSynced: Boolean = false
)
