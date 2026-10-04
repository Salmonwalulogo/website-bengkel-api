<?php

use App\Http\Controllers\ProfileController;
use App\Http\Controllers\SettingsController; 
use App\Http\Controllers\Admin\AdminDashboardController;
use App\Http\Controllers\Admin\SettingController as AdminSettingController; 
use App\Http\Controllers\Customer\CustomerDashboardController;
use App\Http\Controllers\Cashier\CashierDashboardController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\ContactMessageController;
use App\Models\Product;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes - Public (Bisa diakses siapa saja)
|--------------------------------------------------------------------------
*/

// Halaman Utama (Beranda)
Route::get('/', function () {
    // Mengambil 8 produk terbaru untuk ditampilkan di homepage
    $products = Product::where('status', 'active')
                       ->latest()
                       ->take(8)
                       ->get();
    return view('frontend.home', compact('products'));
})->name('home');

// Halaman Layanan
Route::get('/layanan', function () {
    return view('frontend.pages.layanan');
})->name('layanan');

// Halaman Produk (Dengan Pagination)
Route::get('/produk', function () {
    $products = Product::where('status', 'active')
                       ->latest()
                       ->paginate(12);
    return view('frontend.pages.produk', compact('products'));
})->name('produk');

// Halaman Tentang Kami
Route::get('/tentang', function () {
    return view('frontend.pages.tentang');
})->name('tentang');

// Halaman Kontak & Lokasi
Route::get('/kontak', function () {
    return view('frontend.pages.kontak');
})->name('kontak');
Route::post('/kontak', [ContactMessageController::class, 'store'])
    ->middleware('throttle:5,1')
    ->name('kontak.store');

Route::get('/keranjang', [CartController::class, 'index'])->name('cart.index');
Route::post('/keranjang/checkout', [CartController::class, 'checkout'])
    ->middleware(['auth', 'role:customer'])
    ->name('cart.checkout');
Route::post('/keranjang/{product}', [CartController::class, 'add'])->name('cart.add');
Route::patch('/keranjang/{product}', [CartController::class, 'update'])->name('cart.update');
Route::delete('/keranjang/{product}', [CartController::class, 'remove'])->name('cart.remove');


/*
|--------------------------------------------------------------------------
| Web Routes - Authenticated (Harus Login)
|--------------------------------------------------------------------------
*/

Route::middleware(['auth'])->group(function () {
    
    // Dashboard redirect berdasarkan role user
    Route::get('/dashboard', function () {
        $user = auth()->user();
        return match($user->role) {
            'admin'    => redirect()->route('admin.dashboard'),
            'kasir'    => redirect()->route('kasir.dashboard'),
            'customer' => redirect()->route('customer.dashboard'),
            default    => redirect()->route('login'),
        };
    })->name('dashboard');

    // Profile routes (Bawaan Laravel Breeze)
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');

    // ✅ PENGATURAN UMUM (Bisa diakses semua role untuk edit profil sendiri)
    Route::get('/settings', [SettingsController::class, 'edit'])->name('settings.edit');
    Route::put('/settings', [SettingsController::class, 'update'])->name('settings.update');
});


/*
|--------------------------------------------------------------------------
| Admin Routes (Khusus Role: admin)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth', 'role:admin'])->prefix('admin')->name('admin.')->group(function () {
    Route::get('/dashboard', [AdminDashboardController::class, 'index'])->name('dashboard');
     
    // Resource CRUD
    Route::resource('categories', \App\Http\Controllers\Admin\CategoryController::class)->except('show');
    Route::resource('products', \App\Http\Controllers\Admin\ProductController::class);
    Route::resource('users', \App\Http\Controllers\Admin\UserController::class)->except('show');
    Route::resource('vehicles', \App\Http\Controllers\Admin\VehicleController::class)->except('show');
    Route::resource('services', \App\Http\Controllers\Admin\ServiceController::class)->except('show');
    Route::resource('bookings', \App\Http\Controllers\Admin\BookingController::class);
    Route::resource('transactions', \App\Http\Controllers\Admin\TransactionController::class)->only(['index', 'create', 'store', 'show', 'destroy']);
    Route::patch('/transactions/{transaction}/status', [\App\Http\Controllers\Admin\TransactionController::class, 'updateStatus'])->name('transactions.updateStatus');
    Route::resource('payments', \App\Http\Controllers\Admin\PaymentController::class)->except('show');
    Route::resource('notifications', \App\Http\Controllers\Admin\NotificationController::class)->only(['index', 'create', 'store', 'destroy']);
    Route::resource('contact-messages', \App\Http\Controllers\Admin\ContactMessageController::class)
        ->only(['index', 'show', 'destroy']);
    Route::post('/notifications/{id}/read', [\App\Http\Controllers\Admin\NotificationController::class, 'markAsRead'])
        ->name('notifications.markAsRead');
    
    // Laporan
    Route::get('/reports', [\App\Http\Controllers\Admin\ReportController::class, 'index'])->name('reports.index');
     
    // ✅ PENGATURAN KHUSUS ADMIN
    Route::prefix('settings')->name('settings.')->group(function () {
        Route::get('/profile', [AdminSettingController::class, 'profile'])->name('profile');
        Route::put('/profile', [AdminSettingController::class, 'updateProfile'])->name('profile.update');
        
        Route::get('/account', [AdminSettingController::class, 'account'])->name('account');
        Route::put('/account', [AdminSettingController::class, 'updateAccount'])->name('account.update');
        
        Route::get('/general', [AdminSettingController::class, 'general'])->name('general');
        Route::put('/general', [AdminSettingController::class, 'updateGeneral'])->name('general.update');
        
        Route::get('/notification', [AdminSettingController::class, 'notification'])->name('notification');
        Route::put('/notification', [AdminSettingController::class, 'updateNotification'])->name('notification.update');
        
        Route::get('/backup', [AdminSettingController::class, 'backup'])->name('backup');
    });
});


/*
|--------------------------------------------------------------------------
| Customer Routes (Khusus Role: customer)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth', 'role:customer'])->prefix('customer')->name('customer.')->group(function () {
    Route::get('/dashboard', [CustomerDashboardController::class, 'index'])->name('dashboard');
    Route::get('/kendaraan', [CustomerDashboardController::class, 'vehicles'])->name('vehicles');
    Route::post('/kendaraan', [CustomerDashboardController::class, 'storeVehicle'])->name('vehicles.store');
    Route::delete('/kendaraan/{vehicle}', [CustomerDashboardController::class, 'destroyVehicle'])->name('vehicles.destroy');
    Route::get('/booking', [CustomerDashboardController::class, 'createBooking'])->name('booking.create');
    Route::post('/booking', [CustomerDashboardController::class, 'storeBooking'])->name('booking.store');
    Route::get('/pesanan', [CustomerDashboardController::class, 'bookings'])->name('bookings');
    Route::get('/transaksi', [CustomerDashboardController::class, 'transactions'])->name('transactions');
    Route::get('/notifikasi', [CustomerDashboardController::class, 'notifications'])->name('notifications');
    Route::post('/notifikasi/{notification}/read', [CustomerDashboardController::class, 'markNotificationAsRead'])->name('notifications.read');
});


/*
|--------------------------------------------------------------------------
| Cashier Routes (Khusus Role: kasir)
|--------------------------------------------------------------------------
*/
Route::middleware(['auth', 'role:kasir'])->prefix('kasir')->name('kasir.')->group(function () {
    Route::get('/dashboard', [CashierDashboardController::class, 'index'])->name('dashboard');
    Route::get('/pesanan', [CashierDashboardController::class, 'orders'])->name('orders');
    Route::get('/pembayaran', [CashierDashboardController::class, 'payments'])->name('payments');
    Route::get('/invoice', [CashierDashboardController::class, 'invoices'])->name('invoices');
    Route::get('/transaksi', [CashierDashboardController::class, 'pos'])->name('pos');
    Route::post('/transaksi', [CashierDashboardController::class, 'storeTransaction'])->name('pos.store');
});


/*
|--------------------------------------------------------------------------
| Authentication Routes (Bawaan Laravel Breeze)
|--------------------------------------------------------------------------
*/
require __DIR__.'/auth.php';