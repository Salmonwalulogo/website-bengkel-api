package com.bengkel.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bengkel.app.data.local.dao.BookingDao
import com.bengkel.app.data.local.dao.ProductDao
import com.bengkel.app.data.local.dao.ServiceDao
import com.bengkel.app.data.local.dao.SyncQueueDao
import com.bengkel.app.data.local.dao.TransactionDao
import com.bengkel.app.data.local.dao.UserDao
import com.bengkel.app.data.local.dao.VehicleDao
import com.bengkel.app.data.local.entity.BookingEntity
import com.bengkel.app.data.local.entity.ProductEntity
import com.bengkel.app.data.local.entity.ServiceEntity
import com.bengkel.app.data.local.entity.SyncQueueEntity
import com.bengkel.app.data.local.entity.TransactionDetailEntity
import com.bengkel.app.data.local.entity.TransactionEntity
import com.bengkel.app.data.local.entity.UserEntity
import com.bengkel.app.data.local.entity.VehicleEntity

@Database(
    entities = [
        UserEntity::class,
        VehicleEntity::class,
        ServiceEntity::class,
        ProductEntity::class,
        BookingEntity::class,
        TransactionEntity::class,
        TransactionDetailEntity::class,
        SyncQueueEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun serviceDao(): ServiceDao
    abstract fun productDao(): ProductDao
    abstract fun bookingDao(): BookingDao
    abstract fun transactionDao(): TransactionDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bengkel_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
