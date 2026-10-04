<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Product;
use Illuminate\Http\Request;

class ProductController extends Controller
{
    public function index()
    {
        $products = Product::with('category:id,name')
            ->where('status', 'active')
            ->latest()
            ->paginate(12);

        return response()->json([
            'data' => $products->items(),
            'meta' => [
                'current_page' => $products->currentPage(),
                'last_page' => $products->lastPage(),
                'per_page' => $products->perPage(),
                'total' => $products->total(),
            ],
        ]);
    }

    public function show(Product $product)
    {
        abort_unless($product->status === 'active', 404, 'Produk tidak ditemukan.');

        return response()->json([
            'data' => $product->load('category:id,name'),
        ]);
    }
}
