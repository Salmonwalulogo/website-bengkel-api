<?php

namespace Tests\Feature\Api;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class UserControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_admin_can_list_and_create_users(): void
    {
        $admin = User::factory()->create(['role' => 'admin']);
        $token = $admin->createToken('test')->plainTextToken;

        $storeResponse = $this->withToken($token)->postJson('/api/users', [
            'name' => 'Kasir Baru',
            'email' => 'kasirbaru@example.com',
            'username' => 'kasirbaru',
            'no_hp' => '081234567890',
            'role' => 'kasir',
            'password' => 'secret123',
        ]);

        $storeResponse->assertCreated()->assertJsonPath('data.username', 'kasirbaru');

        $listResponse = $this->withToken($token)->getJson('/api/users');
        $listResponse->assertOk();
    }

    public function test_user_can_update_profile(): void
    {
        $user = User::factory()->create(['name' => 'User Lama']);
        $token = $user->createToken('test')->plainTextToken;

        $response = $this->withToken($token)->putJson('/api/profile', [
            'name' => 'User Baru',
            'no_hp' => '08987654321',
        ]);

        $response->assertOk()->assertJsonPath('user.name', 'User Baru');
        $this->assertDatabaseHas('users', ['id' => $user->id, 'name' => 'User Baru']);
    }

    public function test_cashier_can_only_list_customer_accounts(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        User::factory()->create(['role' => 'admin']);
        User::factory()->create(['role' => 'customer']);
        $token = $cashier->createToken('test')->plainTextToken;

        $this->withToken($token)
            ->getJson('/api/users?role=admin')
            ->assertOk()
            ->assertJsonCount(1, 'data')
            ->assertJsonPath('data.0.role', 'customer');

        $this->withToken($token)
            ->getJson('/api/users')
            ->assertOk()
            ->assertJsonCount(1, 'data')
            ->assertJsonPath('data.0.role', 'customer');
    }

    public function test_cashier_cannot_create_users(): void
    {
        $cashier = User::factory()->create(['role' => 'kasir']);
        $token = $cashier->createToken('test')->plainTextToken;

        $this->withToken($token)
            ->postJson('/api/users', [])
            ->assertForbidden();
    }

    public function test_customer_cannot_list_or_create_users(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $token = $customer->createToken('test')->plainTextToken;

        $this->withToken($token)->getJson('/api/users')->assertForbidden();
        $this->withToken($token)->postJson('/api/users', [])->assertForbidden();
    }
}
