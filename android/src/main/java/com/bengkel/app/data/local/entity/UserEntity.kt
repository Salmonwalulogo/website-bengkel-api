package com.bengkel.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val email: String,
    val username: String,
    val noHp: String? = null,
    val role: String, // "admin", "kasir", "customer"
    val photo: String? = null,
    val isSynced: Boolean = true
)
