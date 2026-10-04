package com.bengkel.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bengkel.app.data.local.entity.TransactionDetailEntity
import com.bengkel.app.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionDetails(details: List<TransactionDetailEntity>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE customerId = :customerId OR userId = :customerId ORDER BY createdAt DESC")
    fun getTransactionsByCustomerIdFlow(customerId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE localId = :localId LIMIT 1")
    suspend fun getTransactionByLocalId(localId: String): TransactionEntity?

    @Query("SELECT * FROM transaction_details WHERE transactionLocalId = :transactionLocalId")
    suspend fun getDetailsForTransaction(transactionLocalId: String): List<TransactionDetailEntity>

    @Query("SELECT * FROM transaction_details WHERE transactionLocalId = :transactionLocalId")
    fun getDetailsForTransactionFlow(transactionLocalId: String): Flow<List<TransactionDetailEntity>>

    @Query("SELECT COUNT(*) FROM transactions")
    fun getTransactionCountFlow(): Flow<Int>

    @Query("SELECT SUM(totalPayment) FROM transactions WHERE status = 'completed'")
    fun getTotalIncomeFlow(): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE isSynced = 0")
    suspend fun getUnsyncedTransactions(): List<TransactionEntity>
}
