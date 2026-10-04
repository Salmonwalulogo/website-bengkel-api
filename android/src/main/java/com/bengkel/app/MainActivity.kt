package com.bengkel.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bengkel.app.data.BengkelRepository
import com.bengkel.app.data.CartLine
import com.bengkel.app.data.local.entity.BookingEntity
import com.bengkel.app.data.local.entity.ProductEntity
import com.bengkel.app.data.local.entity.ServiceEntity
import com.bengkel.app.data.local.entity.TransactionEntity
import com.bengkel.app.data.local.entity.UserEntity
import com.bengkel.app.data.local.entity.VehicleEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as BengkelApplication).repository
        setContent {
            MaterialTheme {
                BengkelApp(repository)
            }
        }
    }
}

@Composable
private fun BengkelApp(repository: BengkelRepository) {
    var user by remember { mutableStateOf(repository.sessionManager.getCurrentUser()) }
    if (user == null) {
        LoginScreen(repository) { user = it }
    } else {
        HomeScreen(repository, user!!) {
            repository.sessionManager.clearSession()
            user = null
        }
    }
}

@Composable
private fun LoginScreen(repository: BengkelRepository, onSignedIn: (UserEntity) -> Unit) {
    val scope = rememberCoroutineScope()
    var identifier by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var registering by rememberSaveable { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var apiUrl by rememberSaveable { mutableStateOf(repository.sessionManager.getBaseUrl()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bengkel Rumah Daun", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Aplikasi kasir dan pelanggan",
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        if (registering) {
            OutlinedTextField(name, { name = it }, label = { Text("Nama lengkap") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(username, { username = it }, label = { Text("Username (opsional)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(phone, { phone = it }, label = { Text("Nomor HP (opsional)") }, modifier = Modifier.fillMaxWidth())
        } else {
            OutlinedTextField(
                identifier,
                { identifier = it },
                label = { Text("Email atau username") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        OutlinedTextField(
            password,
            { password = it },
            label = { Text("Kata sandi") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            apiUrl,
            { apiUrl = it },
            label = { Text("Alamat API Laravel") },
            supportingText = { Text("Emulator: 10.0.2.2. Ponsel: gunakan IP/server yang dapat dijangkau.") },
            modifier = Modifier.fillMaxWidth()
        )
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !loading,
            onClick = {
                scope.launch {
                    loading = true
                    error = null
                    try {
                        repository.sessionManager.setBaseUrl(apiUrl)
                        val account = if (registering) {
                            repository.register(name, email, username, phone, password)
                        } else {
                            repository.login(identifier.trim(), password)
                        }
                        onSignedIn(account)
                    } catch (exception: Exception) {
                        error = exception.message ?: "Tidak dapat terhubung ke server."
                    } finally {
                        loading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text(if (registering) "Daftar dan masuk" else "Masuk")
        }
        OutlinedButton(
            onClick = { registering = !registering; error = null },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (registering) "Kembali ke login" else "Daftar sebagai pelanggan")
        }
    }
}

private data class AppTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(repository: BengkelRepository, user: UserEntity, onLogout: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val pending by repository.pendingCount().collectAsState(initial = 0)
    val isStaff = user.role == "admin" || user.role == "kasir"
    val tabs = buildList {
        if (isStaff) add(AppTab("Kasir", Icons.Default.PointOfSale))
        add(AppTab("Katalog", Icons.Default.Inventory2))
        add(AppTab("Booking", Icons.AutoMirrored.Filled.EventNote))
        add(AppTab("Kendaraan", Icons.Default.DirectionsCar))
        if (isStaff) add(AppTab("Riwayat", Icons.Default.Person))
        add(AppTab("Akun", Icons.Default.Settings))
    }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var online by remember { mutableStateOf(hasNetwork(context)) }
    var syncing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            online = hasNetwork(context)
            delay(5_000)
        }
    }
    LaunchedEffect(online, user.id) {
        if (online) {
            try {
                repository.syncPending()
                val errors = repository.refreshData()
                if (errors.isNotEmpty()) snackbar.showSnackbar(errors.first())
            } catch (exception: Exception) {
                snackbar.showSnackbar(exception.message ?: "Sinkronisasi otomatis gagal.")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(tabs[selectedTab.coerceIn(tabs.indices)].title)
                        Text(
                            "${user.name} • ${if (online) "Online" else "Offline"} • antrean $pending",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = !syncing,
                        onClick = {
                            scope.launch {
                                syncing = true
                                try {
                                    val count = repository.syncPending()
                                    val errors = repository.refreshData()
                                    val summary = if (errors.isEmpty()) {
                                        "Data diperbarui. $count transaksi disinkronkan."
                                    } else {
                                        "Sebagian data belum diperbarui: ${errors.first()}"
                                    }
                                    snackbar.showSnackbar(summary)
                                } catch (exception: Exception) {
                                    snackbar.showSnackbar(exception.message ?: "Sinkronisasi gagal.")
                                } finally {
                                    syncing = false
                                }
                            }
                        }
                    ) {
                        if (syncing) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                        else Icon(Icons.Default.CloudSync, contentDescription = "Sinkronkan")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tabs[selectedTab.coerceIn(tabs.indices)].title) {
                "Kasir" -> PosScreen(repository) { message -> scope.launch { snackbar.showSnackbar(message) } }
                "Katalog" -> CatalogScreen(repository)
                "Booking" -> BookingsScreen(repository, user) { message -> scope.launch { snackbar.showSnackbar(message) } }
                "Kendaraan" -> VehiclesScreen(repository, user) { message -> scope.launch { snackbar.showSnackbar(message) } }
                "Riwayat" -> TransactionsScreen(repository)
                "Akun" -> AccountScreen(repository, user, onLogout)
            }
        }
    }
}

@Composable
private fun CatalogScreen(repository: BengkelRepository) {
    val products by repository.products().collectAsState(initial = emptyList())
    val services by repository.services().collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Text("Produk", style = MaterialTheme.typography.titleLarge) }
        if (products.isEmpty()) {
            item { Text("Belum ada produk tersimpan. Sambungkan ke server lalu tekan sinkronkan.") }
        }
        items(products, key = { "product-${it.id}" }) { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                    Text("${rupiah(product.harga)} • stok ${product.stok}")
                    product.description?.takeIf { it.isNotBlank() }?.let { Text(it) }
                }
            }
        }
        item { Text("Layanan", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp)) }
        if (services.isEmpty()) {
            item { Text("Belum ada layanan tersimpan.") }
        }
        items(services, key = { "service-${it.id}" }) { service ->
            ServiceCard(service)
        }
    }
}

@Composable
private fun ServiceCard(service: ServiceEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(service.name, style = MaterialTheme.typography.titleMedium)
            Text("${rupiah(service.harga)}${service.duration?.let { " • $it" } ?: ""}")
            service.description?.takeIf { it.isNotBlank() }?.let { Text(it) }
        }
    }
}

@Composable
private fun PosScreen(repository: BengkelRepository, showMessage: (String) -> Unit) {
    val products by repository.products().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var cart by remember { mutableStateOf(emptyMap<Long, CartLine>()) }
    var paid by rememberSaveable { mutableStateOf("") }
    var paying by remember { mutableStateOf(false) }
    val total = cart.values.sumOf { it.product.harga * it.quantity }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Text("Pilih produk", style = MaterialTheme.typography.titleLarge) }
            if (products.isEmpty()) {
                item { Text("Katalog belum tersimpan. Sambungkan ke server untuk memuat produk.") }
            }
            items(products, key = { it.id }) { product ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("${rupiah(product.harga)} • stok ${product.stok}")
                        }
                        OutlinedButton(
                            enabled = product.stok > 0,
                            onClick = {
                                val existing = cart[product.id]?.quantity ?: 0
                                cart = cart + (product.id to CartLine(product, existing + 1))
                            }
                        ) { Text("Tambah") }
                    }
                }
            }
        }
        Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Keranjang • ${cart.values.sumOf { it.quantity }} item")
                cart.values.forEach { line ->
                    Text("${line.product.name} × ${line.quantity} — ${rupiah(line.product.harga * line.quantity)}")
                }
                Text("Total: ${rupiah(total)}", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = paid,
                    onValueChange = { paid = it.filter(Char::isDigit) },
                    label = { Text("Uang diterima") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    enabled = !paying && cart.isNotEmpty() && (paid.toDoubleOrNull() ?: 0.0) >= total,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            paying = true
                            try {
                                val result = repository.checkout(
                                    cart.values.toList(),
                                    paid.toDoubleOrNull() ?: 0.0,
                                    "cash"
                                )
                                cart = emptyMap()
                                paid = ""
                                showMessage(
                                    if (result.synced) "Transaksi berhasil disimpan ke server."
                                    else "Transaksi tersimpan offline dan akan dikirim saat online."
                                )
                            } catch (exception: Exception) {
                                showMessage(exception.message ?: "Transaksi gagal.")
                            } finally {
                                paying = false
                            }
                        }
                    }
                ) {
                    Text(if (paying) "Menyimpan..." else "Bayar tunai")
                }
            }
        }
    }
}

@Composable
private fun BookingsScreen(
    repository: BengkelRepository,
    user: UserEntity,
    showMessage: (String) -> Unit
) {
    val bookings by repository.bookings().collectAsState(initial = emptyList())
    val visibleBookings = when (user.role) {
        "customer" -> bookings.filter { it.userId == user.id }
        "admin", "kasir" -> bookings
        else -> emptyList()
    }
    var showForm by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Button(
            onClick = { showForm = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Buat booking")
        }
        EntityList(
            title = "Pemesanan servis",
            emptyMessage = "Belum ada pemesanan tersimpan di perangkat.",
            items = visibleBookings,
            key = { it.localId },
            modifier = Modifier.weight(1f)
        ) { booking ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(booking.serviceName ?: "Servis", style = MaterialTheme.typography.titleMedium)
                    Text("${booking.bookingDate} ${booking.bookingTime} • ${booking.status}")
                    Text("Kendaraan: ${booking.vehiclePlate ?: "—"}")
                    booking.userName?.let { Text("Pelanggan: $it") }
                    if (!booking.isSynced) Text("Tersimpan di perangkat (offline)")
                }
            }
        }
    }
    if (showForm) {
        BookingDialog(repository, onDismiss = { showForm = false }, showMessage = showMessage)
    }
}

@Composable
private fun VehiclesScreen(
    repository: BengkelRepository,
    user: UserEntity,
    showMessage: (String) -> Unit
) {
    val vehicles by repository.vehicles().collectAsState(initial = emptyList())
    val visibleVehicles = when (user.role) {
        "customer" -> vehicles.filter { it.userId == user.id }
        "admin", "kasir" -> vehicles
        else -> emptyList()
    }
    var showForm by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Button(
            onClick = { showForm = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Tambah kendaraan")
        }
        EntityList(
            title = "Kendaraan",
            emptyMessage = "Belum ada kendaraan tersimpan di perangkat.",
            items = visibleVehicles,
            key = { it.localId },
            modifier = Modifier.weight(1f)
        ) { vehicle ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(vehicle.plateNumber, style = MaterialTheme.typography.titleMedium)
                    Text("${vehicle.brand} ${vehicle.model} • ${vehicle.year} • ${vehicle.color}")
                    vehicle.ownerName?.let { Text("Pemilik: $it") }
                }
            }
        }
    }
    if (showForm) {
        VehicleDialog(
            repository,
            onDismiss = { showForm = false },
            showMessage = showMessage
        )
    }
}

@Composable
private fun VehicleDialog(
    repository: BengkelRepository,
    onDismiss: () -> Unit,
    showMessage: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var plate by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah kendaraan") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(plate, { plate = it }, label = { Text("Nomor polisi") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(brand, { brand = it }, label = { Text("Merek") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(model, { model = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    year,
                    { year = it.filter(Char::isDigit).take(4) },
                    label = { Text("Tahun (opsional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(color, { color = it }, label = { Text("Warna") }, modifier = Modifier.fillMaxWidth())
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                enabled = !saving && plate.isNotBlank() && brand.isNotBlank() && model.isNotBlank(),
                onClick = {
                    scope.launch {
                        saving = true
                        error = null
                        try {
                            repository.addVehicle(plate, brand, model, year.toIntOrNull(), color)
                            showMessage("Kendaraan berhasil disimpan.")
                            onDismiss()
                        } catch (exception: Exception) {
                            error = exception.message ?: "Kendaraan gagal disimpan. Periksa koneksi server."
                        } finally {
                            saving = false
                        }
                    }
                }
            ) { Text(if (saving) "Menyimpan..." else "Simpan") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Batal") } }
    )
}

@Composable
private fun BookingDialog(
    repository: BengkelRepository,
    onDismiss: () -> Unit,
    showMessage: (String) -> Unit
) {
    val vehicles by repository.vehicles().collectAsState(initial = emptyList())
    val services by repository.services().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val availableVehicles = vehicles.filter { it.serverId != null }
    var selectedVehicle by remember { mutableStateOf<VehicleEntity?>(null) }
    var selectedService by remember { mutableStateOf<ServiceEntity?>(null) }
    var vehicleMenu by remember { mutableStateOf(false) }
    var serviceMenu by remember { mutableStateOf(false) }
    var date by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()))
    }
    var time by remember { mutableStateOf("09:00") }
    var complaint by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Booking servis") },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.foundation.layout.Box {
                    OutlinedButton(
                        onClick = { vehicleMenu = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedVehicle?.let { "${it.plateNumber} • ${it.brand} ${it.model}" } ?: "Pilih kendaraan")
                    }
                    DropdownMenu(expanded = vehicleMenu, onDismissRequest = { vehicleMenu = false }) {
                        availableVehicles.forEach { vehicle ->
                            DropdownMenuItem(
                                text = { Text("${vehicle.plateNumber} • ${vehicle.brand} ${vehicle.model}") },
                                onClick = { selectedVehicle = vehicle; vehicleMenu = false }
                            )
                        }
                    }
                }
                androidx.compose.foundation.layout.Box {
                    OutlinedButton(onClick = { serviceMenu = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(selectedService?.name ?: "Pilih layanan")
                    }
                    DropdownMenu(expanded = serviceMenu, onDismissRequest = { serviceMenu = false }) {
                        services.forEach { service ->
                            DropdownMenuItem(
                                text = { Text("${service.name} • ${rupiah(service.harga)}") },
                                onClick = { selectedService = service; serviceMenu = false }
                            )
                        }
                    }
                }
                OutlinedTextField(date, { date = it }, label = { Text("Tanggal (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(time, { time = it }, label = { Text("Jam (HH:MM)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    complaint,
                    { complaint = it },
                    label = { Text("Keluhan (opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (availableVehicles.isEmpty()) {
                    Text("Tambahkan dan sinkronkan kendaraan sebelum membuat booking.")
                } else if (services.isEmpty()) {
                    Text("Layanan belum tersimpan. Sambungkan ke server untuk memuat katalog layanan.")
                }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                enabled = !saving && selectedVehicle != null && selectedService != null &&
                    date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) &&
                    time.matches(Regex("\\d{2}:\\d{2}")),
                onClick = {
                    scope.launch {
                        saving = true
                        error = null
                        try {
                            repository.addBooking(
                                selectedVehicle!!,
                                selectedService!!,
                                date,
                                time,
                                complaint
                            )
                            showMessage("Booking berhasil dikirim ke server.")
                            onDismiss()
                        } catch (exception: Exception) {
                            error = exception.message ?: "Booking gagal. Periksa koneksi server."
                        } finally {
                            saving = false
                        }
                    }
                }
            ) { Text(if (saving) "Mengirim..." else "Buat booking") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Batal") } }
    )
}

@Composable
private fun TransactionsScreen(repository: BengkelRepository) {
    val transactions by repository.transactions().collectAsState(initial = emptyList())
    EntityList(
        title = "Riwayat transaksi",
        emptyMessage = "Belum ada transaksi tersimpan di perangkat.",
        items = transactions,
        key = { it.localId }
    ) { transaction ->
        TransactionCard(transaction)
    }
}

@Composable
private fun TransactionCard(transaction: TransactionEntity) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(rupiah(transaction.totalPayment), style = MaterialTheme.typography.titleMedium)
            Text("${transaction.paymentMethod} • ${transaction.createdAt.take(16).replace('T', ' ')}")
            Text(
                if (transaction.isSynced) "Tersinkron" else "Menunggu koneksi untuk sinkronisasi"
            )
        }
    }
}

@Composable
private fun AccountScreen(repository: BengkelRepository, user: UserEntity, onLogout: () -> Unit) {
    var showSettings by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(user.name, style = MaterialTheme.typography.headlineSmall)
        Text(user.email)
        Text("Peran: ${user.role}")
        Text("Data katalog dan transaksi tersimpan di perangkat untuk digunakan offline.")
        OutlinedButton(onClick = { showSettings = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Text("  Pengaturan koneksi")
        }
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Text("  Keluar")
        }
    }
    if (showSettings) {
        ApiSettingsDialog(repository) { showSettings = false }
    }
}

@Composable
private fun ApiSettingsDialog(repository: BengkelRepository, onDismiss: () -> Unit) {
    var apiUrl by remember { mutableStateOf(repository.sessionManager.getBaseUrl()) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alamat server Laravel") },
        text = {
            Column {
                OutlinedTextField(
                    value = apiUrl,
                    onValueChange = { apiUrl = it },
                    label = { Text("URL server") },
                    supportingText = { Text("Contoh: https://server-anda.example/api/") }
                )
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsed = android.net.Uri.parse(apiUrl.trim())
                if (parsed.scheme != "https" || parsed.host.isNullOrBlank()) {
                    error = "Masukkan URL server HTTPS yang valid."
                } else {
                    repository.sessionManager.setBaseUrl(apiUrl)
                    onDismiss()
                }
            }) { Text("Simpan") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Batal") } }
    )
}

@Composable
private fun <T> EntityList(
    title: String,
    emptyMessage: String,
    items: List<T>,
    key: (T) -> String,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Text(title, style = MaterialTheme.typography.titleLarge) }
        if (items.isEmpty()) item { Text(emptyMessage) }
        items(items, key = key) { item -> content(item) }
    }
}

private fun hasNetwork(context: Context): Boolean {
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

private fun rupiah(amount: Double): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(amount)
