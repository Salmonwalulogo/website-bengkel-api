<?php

namespace Tests\Feature\Api;

use App\Models\Category;
use App\Models\Product;
use App\Models\Service;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Str;
use Tests\TestCase;

class SyncControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_cashier_can_batch_sync_offline_data(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $customer = User::factory()->create(['role' => 'customer']);
        $service = Service::create(['name' => 'Servis Ringan', 'harga' => 35000]);
        $category = Category::create(['name' => 'Oli']);
        $product = Product::create([
            'category_id' => $category->id,
            'product_code' => 'OLI01',
            'name' => 'Oli Yamalube',
            'harga' => 45000,
            'stok' => 20,
            'status' => 'active',
            'is_active' => true,
        ]);

        $token = $cashier->createToken('test')->plainTextToken;
        $trxUuid = (string) Str::uuid();
        $vehicle = \App\Models\Vehicle::create([
            'user_id' => $customer->id,
            'plate_number' => 'N 9999 SY',
            'brand' => 'Yamaha',
            'model' => 'Jupiter Z',
            'year' => 2018,
            'color' => 'Hitam',
        ]);

        $response = $this->withToken($token)->postJson('/api/sync', [
            'vehicles' => [
                [
                    'client_id' => 'client-v1',
                    'user_id' => $customer->id,
                    'plate_number' => 'N 9999 SY',
                    'brand' => 'Yamaha',
                    'model' => 'Jupiter Z',
                    'year' => 2018,
                    'color' => 'Hitam',
                ],
            ],
            'bookings' => [
                [
                    'client_id' => 'client-b1',
                    'user_id' => $customer->id,
                    'vehicle_id' => $vehicle->id,
                    'service_id' => $service->id,
                    'booking_date' => now()->toDateString(),
                    'booking_time' => '11:00',
                    'complaint' => 'Ganti oli motor',
                ],
            ],
            'transactions' => [
                [
                    'client_uuid' => $trxUuid,
                    'customer_id' => $customer->id,
                    'subtotal' => 45000,
                    'discount' => 0,
                    'tax' => 0,
                    'total_payment' => 45000,
                    'paid_amount' => 50000,
                    'payment_method' => 'cash',
                    'items' => [
                        [
                            'product_id' => $product->id,
                            'item_name' => 'Oli Yamalube',
                            'quantity' => 1,
                            'price' => 45000,
                        ],
                    ],
                ],
            ],
        ]);

        $response->assertOk()
            ->assertJsonPath('message', 'Sinkronisasi berhasil')
            ->assertJsonPath('synced.vehicles.0.client_id', 'client-v1')
            ->assertJsonPath('synced.bookings.0.client_id', 'client-b1')
            ->assertJsonPath('synced.transactions.0.client_id', $trxUuid);

        $this->assertDatabaseHas('vehicles', ['plate_number' => 'N 9999 SY']);
        $this->assertDatabaseHas('bookings', ['user_id' => $customer->id, 'service_id' => $service->id]);
        $this->assertDatabaseHas('transactions', ['checkout_token' => $trxUuid, 'total_payment' => 45000]);
        $this->assertDatabaseHas('products', ['id' => $product->id, 'stok' => 19]);
    }

    public function test_customer_cannot_claim_another_users_vehicle_during_sync(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $otherCustomer = User::factory()->create(['role' => 'customer']);
        $token = $customer->createToken('test')->plainTextToken;
        $vehicle = \App\Models\Vehicle::create([
            'user_id' => $otherCustomer->id,
            'plate_number' => 'N 8888 SY',
            'brand' => 'Yamaha',
            'model' => 'Jupiter',
            'year' => 2020,
            'color' => 'Hitam',
        ]);

        $this->withToken($token)
            ->postJson('/api/sync', [
                'vehicles' => [[
                    'client_id' => 'client-v1',
                    'plate_number' => $vehicle->plate_number,
                    'brand' => 'Hacked',
                    'model' => 'Jupiter',
                ]],
            ])
            ->assertForbidden();

        $this->assertDatabaseHas('vehicles', [
            'id' => $vehicle->id,
            'user_id' => $otherCustomer->id,
            'brand' => 'Yamaha',
        ]);
    }

    public function test_customer_cannot_sync_cashier_transactions(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $token = $customer->createToken('test')->plainTextToken;

        $this->withToken($token)
            ->postJson('/api/sync', [
                'transactions' => [[
                    'subtotal' => 10000,
                    'total_payment' => 10000,
                    'paid_amount' => 10000,
                    'payment_method' => 'cash',
                    'items' => [[
                        'item_name' => 'Test',
                        'quantity' => 1,
                        'price' => 10000,
                    ]],
                ]],
            ])
            ->assertForbidden();

        $this->assertDatabaseCount('transactions', 0);
    }

    public function test_sync_calculates_product_totals_and_rejects_underpayment(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $category = Category::create(['name' => 'Test']);
        $product = Product::create([
            'category_id' => $category->id,
            'product_code' => 'SYNC01',
            'name' => 'Produk Sinkron',
            'harga' => 30000,
            'stok' => 3,
            'status' => 'active',
            'is_active' => true,
        ]);
        $token = $cashier->createToken('test')->plainTextToken;
        $transaction = [
            'subtotal' => 1,
            'total_payment' => 1,
            'paid_amount' => 30000,
            'payment_method' => 'cash',
            'items' => [[
                'product_id' => $product->id,
                'item_name' => 'Harga palsu',
                'quantity' => 1,
                'price' => 1,
            ]],
        ];

        $response = $this->withToken($token)
            ->postJson('/api/sync', ['transactions' => [$transaction]]);

        $response->assertOk();
        $this->assertDatabaseHas('transactions', [
            'id' => $response->json('synced.transactions.0.server_id'),
            'subtotal' => 30000,
            'total_payment' => 30000,
        ]);
        $this->assertDatabaseHas('transaction_details', [
            'product_id' => $product->id,
            'product_name' => 'Produk Sinkron',
            'price' => 30000,
        ]);

        $transaction['paid_amount'] = 1;
        $transaction['client_uuid'] = (string) Str::uuid();
        $this->withToken($token)
            ->postJson('/api/sync', ['transactions' => [$transaction]])
            ->assertUnprocessable()
            ->assertJsonValidationErrors('paid_amount');

        $this->assertDatabaseCount('transactions', 1);
        $this->assertDatabaseHas('products', ['id' => $product->id, 'stok' => 2]);
    }
}
