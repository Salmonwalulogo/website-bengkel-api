<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('transactions', function (Blueprint $table) {
            if (!Schema::hasColumn('transactions', 'checkout_token')) {
                $table->uuid('checkout_token')->nullable()->unique()->after('invoice_number');
            }
        });

        if (DB::table('payments')->count() === DB::table('payments')->distinct('transaction_id')->count()) {
            Schema::table('payments', function (Blueprint $table) {
                $table->unique('transaction_id', 'payments_transaction_id_unique');
            });
        }

        Schema::table('transaction_details', function (Blueprint $table) {
            $table->dropForeign(['product_id']);
            $table->foreign('product_id')
                ->references('id')
                ->on('products')
                ->restrictOnDelete();
        });
    }

    public function down(): void
    {
        Schema::table('transaction_details', function (Blueprint $table) {
            $table->dropForeign(['product_id']);
            $table->foreign('product_id')
                ->references('id')
                ->on('products')
                ->cascadeOnDelete();
        });

        Schema::table('payments', function (Blueprint $table) {
            $table->dropUnique('payments_transaction_id_unique');
        });

        Schema::table('transactions', function (Blueprint $table) {
            $table->dropUnique(['checkout_token']);
            $table->dropColumn('checkout_token');
        });
    }
};
