<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Vehicle;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class VehicleController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $query = Vehicle::with('user:id,name,email,no_hp');

        if ($user->isCustomer()) {
            $query->where('user_id', $user->id);
        } elseif ($request->has('user_id')) {
            $query->where('user_id', $request->user_id);
        }

        $vehicles = $query->latest()->get();

        return response()->json(['data' => $vehicles]);
    }

    public function store(Request $request)
    {
        $user = $request->user();

        $validated = $request->validate([
            'user_id' => ['nullable', Rule::exists('users', 'id')->where(
                fn ($query) => $query->where('role', 'customer')->orWhere('id', $user->id)
            )],
            'plate_number' => ['required', 'string', 'max:20'],
            'brand' => ['required', 'string', 'max:100'],
            'model' => ['required', 'string', 'max:100'],
            'year' => ['nullable', 'integer'],
            'color' => ['nullable', 'string', 'max:50'],
        ]);

        $ownerId = $user->isCustomer() ? $user->id : ($validated['user_id'] ?? $user->id);

        $vehicle = Vehicle::create([
            'user_id' => $ownerId,
            'plate_number' => strtoupper($validated['plate_number']),
            'brand' => $validated['brand'],
            'model' => $validated['model'],
            'year' => $validated['year'] ?? date('Y'),
            'color' => $validated['color'] ?? '-',
        ]);

        return response()->json(['message' => 'Kendaraan berhasil ditambahkan', 'data' => $vehicle->load('user:id,name')], 201);
    }

    public function update(Request $request, Vehicle $vehicle)
    {
        $user = $request->user();

        if ($user->isCustomer() && $vehicle->user_id !== $user->id) {
            return response()->json(['message' => 'Akses ditolak'], 403);
        }

        $validated = $request->validate([
            'plate_number' => ['sometimes', 'string', 'max:20'],
            'brand' => ['sometimes', 'string', 'max:100'],
            'model' => ['sometimes', 'string', 'max:100'],
            'year' => ['sometimes', 'nullable', 'integer'],
            'color' => ['sometimes', 'nullable', 'string', 'max:50'],
        ]);

        if (isset($validated['plate_number'])) {
            $validated['plate_number'] = strtoupper($validated['plate_number']);
        }

        $vehicle->update($validated);

        return response()->json(['message' => 'Data kendaraan diperbarui', 'data' => $vehicle]);
    }

    public function destroy(Request $request, Vehicle $vehicle)
    {
        $user = $request->user();

        if ($user->isCustomer() && $vehicle->user_id !== $user->id) {
            return response()->json(['message' => 'Akses ditolak'], 403);
        }

        $vehicle->delete();

        return response()->json(['message' => 'Kendaraan berhasil dihapus']);
    }
}
