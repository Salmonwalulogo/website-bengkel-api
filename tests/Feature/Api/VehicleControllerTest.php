<?php

namespace Tests\Feature\Api;

use App\Models\User;
use App\Models\Vehicle;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class VehicleControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_customer_can_list_and_create_vehicles(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $vehicle = Vehicle::create([
            'user_id' => $customer->id,
            'plate_number' => 'N 1111 AB',
            'brand' => 'Honda',
            'model' => 'Supra X',
            'year' => 2020,
            'color' => 'Hitam',
        ]);

        $token = $customer->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->getJson('/api/vehicles');
        $response->assertOk()->assertJsonCount(1, 'data');

        $storeResponse = $this->withToken($token)->postJson('/api/vehicles', [
            'plate_number' => 'B 1234 ABC',
            'brand' => 'Honda',
            'model' => 'Vario 150',
            'year' => 2022,
            'color' => 'Hitam',
        ]);

        $storeResponse->assertCreated()->assertJsonPath('data.plate_number', 'B 1234 ABC');
        $this->assertDatabaseHas('vehicles', ['plate_number' => 'B 1234 ABC', 'user_id' => $customer->id]);
    }

    public function test_customer_can_update_and_delete_own_vehicle(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $vehicle = Vehicle::create([
            'user_id' => $customer->id,
            'plate_number' => 'N 2222 CD',
            'brand' => 'Yamaha',
            'model' => 'Mio',
            'year' => 2019,
            'color' => 'Merah',
        ]);

        $token = $customer->createToken('test')->plainTextToken;

        $updateResponse = $this->withToken($token)->putJson("/api/vehicles/{$vehicle->id}", [
            'brand' => 'Honda',
        ]);
        $updateResponse->assertOk();
        $this->assertDatabaseHas('vehicles', ['id' => $vehicle->id, 'brand' => 'Honda']);

        $deleteResponse = $this->withToken($token)->deleteJson("/api/vehicles/{$vehicle->id}");
        $deleteResponse->assertOk();
        $this->assertDatabaseMissing('vehicles', ['id' => $vehicle->id]);
    }

    public function test_customer_cannot_modify_other_customer_vehicle(): void
    {
        $customer1 = User::factory()->create(['role' => 'customer']);
        $customer2 = User::factory()->create(['role' => 'customer']);
        $vehicle = Vehicle::create([
            'user_id' => $customer2->id,
            'plate_number' => 'N 3333 EF',
            'brand' => 'Suzuki',
            'model' => 'Spin',
            'year' => 2018,
            'color' => 'Biru',
        ]);

        $token = $customer1->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->putJson("/api/vehicles/{$vehicle->id}", [
            'brand' => 'Hacked',
        ]);
        $response->assertForbidden();
    }

    public function test_cashier_can_create_a_vehicle_for_themselves_or_a_customer(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $customer = User::factory()->create(['role' => 'customer']);
        $token = $cashier->createToken('test')->plainTextToken;

        $vehicleData = [
            'plate_number' => 'N 5555 GH',
            'brand' => 'Honda',
            'model' => 'Beat',
            'year' => 2022,
            'color' => 'Hitam',
        ];

        $this->withToken($token)
            ->postJson('/api/vehicles', ['user_id' => $cashier->id, ...$vehicleData])
            ->assertCreated()
            ->assertJsonPath('data.user_id', $cashier->id);

        $vehicleData['plate_number'] = 'N 6666 IJ';
        $this->withToken($token)
            ->postJson('/api/vehicles', ['user_id' => $customer->id, ...$vehicleData])
            ->assertCreated()
            ->assertJsonPath('data.user_id', $customer->id);
    }
}
