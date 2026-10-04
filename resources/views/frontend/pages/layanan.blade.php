<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Layanan - Rumah Daun Bengkel Otomotif</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    
    @vite('resources/css/frontend/frontend.css')
    @vite('resources/css/frontend/layanan.css')
    @vite('resources/js/frontend.js')
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <div class="container">
            <a href="{{ route('home') }}" class="navbar-brand d-flex align-items-center gap-3">
                <img src="{{ asset('images/logo-rumah-daun.png') }}" alt="Logo">
                <div>
                    <div  class="page-style-1">RUMAH DAUN</div>
                    <div  class="page-style-2">BENGKEL OTOMOTIF</div>
                </div>
            </a>
            <button class="nav-toggle" type="button" aria-label="Buka menu navigasi" aria-expanded="false">
                <i class="bi bi-list"></i>
            </button>
            <div class="nav-links">
                <a href="{{ route('home') }}">Beranda</a>
                <a href="{{ route('layanan') }}" class="active">Layanan</a>
                <a href="{{ route('produk') }}">Produk</a>
                <a href="{{ route('tentang') }}">Tentang</a>
                <a href="{{ route('kontak') }}">Kontak</a>
                @auth
                    <a href="{{ url('/dashboard') }}" class="btn btn-sm btn-outline-light ms-3">Dashboard</a>
                @else
                    <a href="{{ route('login') }}" class="btn btn-sm btn-outline-light ms-3">Login</a>
                @endauth
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <section class="page-header">
        <div class="container">
            <h1>Layanan <span>Kami</span></h1>
            <p class="lead">Solusi lengkap untuk perawatan dan perbaikan motor Anda</p>
        </div>
    </section>

    <!-- Services Section -->
    <section class="services-section">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-droplet"></i>
                        </div>
                        <h3>Ganti Oli</h3>
                        <p>Ganti oli dengan produk berkualitas tinggi untuk menjaga mesin tetap terlumasi dan awet. Tersedia berbagai merek oli ternama.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-gear-wide-connected"></i>
                        </div>
                        <h3>Tune Up</h3>
                        <p>Servis tune up menyeluruh untuk mengembalikan performa mesin seperti baru. Pemeriksaan sistem pengapian, bahan bakar, dan pendingin.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-disc"></i>
                        </div>
                        <h3>Servis Rem</h3>
                        <p>Perbaikan dan penggantian kampas rem untuk keselamatan berkendara Anda. Sistem rem diperiksa secara menyeluruh.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-circle"></i>
                        </div>
                        <h3>Ganti Ban</h3>
                        <p>Tersedia berbagai merek ban berkualitas dengan harga kompetitif. Pemasangan dilakukan dengan peralatan modern.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-battery-charging"></i>
                        </div>
                        <h3>Servis Kelistrikan</h3>
                        <p>Perbaikan sistem kelistrikan motor termasuk aki, lampu, dan komponen kelistrikan lainnya.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="service-card">
                        <div class="service-icon">
                            <i class="bi bi-tools"></i>
                        </div>
                        <h3>Overhaul Mesin</h3>
                        <p>Servis besar untuk mesin yang membutuhkan perbaikan menyeluruh. Ditangani oleh teknisi berpengalaman.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <div class="container">
            <p>&copy; {{ date('Y') }} Rumah Daun Bengkel Otomotif. All rights reserved.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>