package com.bengkel.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bengkel.app.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Query("SELECT * FROM services")
    fun getAllServicesFlow(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE status = 'active'")
    fun getActiveServicesFlow(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun getServiceById(id: Long): ServiceEntity?

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteServiceById(id: Long)
}
