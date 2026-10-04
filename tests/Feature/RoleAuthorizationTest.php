<?php

namespace Tests\Feature;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class RoleAuthorizationTest extends TestCase
{
    use RefreshDatabase;

    public function test_web_admin_routes_require_admin_role(): void
    {
        $this->get('/admin/dashboard')->assertRedirect('/login');

        $this->actingAs(User::factory()->create(['role' => 'kasir']))
            ->get('/admin/dashboard')
            ->assertForbidden();
    }

    public function test_web_cashier_and_customer_routes_reject_other_roles(): void
    {
        $customer = User::factory()->create(['role' => 'customer']);
        $admin = User::factory()->create(['role' => 'admin']);

        $this->actingAs($customer)
            ->get('/kasir/dashboard')
            ->assertForbidden();

        $this->actingAs($admin)
            ->get('/customer/dashboard')
            ->assertForbidden();
    }
}
