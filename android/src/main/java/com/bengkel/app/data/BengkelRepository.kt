package com.bengkel.app.data

import android.content.Context
import com.bengkel.app.data.local.AppDatabase
import com.bengkel.app.data.local.SessionManager
import com.bengkel.app.data.local.entity.BookingEntity
import com.bengkel.app.data.local.entity.ProductEntity
import com.bengkel.app.data.local.entity.ServiceEntity
import com.bengkel.app.data.local.entity.SyncQueueEntity
import com.bengkel.app.data.local.entity.TransactionDetailEntity
import com.bengkel.app.data.local.entity.TransactionEntity
import com.bengkel.app.data.local.entity.UserEntity
import com.bengkel.app.data.local.entity.VehicleEntity
import com.bengkel.app.data.remote.api.ApiService
import com.bengkel.app.data.remote.api.RetrofitClient
import com.bengkel.app.data.remote.model.LoginRequest
import com.bengkel.app.data.remote.model.RegisterRequest
import com.bengkel.app.data.remote.model.BookingRequest
import com.bengkel.app.data.remote.model.TransactionItemDto
import com.bengkel.app.data.remote.model.TransactionRequest
import com.bengkel.app.data.remote.model.VehicleRequest
import com.google.gson.Gson
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

data class CartLine(val product: ProductEntity, val quantity: Int)

data class CheckoutResult(val transactionId: String, val synced: Boolean)

private class ApiException(message: String) : Exception(message)

class BengkelRepository(context: Context) {
    val sessionManager = SessionManager(context.applicationContext)

    private val database = AppDatabase.getDatabase(context.applicationContext)
    private val retrofitClient = RetrofitClient(sessionManager)
    private val gson = Gson()
    private val api: ApiService
        get() = retrofitClient.getApiService()

    fun products(): Flow<List<ProductEntity>> = database.productDao().getActiveProductsFlow()
    fun services(): Flow<List<ServiceEntity>> = database.serviceDao().getActiveServicesFlow()
    fun vehicles(): Flow<List<VehicleEntity>> = database.vehicleDao().getAllVehiclesFlow()
    fun bookings(): Flow<List<BookingEntity>> = database.bookingDao().getAllBookingsFlow()
    fun transactions(): Flow<List<TransactionEntity>> = database.transactionDao().getAllTransactionsFlow()
    fun pendingCount(): Flow<Int> = database.syncQueueDao().getPendingCountFlow()

    suspend fun login(identifier: String, password: String): UserEntity {
        val response = api.login(
            LoginRequest(
                email = identifier.takeIf { it.contains("@") },
                username = identifier.takeUnless { it.contains("@") },
                password = password
            )
        )
        val body = response.requireBody("Login gagal")
        val token = body.token ?: throw IOException("Server tidak mengirim token login.")
        val user = body.user ?: throw IOException("Server tidak mengirim data akun.")
        val localUser = user.toEntity()
        sessionManager.saveAuthToken(token)
        sessionManager.saveUser(localUser)
        return localUser
    }

    suspend fun register(
        name: String,
        email: String,
        username: String,
        phone: String,
        password: String
    ): UserEntity {
        val response = api.register(
            RegisterRequest(
                name = name.trim(),
                email = email.trim(),
                username = username.trim().ifBlank { null },
                noHp = phone.trim().ifBlank { null },
                password = password
            )
        )
        val body = response.requireBody("Pendaftaran gagal")
        val token = body.token ?: throw IOException("Server tidak mengirim token login.")
        val user = body.user ?: throw IOException("Server tidak mengirim data akun.")
        val localUser = user.toEntity()
        sessionManager.saveAuthToken(token)
        sessionManager.saveUser(localUser)
        return localUser
    }

    suspend fun refreshData(): List<String> {
        val errors = mutableListOf<String>()

        try {
            val response = api.getProducts()
            val body = response.requireBody("Gagal memuat produk")
            database.productDao().insertProducts(
                body.data.map { product ->
                    ProductEntity(
                        id = product.id,
                        categoryId = product.categoryId,
                        name = product.name,
                        description = product.description,
                        harga = product.harga,
                        stok = product.stok,
                        status = product.status ?: "active",
                        photo = product.photo
                    )
                }
            )
        } catch (error: IOException) {
            errors += "Produk: ${error.message ?: "tidak dapat diperbarui"}"
        } catch (error: ApiException) {
            errors += "Produk: ${error.message}"
        }

        try {
            val response = api.getServices()
            val body = response.requireBody("Gagal memuat layanan")
            database.serviceDao().insertServices(
                body.data.map { service ->
                    ServiceEntity(
                        id = service.id,
                        name = service.name,
                        description = service.description,
                        harga = service.harga,
                        duration = service.duration,
                        status = service.status ?: "active"
                    )
                }
            )
        } catch (error: IOException) {
            errors += "Layanan: ${error.message ?: "tidak dapat diperbarui"}"
        } catch (error: ApiException) {
            errors += "Layanan: ${error.message}"
        }

        val user = sessionManager.getCurrentUser()
        try {
            val response = api.getVehicles(user?.id.takeIf { user?.role == "customer" })
            val body = response.requireBody("Gagal memuat kendaraan")
            database.vehicleDao().insertVehicles(
                body.data.map { vehicle ->
                    VehicleEntity(
                        localId = vehicle.id.toString(),
                        serverId = vehicle.id,
                        userId = vehicle.userId,
                        ownerName = vehicle.user?.name,
                        plateNumber = vehicle.plateNumber,
                        brand = vehicle.brand,
                        model = vehicle.model,
                        year = vehicle.year ?: 2024,
                        color = vehicle.color ?: "-",
                        isSynced = true
                    )
                }
            )
        } catch (error: IOException) {
            errors += "Kendaraan: ${error.message ?: "tidak dapat diperbarui"}"
        } catch (error: ApiException) {
            errors += "Kendaraan: ${error.message}"
        }

        try {
            val response = api.getBookings()
            val body = response.requireBody("Gagal memuat pemesanan")
            database.bookingDao().insertBookings(
                body.data.map { booking ->
                    BookingEntity(
                        localId = "server-${booking.id}",
                        serverId = booking.id,
                        userId = booking.userId,
                        userName = booking.user?.name,
                        vehicleId = booking.vehicleId,
                        vehiclePlate = booking.vehicle?.plateNumber,
                        serviceId = booking.serviceId,
                        serviceName = booking.service?.name,
                        bookingDate = booking.bookingDate,
                        bookingTime = booking.bookingTime ?: "-",
                        complaint = booking.complaint ?: "-",
                        status = booking.status,
                        notes = booking.notes,
                        isSynced = true
                    )
                }
            )
        } catch (error: IOException) {
            errors += "Pemesanan: ${error.message ?: "tidak dapat diperbarui"}"
        } catch (error: ApiException) {
            errors += "Pemesanan: ${error.message}"
        }

        try {
            val response = api.getTransactions()
            val body = response.requireBody("Gagal memuat transaksi")
            database.transactionDao().insertTransactions(
                body.data.map { transaction ->
                    TransactionEntity(
                        localId = "server-${transaction.id}",
                        serverId = transaction.id,
                        userId = transaction.userId,
                        customerId = transaction.customerId,
                        customerName = transaction.customer?.name,
                        subtotal = transaction.subtotal,
                        discount = transaction.discount,
                        tax = transaction.tax,
                        totalPayment = transaction.totalPayment,
                        paidAmount = transaction.paidAmount,
                        changeAmount = transaction.changeAmount,
                        status = transaction.status,
                        paymentMethod = transaction.paymentMethod,
                        createdAt = transaction.createdAt.orEmpty(),
                        isSynced = true
                    )
                }
            )
        } catch (error: IOException) {
            errors += "Riwayat transaksi: ${error.message ?: "tidak dapat diperbarui"}"
        } catch (error: ApiException) {
            errors += "Riwayat transaksi: ${error.message}"
        }
        return errors
    }

    suspend fun addVehicle(
        plateNumber: String,
        brand: String,
        model: String,
        year: Int?,
        color: String
    ) {
        val user = sessionManager.getCurrentUser()
            ?: throw IOException("Silakan masuk kembali.")
        val response = api.createVehicle(
            VehicleRequest(
                userId = user.id,
                plateNumber = plateNumber.trim().uppercase(),
                brand = brand.trim(),
                model = model.trim(),
                year = year,
                color = color.trim().ifBlank { "-" }
            )
        )
        val vehicle = response.requireBody("Gagal menyimpan kendaraan").data
            ?: throw ApiException("Server tidak mengirim data kendaraan.")
        database.vehicleDao().insertVehicle(
            VehicleEntity(
                localId = vehicle.id.toString(),
                serverId = vehicle.id,
                userId = vehicle.userId,
                ownerName = vehicle.user?.name ?: user.name,
                plateNumber = vehicle.plateNumber,
                brand = vehicle.brand,
                model = vehicle.model,
                year = vehicle.year ?: year ?: 2024,
                color = vehicle.color ?: color,
                isSynced = true
            )
        )
    }

    suspend fun addBooking(
        vehicle: VehicleEntity,
        service: ServiceEntity,
        date: String,
        time: String,
        complaint: String
    ) {
        val user = sessionManager.getCurrentUser()
            ?: throw IOException("Silakan masuk kembali.")
        val vehicleId = vehicle.serverId
            ?: throw IOException("Kendaraan harus disinkronkan sebelum membuat booking.")
        val response = api.createBooking(
            BookingRequest(
                userId = user.id,
                vehicleId = vehicleId,
                serviceId = service.id,
                bookingDate = date,
                bookingTime = time,
                complaint = complaint.trim().ifBlank { "-" }
            )
        )
        val booking = response.requireBody("Gagal membuat booking").data
            ?: throw ApiException("Server tidak mengirim data booking.")
        database.bookingDao().insertBooking(
            BookingEntity(
                localId = "server-${booking.id}",
                serverId = booking.id,
                userId = booking.userId,
                userName = booking.user?.name ?: user.name,
                vehicleId = booking.vehicleId,
                vehiclePlate = booking.vehicle?.plateNumber ?: vehicle.plateNumber,
                serviceId = booking.serviceId,
                serviceName = booking.service?.name ?: service.name,
                bookingDate = booking.bookingDate,
                bookingTime = booking.bookingTime ?: time,
                complaint = booking.complaint ?: complaint,
                status = booking.status,
                isSynced = true
            )
        )
    }

    suspend fun checkout(
        cart: List<CartLine>,
        paidAmount: Double,
        paymentMethod: String
    ): CheckoutResult {
        require(cart.isNotEmpty()) { "Keranjang masih kosong." }
        val subtotal = cart.sumOf { it.product.harga * it.quantity }
        require(paidAmount >= subtotal) { "Uang yang dibayar kurang dari total belanja." }
        cart.forEach { line ->
            require(line.quantity > 0 && line.quantity <= line.product.stok) {
                "Stok ${line.product.name} tidak mencukupi."
            }
        }

        val user = sessionManager.getCurrentUser()
            ?: throw IOException("Silakan masuk kembali untuk membuat transaksi.")
        val localId = UUID.randomUUID().toString()
        val request = TransactionRequest(
            items = cart.map { line ->
                TransactionItemDto(
                    productId = line.product.id,
                    itemName = line.product.name,
                    quantity = line.quantity,
                    price = line.product.harga,
                    subtotal = line.product.harga * line.quantity
                )
            },
            subtotal = subtotal,
            totalPayment = subtotal,
            paidAmount = paidAmount,
            paymentMethod = paymentMethod,
            clientUuid = localId
        )

        val transaction = TransactionEntity(
            localId = localId,
            userId = user.id,
            subtotal = subtotal,
            totalPayment = subtotal,
            paidAmount = paidAmount,
            changeAmount = paidAmount - subtotal,
            paymentMethod = paymentMethod,
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date())
        )
        database.transactionDao().insertTransaction(transaction)
        database.transactionDao().insertTransactionDetails(
            cart.map { line ->
                TransactionDetailEntity(
                    transactionLocalId = localId,
                    productId = line.product.id,
                    itemName = line.product.name,
                    quantity = line.quantity,
                    price = line.product.harga,
                    subtotal = line.product.harga * line.quantity
                )
            }
        )
        cart.forEach { line ->
            database.productDao().decrementStock(line.product.id, line.quantity)
        }

        return try {
            val response = api.createTransaction(request)
            val body = response.requireBody("Server menolak transaksi")
            database.transactionDao().updateTransaction(
                transaction.copy(serverId = body.data?.id, isSynced = true)
            )
            CheckoutResult(localId, synced = true)
        } catch (error: IOException) {
            database.syncQueueDao().insert(
                SyncQueueEntity(
                    entityType = "transaction",
                    action = "INSERT",
                    entityLocalId = localId,
                    payloadJson = gson.toJson(request)
                )
            )
            CheckoutResult(localId, synced = false)
        } catch (error: ApiException) {
            database.syncQueueDao().insert(
                SyncQueueEntity(
                    entityType = "transaction",
                    action = "INSERT",
                    entityLocalId = localId,
                    payloadJson = gson.toJson(request)
                )
            )
            throw error
        }
    }

    suspend fun syncPending(): Int {
        val queueDao = database.syncQueueDao()
        val transactionDao = database.transactionDao()
        val currentUser = sessionManager.getCurrentUser()
            ?: throw IOException("Silakan masuk sebelum menyinkronkan transaksi.")
        val pending = queueDao.getPendingSyncItems()
            .filter { it.entityType == "transaction" }
        var syncedCount = 0

        pending.forEach { item ->
            val transaction = transactionDao.getTransactionByLocalId(item.entityLocalId)
                ?: throw ApiException("Transaksi lokal ${item.entityLocalId} tidak ditemukan.")
            if (transaction.userId != currentUser.id) {
                throw ApiException(
                    "Ada transaksi offline milik akun lain. Masuk dengan akun kasir pembuat transaksi untuk menyinkronkannya."
                )
            }
            val request = gson.fromJson(item.payloadJson, TransactionRequest::class.java)
            val response = api.createTransaction(request)
            val body = response.requireBody("Transaksi ${item.entityLocalId} gagal disinkronkan.")
            transactionDao.updateTransaction(
                transaction.copy(serverId = body.data?.id, isSynced = true)
            )
            queueDao.deleteById(item.id)
            syncedCount++
        }
        return syncedCount
    }

    private fun <T : Any> Response<T>.requireBody(message: String): T {
        if (!isSuccessful) {
            val detail = errorBody()?.string()?.takeIf { it.isNotBlank() }
            throw ApiException(detail ?: "$message (HTTP ${code()})")
        }
        return body() ?: throw ApiException("$message: respons server kosong.")
    }
}

private fun com.bengkel.app.data.remote.model.UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    username = username,
    noHp = noHp,
    role = role,
    photo = photo
)
