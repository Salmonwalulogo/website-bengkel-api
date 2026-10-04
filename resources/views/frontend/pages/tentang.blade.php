<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tentang Kami - Rumah Daun Bengkel Otomotif</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    
    @vite('resources/css/frontend/frontend.css')
    @vite('resources/css/frontend/tentang.css')
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
                <a href="{{ route('layanan') }}">Layanan</a>
                <a href="{{ route('produk') }}">Produk</a>
                <a href="{{ route('tentang') }}" class="active">Tentang</a>
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
            <h1>Tentang <span>Kami</span></h1>
            <p class="lead">Mengenal lebih dekat Rumah Daun Bengkel Otomotif</p>
        </div>
    </section>

    <!-- About Section -->
    <section class="about-section">
        <div class="container">
            <div class="about-content">
                <h2>Mengapa Memilih <span>Rumah Daun?</span></h2>
                <p>
                    Rumah Daun Bengkel Otomotif telah berpengalaman lebih dari 10 tahun dalam melayani 
                    kebutuhan perawatan dan perbaikan motor. Kami berkomitmen memberikan layanan terbaik 
                    dengan teknisi profesional dan suku cadang berkualitas.
                </p>
                <p>
                    Dengan lebih dari 5000 pelanggan puas, kami terus berinovasi untuk memberikan 
                    pengalaman servis motor yang nyaman dan terpercaya. Bengkel kami dilengkapi dengan 
                    peralatan modern dan teknisi bersertifikat yang siap melayani berbagai kebutuhan 
                    kendaraan Anda.
                </p>
                <p>
                    Kami memahami bahwa motor Anda adalah investasi penting. Oleh karena itu, setiap 
                    layanan yang kami berikan selalu mengutamakan kualitas, keselamatan, dan kepuasan 
                    pelanggan.
                </p>
                
                <div class="stats">
                    <div class="stat-item">
                        <h3>5000+</h3>
                        <p>Pelanggan Puas</p>
                    </div>
                    <div class="stat-item">
                        <h3>10+</h3>
                        <p>Tahun Pengalaman</p>
                    </div>
                    <div class="stat-item">
                        <h3>50+</h3>
                        <p>Produk Terbaik</p>
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