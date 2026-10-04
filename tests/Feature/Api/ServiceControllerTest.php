<?php

namespace Tests\Feature\Api;

use App\Models\Service;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ServiceControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_authenticated_user_can_list_services(): void
    {
        $user = User::factory()->create();
        Service::create([
            'name' => 'Ganti Oli',
            'description' => 'Ganti oli mesin',
            'harga' => 20000,
        ]);

        $token = $user->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->getJson('/api/services');
        $response->assertOk();
        $this->assertGreaterThanOrEqual(1, count($response->json('data')));
    }

    public function test_admin_can_manage_services(): void
    {
        $admin = User::factory()->create(['role' => 'admin']);
        $token = $admin->createToken('test')->plainTextToken;

        $storeResponse = $this->withToken($token)->postJson('/api/services', [
            'name' => 'Tune Up',
            'description' => 'Pembersihan karburator',
            'harga' => 75000,
            'duration' => '45m',
            'status' => 'active',
        ]);
        $storeResponse->assertCreated()->assertJsonPath('data.name', 'Tune Up');

        $serviceId = $storeResponse->json('data.id');

        $updateResponse = $this->withToken($token)->putJson("/api/services/{$serviceId}", [
            'harga' => 80000,
        ]);
        $updateResponse->assertOk();
        $this->assertEquals(80000, (float) $updateResponse->json('data.harga'));

        $deleteResponse = $this->withToken($token)->deleteJson("/api/services/{$serviceId}");
        $deleteResponse->assertOk();
        $this->assertDatabaseMissing('services', ['id' => $serviceId]);
    }
}
