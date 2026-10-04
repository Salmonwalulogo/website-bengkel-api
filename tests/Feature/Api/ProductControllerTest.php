<?php

namespace Tests\Feature\Api;

use App\Models\Category;
use App\Models\Product;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ProductControllerTest extends TestCase
{
    use RefreshDatabase;

    public function test_can_list_and_show_active_products(): void
    {
        $category = Category::create(['name' => 'Oli Test Category']);
        $product = Product::create([
            'category_id' => $category->id,
            'product_code' => 'PTEST001',
            'name' => 'Oli Mesin Special 1L',
            'harga' => 50000,
            'stok' => 10,
            'status' => 'active',
            'is_active' => true,
        ]);

        $response = $this->getJson('/api/products');
        $response->assertOk();
        $this->assertGreaterThanOrEqual(1, count($response->json('data')));

        $showResponse = $this->getJson("/api/products/{$product->id}");
        $showResponse->assertOk()->assertJsonPath('data.name', 'Oli Mesin Special 1L');
    }
}
