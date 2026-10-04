@extends('backend.layouts.cashier')

@section('title', 'Kasir Dashboard')
@section('page-title', 'Ringkasan Operasional')

@section('content')
<div class="container-fluid">
    <div class="dashboard-hero cashier-hero mb-4">
        <div>
            <span class="dashboard-eyebrow"><i class="fas fa-clock me-2"></i>SHIFT KASIR HARI INI</span>
            <h1>Halo, {{ auth()->user()->name }}!</h1>
            <p>Semua pesanan, pembayaran, dan invoice bengkel ada dalam satu kendali.</p>
        </div>
        <div class="hero-date"><i class="fas fa-calendar-alt me-2"></i>{{ now()->translatedFormat('d F Y') }}</div>
    </div>

    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card shadow-sm h-100 stat-card stat-card-amber">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                Pesanan Pending</div>
                            <div class="h3 mb-0 font-weight-bold text-gray-800">{{ $pendingOrders }}</div>
                        </div>
                        <div class="col-auto">
                            <span class="stat-icon"><i class="fas fa-shopping-cart"></i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4 mb-4">
            <div class="card shadow-sm h-100 stat-card stat-card-teal">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                Pembayaran Hari Ini</div>
                            <div class="h3 mb-0 font-weight-bold text-gray-800">{{ $todayPayments }}</div>
                        </div>
                        <div class="col-auto">
                            <span class="stat-icon"><i class="fas fa-money-bill-wave"></i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4 mb-4">
            <div class="card shadow-sm h-100 stat-card stat-card-blue">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Total Transaksi</div>
                            <div class="h3 mb-0 font-weight-bold text-gray-800">{{ $totalTransactions }}</div>
                        </div>
                        <div class="col-auto">
                            <span class="stat-icon"><i class="fas fa-receipt"></i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="quick-actions mb-4">
        <a href="{{ route('kasir.orders') }}"><i class="fas fa-shopping-bag"></i><span>Kelola Pesanan</span><small>Periksa booking masuk</small></a>
        <a href="{{ route('kasir.pos') }}"><i class="fas fa-cash-register"></i><span>Transaksi Kasir</span><small>Buka halaman POS</small></a>
        <a href="{{ route('kasir.invoices') }}"><i class="fas fa-file-invoice"></i><span>Lihat Invoice</span><small>Cetak bukti transaksi</small></a>
    </div>
</div>
@endsection