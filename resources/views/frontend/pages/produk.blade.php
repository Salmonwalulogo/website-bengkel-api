<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produk - Rumah Daun Bengkel Otomotif</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    
    @vite('resources/css/frontend/frontend.css')
    @vite('resources/css/frontend/produk.css')
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
                <a href="{{ route('produk') }}" class="active">Produk</a>
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
            <h1>Produk <span>Kami</span></h1>
            <p class="lead">Berbagai produk berkualitas untuk motor Anda</p>
        </div>
    </section>

    <!-- Products Section -->
    <section class="products-section">
        <div class="container">
            <div class="row">
                @forelse($products as $product)
                <div class="col-md-4">
                    <div class="product-card">
                        <div class="product-image">
                            @if($product->foto && file_exists(public_path('storage/' . $product->foto)))
                                <img src="{{ asset('storage/' . $product->foto) }}" alt="{{ $product->name }}">
                            @else
                                <i class="bi bi-box-seam page-style-3" ></i>
                            @endif
                            <span class="product-badge">Tersedia</span>
                        </div>
                        <div class="product-info">
                            <span class="product-category">{{ $product->category->name ?? 'Umum' }}</span>
                            <h3 class="product-name">{{ $product->name }}</h3>
                            <div class="product-price">
                                Rp {{ number_format($product->harga, 0, ',', '.') }}
                            </div>
                            <div class="d-grid gap-2">
                                <a class="btn-order text-center text-decoration-none" target="_blank" href="https://wa.me/6282124626248?text={{ urlencode('Halo Rumah Daun, saya ingin memesan ' . $product->name . ' dengan harga Rp ' . number_format($product->harga, 0, ',', '.')) }}">
                                    <i class="bi bi-whatsapp me-2"></i>Pesan via WhatsApp
                                </a>
                                <form action="{{ route('cart.add', $product) }}" method="POST">
                                    @csrf
                                    <input type="hidden" name="quantity" value="1">
                                    <button class="btn btn-outline-dark w-100" type="submit"><i class="bi bi-cart-plus me-2"></i>Tambah ke Keranjang</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                @empty
                <div class="col-12 text-center py-5">
                    <i class="bi bi-box-seam page-style-4" ></i>
                    <h3 class="mt-3 page-style-5" >Belum Ada Produk</h3>
                </div>
                @endforelse
            </div>
            
            @if($products->hasPages())
            <div class="mt-4">
                {{ $products->links() }}
            </div>
            @endif
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