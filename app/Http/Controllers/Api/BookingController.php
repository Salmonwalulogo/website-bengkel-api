<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Booking;
use App\Models\Vehicle;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Validation\ValidationException;

class BookingController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $query = Booking::with(['user:id,name,email,no_hp', 'vehicle', 'service']);

        if ($user->isCustomer()) {
            $query->where('user_id', $user->id);
        }

        $bookings = $query->latest()->get();

        return response()->json(['data' => $bookings]);
    }

    public function store(Request $request)
    {
        $user = $request->user();

        $validated = $request->validate([
            'user_id' => ['nullable', Rule::exists('users', 'id')->where(
                fn ($query) => $query->where('role', 'customer')->orWhere('id', $user->id)
            )],
            'vehicle_id' => ['required', 'exists:vehicles,id'],
            'service_id' => ['required', Rule::exists('services', 'id')->where(fn ($query) => $query->where('status', 'active'))],
            'booking_date' => ['required', 'date'],
            'booking_time' => ['nullable', 'string'],
            'complaint' => ['nullable', 'string'],
        ]);

        $vehicle = Vehicle::findOrFail($validated['vehicle_id']);
        $ownerId = $user->isCustomer()
            ? $user->id
            : ($validated['user_id'] ?? $vehicle->user_id);

        if ((int) $vehicle->user_id !== (int) $ownerId) {
            throw ValidationException::withMessages([
                'vehicle_id' => 'Kendaraan tidak dimiliki oleh pelanggan yang dipilih.',
            ]);
        }

        $booking = Booking::create([
            'user_id' => $ownerId,
            'vehicle_id' => $validated['vehicle_id'],
            'service_id' => $validated['service_id'],
            'booking_date' => $validated['booking_date'],
            'booking_time' => $validated['booking_time'] ?? date('H:i'),
            'complaint' => $validated['complaint'] ?? '-',
            'status' => 'pending',
        ]);

        return response()->json(['message' => 'Pesanan servis berhasil dibuat', 'data' => $booking->load(['vehicle', 'service'])], 201);
    }

    public function updateStatus(Request $request, Booking $booking)
    {
        $user = $request->user();

        $validated = $request->validate([
            'status' => ['required', 'string', 'in:pending,confirmed,in_progress,completed,cancelled'],
            'notes' => ['nullable', 'string'],
        ]);

        $booking->update($validated);

        return response()->json(['message' => 'Status pesanan servis diperbarui', 'data' => $booking->load(['vehicle', 'service'])]);
    }
}
