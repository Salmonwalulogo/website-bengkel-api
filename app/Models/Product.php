<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Product extends Model
{
    use HasFactory;

    protected $fillable = [
        'category_id',
        'name',
        'product_code',
        'barcode',
        'description',
        'harga',
        'stok',
        'foto',
        'status',
        'is_active',
    ];

    protected $casts = [
        'harga' => 'decimal:2',
        'is_active' => 'boolean',
    ];

    // Relasi ke category
    public function category()
    {
        return $this->belongsTo(Category::class, 'category_id');
    }

    // Relasi ke transaction_details
    public function transactionDetails()
    {
        return $this->hasMany(TransactionDetail::class, 'product_id');
    }

    // Helper: cek apakah produk aktif
    public function isActive()
    {
        return $this->status === 'active' && $this->is_active;
    }

    // Helper: format harga
    public function getFormattedHargaAttribute()
    {
        return 'Rp ' . number_format($this->harga, 0, ',', '.');
    }
}