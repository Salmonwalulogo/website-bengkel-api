<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rumah Daun Bengkel Otomotif - Servis Motor Profesional & Terpercaya</title>
    <meta name="description" content="Bengkel motor profesional dengan teknisi berpengalaman. Servis rutin, tune up, ganti oli, dan perbaikan motor dengan harga terjangkau.">
    
    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    
    <!-- AOS Animation -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    @vite('resources/css/frontend/frontend.css')

    
    @vite('resources/css/frontend/home.css')
    @vite('resources/js/frontend.js')
</head>
<body class="frontend-page">
    <!-- Navbar -->
    <nav class="navbar" id="navbar">
        <div class="container">
            <a href="{{ route('home') }}" class="navbar-brand">
                <img src="{{ asset('images/logo-rumah-daun.png') }}" alt="Rumah Daun Bengkel Otomotif"  class="page-style-1">
                <div class="brand-text">
                    <h1>RUMAH DAUN</h1>
                    <span>BENGKEL OTOMOTIF</span>
                </div>
            </a>
            <button class="nav-toggle" type="button" aria-label="Buka menu navigasi" aria-expanded="false">
                <i class="bi bi-list"></i>
            </button>
            <div class="nav-links">
                <a href="{{ route('home') }}" class="{{ request()->routeIs('home') ? 'active' : '' }}">Beranda</a>
                <a href="{{ route('layanan') }}" class="{{ request()->routeIs('layanan') ? 'active' : '' }}">Layanan</a>
                <a href="{{ route('produk') }}" class="{{ request()->routeIs('produk') ? 'active' : '' }}">Produk</a>
                <a href="{{ route('tentang') }}" class="{{ request()->routeIs('tentang') ? 'active' : '' }}">Tentang</a>
                <a href="#lokasi">Lokasi</a>
                <a href="{{ route('kontak') }}" class="{{ request()->routeIs('kontak') ? 'active' : '' }}">Kontak</a>
                @auth
                    <a href="{{ url('/dashboard') }}" class="btn-nav btn-login">Dashboard</a>
                @else
                    <a href="{{ route('login') }}" class="btn-nav btn-login">Login</a>
                    <a href="{{ route('register') }}" class="btn-nav btn-register">Register</a>
                @endauth
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero" id="beranda">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <div class="hero-content" data-aos="fade-right">
                        <div class="hero-badge">
                            <i class="bi bi-star-fill"></i>
                            Bengkel Terpercaya No. 1
                        </div>
                        <h1>
                            Servis Motor Profesional
                            <span>Berkualitas & Terpercaya</span>
                        </h1>
                        <p>
                            Rumah Daun Bengkel Otomotif menghadirkan layanan perawatan, tune up, 
                            servis rutin, dan perbaikan motor dengan teknisi berpengalaman dan 
                            suku cadang berkualitas.
                        </p>
                        <div class="hero-buttons">
                            @auth
                                @if(auth()->user()->role === 'customer')
                                    <a href="{{ route('customer.booking.create') }}" class="btn-hero btn-primary-hero">
                                        <i class="bi bi-calendar2-check"></i>
                                        Booking Servis
                                    </a>
                                @else
                                    <a href="{{ url('/dashboard') }}" class="btn-hero btn-primary-hero">
                                        <i class="bi bi-grid"></i>
                                        Buka Dashboard
                                    </a>
                                @endif
                            @else
                                <a href="{{ route('login') }}" class="btn-hero btn-primary-hero">
                                    <i class="bi bi-calendar2-check"></i>
                                    Booking Servis
                                </a>
                            @endauth
                            <a href="{{ route('produk') }}" class="btn-hero btn-primary-hero">
                                <i class="bi bi-cart3"></i>
                                Lihat Produk
                            </a>
                            <a href="{{ route('layanan') }}" class="btn-hero btn-secondary-hero">
                                <i class="bi bi-tools"></i>
                                Layanan Kami
                            </a>
                        </div>
                        <div class="hero-stats">
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
                <div class="col-lg-6" data-aos="fade-left">
                    <div class="hero-console">
                        <div class="console-top"><span>STATUS BENGKEL</span><span class="console-live"><i class="bi bi-circle-fill me-1"></i> Buka hari ini</span></div>
                        <div class="console-bike"><i class="bi bi-motorcycle"></i></div>
                        <div class="console-status"><span class="console-check"><i class="bi bi-check2"></i></span><div><strong>Perawatan lebih terarah</strong><small>Booking, produk, dan riwayat tersimpan rapi.</small></div></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Services Section -->
    <section class="services" id="layanan">
        <div class="container">
            <div class="section-header" data-aos="fade-up">
                <span class="section-tag">
                    <i class="bi bi-tools"></i> Layanan Kami
                </span>
                <h2>Solusi Lengkap Untuk <span>Motor Anda</span></h2>
                <p>Kami menyediakan berbagai layanan profesional untuk menjaga performa motor Anda tetap optimal</p>
            </div>
            <div class="services-grid">
                <div class="service-card" data-aos="fade-up" data-aos-delay="100">
                    <div class="service-icon">
                        <i class="bi bi-droplet"></i>
                    </div>
                    <h3>Ganti Oli</h3>
                    <p>Ganti oli dengan produk berkualitas tinggi untuk menjaga mesin tetap terlumasi dan awet</p>
                </div>
                <div class="service-card" data-aos="fade-up" data-aos-delay="200">
                    <div class="service-icon">
                        <i class="bi bi-gear-wide-connected"></i>
                    </div>
                    <h3>Tune Up</h3>
                    <p>Servis tune up menyeluruh untuk mengembalikan performa mesin seperti baru</p>
                </div>
                <div class="service-card" data-aos="fade-up" data-aos-delay="300">
                    <div class="service-icon">
                        <i class="bi bi-disc"></i>
                    </div>
                    <h3>Servis Rem</h3>
                    <p>Perbaikan dan penggantian kampas rem untuk keselamatan berkendara Anda</p>
                </div>
                <div class="service-card" data-aos="fade-up" data-aos-delay="400">
                    <div class="service-icon">
                        <i class="bi bi-circle"></i>
                    </div>
                    <h3>Ganti Ban</h3>
                    <p>Tersedia berbagai merek ban berkualitas dengan harga kompetitif</p>
                </div>
            </div>
        </div>
    </section>

    <!-- About Section -->
    <section class="about" id="tentang">
        <div class="container">
            <div class="about-content">
                <div class="about-image" data-aos="fade-right">
                    <div class="about-illustration">
                        <i class="bi bi-motorcycle about-illustration-icon"></i>
                    </div>
                </div>
                <div class="about-text" data-aos="fade-left">
                    <span class="section-tag">
                        <i class="bi bi-info-circle"></i> Tentang Kami
                    </span>
                    <h2>Mengapa Memilih <span>Rumah Daun?</span></h2>
                    <p>
                        Rumah Daun Bengkel Otomotif telah berpengalaman lebih dari 10 tahun dalam melayani 
                        kebutuhan perawatan dan perbaikan motor. Kami berkomitmen memberikan layanan terbaik 
                        dengan teknisi profesional dan suku cadang berkualitas.
                    </p>
                    <p>
                        Dengan lebih dari 5000 pelanggan puas, kami terus berinovasi untuk memberikan 
                        pengalaman servis motor yang nyaman dan terpercaya.
                    </p>
                    <div class="about-features">
                        <div class="about-feature-item">
                            <i class="bi bi-check-circle-fill"></i>
                            <div>
                                <strong>Garansi Servis</strong>
                                <p class="about-feature-note">Setiap layanan bergaransi</p>
                            </div>
                        </div>
                        <div class="about-feature-item">
                            <i class="bi bi-check-circle-fill"></i>
                            <div>
                                <strong>Teknisi Bersertifikat</strong>
                                <p class="about-feature-note">Tim profesional berpengalaman</p>
                            </div>
                        </div>
                        <div class="about-feature-item">
                            <i class="bi bi-check-circle-fill"></i>
                            <div>
                                <strong>Harga Transparan</strong>
                                <p class="about-feature-note">Tanpa biaya tersembunyi</p>
                            </div>
                        </div>
                        <div class="about-feature-item">
                            <i class="bi bi-check-circle-fill"></i>
                            <div>
                                <strong>Pengerjaan Cepat</strong>
                                <p class="about-feature-note">Tepat waktu dan berkualitas</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Testimonials Section -->
    <section class="testimonials">
        <div class="container">
            <div class="section-header" data-aos="fade-up">
                <span class="section-tag testimonial-tag">
                    <i class="bi bi-chat-quote"></i> Testimoni
                </span>
                <h2>Apa Kata <span class="testimonial-highlight">Pelanggan Kami</span></h2>
                <p>Kepercayaan pelanggan adalah prioritas utama kami</p>
            </div>
            <div class="testimonials-grid">
                <div class="testimonial-card" data-aos="fade-up" data-aos-delay="100">
                    <div class="testimonial-rating">
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                    </div>
                    <p class="testimonial-text">
                        "Pelayanan sangat profesional dan cepat. Motor saya jadi lebih responsif setelah tune up di sini. Highly recommended!"
                    </p>
                    <div class="testimonial-author">
                        <div class="testimonial-avatar">AS</div>
                        <div class="testimonial-author-info">
                            <h4>Andi Susanto</h4>
                            <p>Pelanggan Setia</p>
                        </div>
                    </div>
                </div>
                <div class="testimonial-card" data-aos="fade-up" data-aos-delay="200">
                    <div class="testimonial-rating">
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                    </div>
                    <p class="testimonial-text">
                        "Harga sangat kompetitif dengan kualitas layanan yang excellent. Teknisi ramah dan menjelaskan dengan detail kondisi motor saya."
                    </p>
                    <div class="testimonial-author">
                        <div class="testimonial-avatar">BW</div>
                        <div class="testimonial-author-info">
                            <h4>Budi Wijaya</h4>
                            <p>Pelanggan Baru</p>
                        </div>
                    </div>
                </div>
                <div class="testimonial-card" data-aos="fade-up" data-aos-delay="300">
                    <div class="testimonial-rating">
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                        <i class="bi bi-star-fill"></i>
                    </div>
                    <p class="testimonial-text">
                        "Sudah 3 tahun servis di Rumah Daun dan tidak pernah kecewa. Selalu puas dengan hasilnya. Terima kasih!"
                    </p>
                    <div class="testimonial-author">
                        <div class="testimonial-avatar">CR</div>
                        <div class="testimonial-author-info">
                            <h4>Citra Rahayu</h4>
                            <p>Pelanggan Setia</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Location Section -->
    <section class="location-section" id="lokasi">
        <div class="container">
            <div class="section-header" data-aos="fade-up">
                <span class="section-tag">
                    <i class="bi bi-geo-alt-fill"></i> Lokasi Kami
                </span>
                <h2>Temukan <span>Rumah Daun</span></h2>
                <p>Kunjungi bengkel kami untuk mendapatkan layanan servis motor terbaik.</p>
            </div>
            <div class="location-content">
                <div class="location-info" data-aos="fade-right">
                    <div class="location-icon">
                        <i class="bi bi-pin-map-fill"></i>
                    </div>
                    <h3>Rumah Daun Bengkel Otomotif</h3>
                    <p class="location-address">
                        <i class="bi bi-geo-alt-fill"></i>
                        Desa Menia, Kecamatan Sabu Barat, Kabupaten Sabu Raijua, Nusa Tenggara Timur, Indonesia
                    </p>
                    <p class="location-hours">
                        <i class="bi bi-clock-fill"></i>
                        Senin - Sabtu, 08:00 - 17:00
                    </p>
                    <a href="https://www.google.com/maps/search/?api=1&query=Desa+Menia%2C+Kecamatan+Sabu+Barat%2C+Kabupaten+Sabu+Raijua%2C+Nusa+Tenggara+Timur%2C+Indonesia"
                       class="btn-location" target="_blank" rel="noopener noreferrer">
                        <i class="bi bi-sign-turn-right-fill"></i>
                        Buka di Google Maps
                    </a>
                </div>
                <div class="location-map" data-aos="fade-left">
                    <iframe
                        src="https://www.google.com/maps?q=Desa+Menia%2C+Kecamatan+Sabu+Barat%2C+Kabupaten+Sabu+Raijua%2C+Nusa+Tenggara+Timur%2C+Indonesia&output=embed"
                        title="Peta lokasi Rumah Daun Bengkel Otomotif"
                        loading="lazy"
                        referrerpolicy="no-referrer-when-downgrade"
                        allowfullscreen></iframe>
                </div>
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta">
        <div class="container">
            <div data-aos="zoom-in">
                <h2>Siap Untuk Servis Motor Anda?</h2>
                <p>Jadwalkan servis motor Anda sekarang dan dapatkan pelayanan terbaik dari teknisi berpengalaman kami. Garansi servis dan harga kompetitif!</p>
                <a href="https://wa.me/6282124626248" class="btn-cta" target="_blank">
                    <i class="bi bi-whatsapp"></i>
                    Hubungi Kami Sekarang
                </a>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="footer-content" data-aos="fade-up">
                <div class="footer-logo">
                    <img src="{{ asset('images/logo-rumah-daun.png') }}" alt="Rumah Daun Bengkel Otomotif">
                </div>
                <h3>RUMAH DAUN BENGKEL OTOMOTIF</h3>
                <p>Solusi terbaik untuk perawatan dan perbaikan motor Anda</p>
                <div class="social-links">
                    <a href="#" title="Facebook"><i class="bi bi-facebook"></i></a>
                    <a href="#" title="Instagram"><i class="bi bi-instagram"></i></a>
                    <a href="https://wa.me/6282124626248" title="WhatsApp"><i class="bi bi-whatsapp"></i></a>
                    <a href="#" title="YouTube"><i class="bi bi-youtube"></i></a>
                </div>
                <div class="footer-bottom">
                    <p>&copy; {{ date('Y') }} Rumah Daun Bengkel Otomotif. All rights reserved.</p>
                    <p class="contact-note">
                        <i class="bi bi-geo-alt me-2"></i>Desa Menia, Kecamatan Sabu Barat, Sabu Raijua
                        <span class="mx-3">|</span>
                        <i class="bi bi-telephone me-2"></i>0821-2462-6248
                    </p>
                </div>
            </div>
        </div>
    </footer>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    
    <script>
        // Initialize AOS Animation
        AOS.init({
            once: true,
            offset: 100,
            duration: 800,
            easing: 'ease-out-cubic',
        });

        // Navbar Scroll Effect
        window.addEventListener('scroll', function() {
            const navbar = document.getElementById('navbar');
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    </script>
</body>
</html>