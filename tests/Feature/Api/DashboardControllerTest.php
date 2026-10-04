<?php

namespace Tests\Feature\Api;

use App\Models\Booking;
use App\Models\Service;
use App\Models\Transaction;
use App\Models\User;
use App\Models\Vehicle;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class DashboardControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_customer_receives_customer_dashboard_stats(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $vehicle = Vehicle::create([
            'user_id' => $customer->id,
            'plate_number' => 'N 1000 DB',
            'brand' => 'Honda',
            'model' => 'Scoopy',
            'year' => 2021,
            'color' => 'Merah',
        ]);
        $service = Service::create(['name' => 'Servis', 'harga' => 30000]);
        Booking::create([
            'user_id' => $customer->id,
            'vehicle_id' => $vehicle->id,
            'service_id' => $service->id,
            'booking_date' => now()->toDateString(),
            'booking_time' => '08:00',
            'status' => 'pending',
        ]);

        $token = $customer->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->getJson('/api/dashboard/stats');
        $response->assertOk()
            ->assertJson([
                'vehicles_count' => 1,
                'active_bookings' => 1,
                'completed_bookings' => 0,
            ]);
    }

    public function test_admin_receives_admin_dashboard_stats(): void
    {
        $admin = User::factory()->create(['role' => 'admin']);
        $customer = User::factory()->create(['role' => 'customer']);
        Transaction::create([
            'user_id' => $admin->id,
            'customer_id' => $customer->id,
            'subtotal' => 100000,
            'total_payment' => 100000,
            'paid_amount' => 100000,
            'change_amount' => 0,
            'status' => 'completed',
            'payment_method' => 'cash',
        ]);

        $token = $admin->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->getJson('/api/dashboard/stats');
        $response->assertOk()
            ->assertJson([
                'total_customers' => 1,
                'total_transactions' => 1,
                'total_income' => 100000,
            ]);
    }
}
