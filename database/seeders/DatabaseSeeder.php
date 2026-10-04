<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        if (! app()->environment(['local', 'testing'])) {
            return;
        }

        User::updateOrCreate(['email' => 'test@example.com'], [
            'name' => 'Test User',
            'username' => 'testuser',
            'role' => 'customer',
            'password' => Hash::make('password'),
        ]);

        User::updateOrCreate(['email' => 'admin@rumahdaun.test'], [
            'name' => 'Administrator Rumah Daun',
            'username' => 'admin',
            'role' => 'admin',
            'password' => Hash::make('Admin12345!'),
        ]);

        User::updateOrCreate(['email' => 'kasir@rumahdaun.test'], [
            'name' => 'Kasir Rumah Daun',
            'username' => 'kasir',
            'role' => 'kasir',
            'password' => Hash::make('Kasir12345!'),
        ]);
    }
}
