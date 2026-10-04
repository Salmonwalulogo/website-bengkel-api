<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('products', function (Blueprint $table) {
            if (!Schema::hasColumn('products', 'product_code')) {
                $table->string('product_code', 50)->nullable()->unique()->after('name');
            }
            if (!Schema::hasColumn('products', 'barcode')) {
                $table->string('barcode', 80)->nullable()->unique()->after('product_code');
            }
        });

        Schema::table('transactions', function (Blueprint $table) {
            foreach ([
                'subtotal' => ['decimal', 12, 2],
                'discount' => ['decimal', 12, 2],
                'tax' => ['decimal', 12, 2],
                'other_fee' => ['decimal', 12, 2],
                'paid_amount' => ['decimal', 12, 2],
                'change_amount' => ['decimal', 12, 2],
            ] as $column => $definition) {
                if (!Schema::hasColumn('transactions', $column)) {
                    $table->{$definition[0]}($column, $definition[1], $definition[2])->default(0);
                }
            }
            if (!Schema::hasColumn('transactions', 'customer_id')) {
                $table->foreignId('customer_id')->nullable()->after('user_id')->constrained('users')->nullOnDelete();
            }
        });

        Schema::table('transaction_details', function (Blueprint $table) {
            if (!Schema::hasColumn('transaction_details', 'product_code')) {
                $table->string('product_code', 50)->nullable()->after('product_id');
            }
            if (!Schema::hasColumn('transaction_details', 'product_name')) {
                $table->string('product_name')->nullable()->after('product_code');
            }
        });

        if (DB::getDriverName() === 'mysql') {
            DB::statement("ALTER TABLE transactions MODIFY payment_method VARCHAR(20) NULL");
        }

        if (DB::table('products')->count() === 0) {
            $categoryId = DB::table('categories')->value('id');
            if (!$categoryId) {
                $categoryId = DB::table('categories')->insertGetId([
                    'name' => 'Suku Cadang Bengkel',
                    'description' => 'Suku cadang dan perlengkapan servis kendaraan',
                    'created_at' => now(),
                    'updated_at' => now(),
                ]);
            }

            $products = [
                ['BRG001', '899123456001', 'Oli Mesin 10W-40 1 Liter', 65000],
                ['BRG002', '899123456002', 'Busi Motor NGK', 22000],
                ['BRG003', '899123456003', 'Kampas Rem Depan Motor', 45000],
                ['BRG004', '899123456004', 'Filter Oli Motor', 18000],
                ['BRG005', '899123456005', 'Aki Motor 12V', 285000],
                ['BRG006', '899123456006', 'Rantai Motor DID', 175000],
            ];

            foreach ($products as [$code, $barcode, $name, $price]) {
                DB::table('products')->insert([
                    'category_id' => $categoryId,
                    'product_code' => $code,
                    'barcode' => $barcode,
                    'name' => $name,
                    'harga' => $price,
                    'stok' => 100,
                    'status' => 'active',
                    'is_active' => true,
                    'created_at' => now(),
                    'updated_at' => now(),
                ]);
            }
        }
    }

    public function down(): void
    {
        Schema::table('transaction_details', function (Blueprint $table) {
            $table->dropColumn(['product_code', 'product_name']);
        });
        Schema::table('transactions', function (Blueprint $table) {
            $table->dropForeign(['customer_id']);
            $table->dropColumn(['customer_id', 'subtotal', 'discount', 'tax', 'other_fee', 'paid_amount', 'change_amount']);
        });
        Schema::table('products', function (Blueprint $table) {
            $table->dropColumn(['product_code', 'barcode']);
        });
    }
};
