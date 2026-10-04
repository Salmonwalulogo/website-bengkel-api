<?php

namespace Tests\Feature;

use App\Models\Category;
use App\Models\Product;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Str;
use Tests\TestCase;

class CashierPosTest extends TestCase
{
    use RefreshDatabase;

    public function test_cashier_can_open_pos_and_complete_a_transaction(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $category = Category::create(['name' => 'Retail']);
        $product = Product::create([
            'category_id' => $category->id,
            'product_code' => 'TEST001',
            'barcode' => '899000000001',
            'name' => 'Produk Test',
            'harga' => 10000,
            'stok' => 5,
            'status' => 'active',
            'is_active' => true,
        ]);

        $this->actingAs($cashier)
            ->get(route('kasir.pos'))
            ->assertOk()
            ->assertSee('Transaksi Pembayaran Kasir');

        $checkoutToken = (string) Str::uuid();
        $response = $this->actingAs($cashier)->post(route('kasir.pos.store'), [
            'checkout_token' => $checkoutToken,
            'items' => [['product_id' => $product->id, 'quantity' => 2]],
            'discount_percent' => 10,
            'discount_nominal' => 0,
            'tax' => 0,
            'other_fee' => 0,
            'paid_amount' => 20000,
            'payment_method' => 'cash',
        ]);

        $response->assertRedirect(route('kasir.pos'));
        $this->assertDatabaseHas('transactions', [
            'user_id' => $cashier->id,
            'invoice_number' => 'TRX-' . now()->format('Ymd') . '-000001',
            'total_payment' => 18000,
            'paid_amount' => 20000,
            'change_amount' => 2000,
        ]);
        $this->assertDatabaseHas('transaction_details', [
            'product_id' => $product->id,
            'quantity' => 2,
            'subtotal' => 20000,
        ]);
        $this->assertDatabaseHas('products', ['id' => $product->id, 'stok' => 3]);

        $this->actingAs($cashier)->post(route('kasir.pos.store'), [
            'checkout_token' => $checkoutToken,
            'items' => [['product_id' => $product->id, 'quantity' => 2]],
            'paid_amount' => 20000,
            'payment_method' => 'cash',
        ]);
        $this->assertDatabaseCount('transactions', 1);
        $this->assertDatabaseHas('products', ['id' => $product->id, 'stok' => 3]);
    }

    public function test_cashier_cannot_complete_transaction_when_payment_is_insufficient(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $category = Category::create(['name' => 'Retail']);
        $product = Product::create([
            'category_id' => $category->id,
            'product_code' => 'TEST002',
            'barcode' => '899000000002',
            'name' => 'Produk Test 2',
            'harga' => 10000,
            'stok' => 5,
            'status' => 'active',
            'is_active' => true,
        ]);

        $response = $this->actingAs($cashier)->post(route('kasir.pos.store'), [
            'checkout_token' => (string) Str::uuid(),
            'items' => [['product_id' => $product->id, 'quantity' => 1]],
            'paid_amount' => 5000,
            'payment_method' => 'cash',
        ]);

        $response->assertSessionHasErrors('paid_amount');
        $this->assertDatabaseCount('transactions', 0);
        $this->assertDatabaseHas('products', ['id' => $product->id, 'stok' => 5]);
    }
}
