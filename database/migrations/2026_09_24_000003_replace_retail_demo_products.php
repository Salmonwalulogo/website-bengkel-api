<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up(): void
    {
        $categoryId = DB::table('categories')
            ->where('name', 'Produk Retail')
            ->value('id');

        if (!$categoryId) {
            return;
        }

        DB::table('categories')
            ->where('id', $categoryId)
            ->update([
                'name' => 'Suku Cadang Bengkel',
                'description' => 'Suku cadang dan perlengkapan servis kendaraan',
                'updated_at' => now(),
            ]);

        $products = [
            ['BRG001', '899123456001', 'Oli Mesin 10W-40 1 Liter', 65000],
            ['BRG002', '899123456002', 'Busi Motor NGK', 22000],
            ['BRG003', '899123456003', 'Kampas Rem Depan Motor', 45000],
            ['BRG004', '899123456004', 'Filter Oli Motor', 18000],
            ['BRG005', '899123456005', 'Aki Motor 12V', 285000],
            ['BRG006', '899123456006', 'Rantai Motor DID', 175000],
        ];

        foreach ($products as [$code, $barcode, $name, $price]) {
            DB::table('products')
                ->where('category_id', $categoryId)
                ->where('product_code', $code)
                ->whereIn('name', [
                    'Indomie Goreng',
                    'Aqua 600ml',
                    'Teh Botol Sosro',
                    'Beras 5 Kg',
                    'Minyak Goreng 1 Liter',
                    'Gula Pasir 1 Kg',
                ])
                ->update([
                    'barcode' => $barcode,
                    'name' => $name,
                    'description' => 'Suku cadang bengkel',
                    'harga' => $price,
                    'updated_at' => now(),
                ]);
        }
    }

    public function down(): void
    {
        $categoryId = DB::table('categories')
            ->where('name', 'Suku Cadang Bengkel')
            ->value('id');

        if (!$categoryId) {
            return;
        }

        $products = [
            ['BRG001', 'Indomie Goreng', 3500],
            ['BRG002', 'Aqua 600ml', 4000],
            ['BRG003', 'Teh Botol Sosro', 5000],
            ['BRG004', 'Beras 5 Kg', 75000],
            ['BRG005', 'Minyak Goreng 1 Liter', 18000],
            ['BRG006', 'Gula Pasir 1 Kg', 17000],
        ];

        foreach ($products as [$code, $name, $price]) {
            DB::table('products')
                ->where('category_id', $categoryId)
                ->where('product_code', $code)
                ->update(['name' => $name, 'harga' => $price, 'description' => null, 'updated_at' => now()]);
        }

        DB::table('categories')->where('id', $categoryId)->update([
            'name' => 'Produk Retail',
            'description' => 'Kategori produk demo kasir',
            'updated_at' => now(),
        ]);
    }
};
