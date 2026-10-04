package com.bengkel.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bengkel.app.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles WHERE localId = :localId LIMIT 1")
    suspend fun getVehicleByLocalId(localId: String): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE serverId = :serverId LIMIT 1")
    suspend fun getVehicleByServerId(serverId: Long): VehicleEntity?

    @Query("SELECT * FROM vehicles")
    fun getAllVehiclesFlow(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE userId = :userId")
    fun getVehiclesByUserIdFlow(userId: Long): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE userId = :userId")
    suspend fun getVehiclesByUserId(userId: Long): List<VehicleEntity>

    @Query("SELECT COUNT(*) FROM vehicles")
    fun getVehicleCountFlow(): Flow<Int>

    @Query("DELETE FROM vehicles WHERE localId = :localId")
    suspend fun deleteVehicleByLocalId(localId: String)

    @Query("SELECT * FROM vehicles WHERE isSynced = 0")
    suspend fun getUnsyncedVehicles(): List<VehicleEntity>
}
