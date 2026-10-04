<?php

namespace App\Http\Controllers\Cashier;

use App\Http\Controllers\Controller;
use App\Models\Booking;
use App\Models\Payment;
use App\Models\Product;
use App\Models\TransactionDetail;
use App\Models\Transaction;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;
use Illuminate\Validation\ValidationException;
use Illuminate\Support\Str;

class CashierDashboardController extends Controller
{
    public function index()
    {
        return view('backend.cashier.dashboard', [
            'pendingOrders' => Booking::whereIn('status', ['pending', 'confirmed'])->count(),
            'todayPayments' => Payment::where('status', 'paid')
                ->whereDate('payment_date', Carbon::today())
                ->count(),
            'totalTransactions' => Transaction::count(),
        ]);
    }

    public function orders()
    {
        $bookings = Booking::with(['user', 'vehicle', 'service'])
            ->latest('booking_date')
            ->paginate(10);

        return view('backend.cashier.orders', compact('bookings'));
    }

    public function payments()
    {
        $payments = Payment::with('transaction')
            ->latest('payment_date')
            ->paginate(10);

        return view('backend.cashier.payments', compact('payments'));
    }

    public function invoices()
    {
        $transactions = Transaction::with('user')
            ->latest()
            ->paginate(10);

        return view('backend.cashier.invoices', compact('transactions'));
    }

    public function pos()
    {
        $products = Product::query()
            ->where('status', 'active')
            ->where('is_active', true)
            ->where('stok', '>', 0)
            ->orderBy('name')
            ->get();

        $customers = User::where('role', 'customer')->orderBy('name')->get();

        return view('backend.cashier.pos', [
            'products' => $products,
            'customers' => $customers,
            'transactionNumber' => Transaction::nextInvoiceNumber(),
        ]);
    }

    public function storeTransaction(Request $request)
    {
        $validated = $request->validate([
            'customer_id' => [
                'nullable',
                Rule::exists('users', 'id')->where(fn ($query) => $query->where('role', 'customer')),
            ],
            'items' => ['required', 'array', 'min:1'],
            'checkout_token' => ['required', 'uuid'],
            'items.*.product_id' => ['required', 'integer', 'exists:products,id'],
            'items.*.quantity' => ['required', 'integer', 'min:1'],
            'discount_percent' => ['nullable', 'numeric', 'min:0', 'max:100'],
            'discount_nominal' => ['nullable', 'numeric', 'min:0'],
            'tax' => ['nullable', 'numeric', 'min:0'],
            'other_fee' => ['nullable', 'numeric', 'min:0'],
            'paid_amount' => ['required', 'numeric', 'min:0'],
            'payment_method' => ['required', 'in:cash,qris,debit,kredit,ewallet,transfer'],
        ]);

        $existingTransaction = Transaction::where('checkout_token', $validated['checkout_token'])
            ->where('user_id', $request->user()->id)
            ->first();
        if ($existingTransaction) {
            return redirect()->route('kasir.pos')->with('receipt', $existingTransaction->load(['user', 'customer', 'details']));
        }

        try {
            $transaction = DB::transaction(function () use ($validated, $request) {
            $subtotal = 0;
            $details = [];
            $itemsByProduct = collect($validated['items'])
                ->groupBy('product_id')
                ->map(fn ($items) => [
                    'product_id' => (int) $items->first()['product_id'],
                    'quantity' => $items->sum('quantity'),
                ])
                ->values();

            if ($validated['customer_id'] ?? null) {
                User::whereKey($validated['customer_id'])
                    ->where('role', 'customer')
                    ->firstOrFail();
            }

            foreach ($itemsByProduct as $item) {
                $product = Product::whereKey($item['product_id'])->lockForUpdate()->firstOrFail();
                if ((float) $product->harga < 0) {
                    throw ValidationException::withMessages([
                        'items' => "Harga produk {$product->name} tidak valid.",
                    ]);
                }
                if (!$product->isActive() || $product->stok < $item['quantity']) {
                    throw ValidationException::withMessages([
                        'items' => "Stok {$product->name} tidak mencukupi.",
                    ]);
                }

                $lineSubtotal = round((float) $product->harga * $item['quantity']);
                $subtotal += $lineSubtotal;
                $details[] = compact('product', 'item', 'lineSubtotal');
            }

            $discountPercent = (float) ($validated['discount_percent'] ?? 0);
            $discountNominal = (float) ($validated['discount_nominal'] ?? 0);
            $discount = min($subtotal, round(($subtotal * $discountPercent / 100) + $discountNominal));
            $tax = round((float) ($validated['tax'] ?? 0));
            $otherFee = round((float) ($validated['other_fee'] ?? 0));
            $grandTotal = max(0, $subtotal - $discount + $tax + $otherFee);
            $paidAmount = round((float) $validated['paid_amount']);

            if ($paidAmount < $grandTotal) {
                throw ValidationException::withMessages([
                    'paid_amount' => 'Pembayaran kurang dari total transaksi.',
                ]);
            }

            $transaction = Transaction::create([
                'user_id' => $request->user()->id,
                'checkout_token' => $validated['checkout_token'],
                'customer_id' => $validated['customer_id'] ?? null,
                'subtotal' => $subtotal,
                'discount' => $discount,
                'tax' => $tax,
                'other_fee' => $otherFee,
                'total_payment' => $grandTotal,
                'paid_amount' => $paidAmount,
                'change_amount' => $paidAmount - $grandTotal,
                'payment_method' => $validated['payment_method'],
                'status' => 'completed',
            ]);

            foreach ($details as $detail) {
                $detail['product']->decrement('stok', $detail['item']['quantity']);
                TransactionDetail::create([
                    'transaction_id' => $transaction->id,
                    'product_id' => $detail['product']->id,
                    'product_code' => $detail['product']->product_code,
                    'product_name' => $detail['product']->name,
                    'quantity' => $detail['item']['quantity'],
                    'price' => $detail['product']->harga,
                    'subtotal' => $detail['lineSubtotal'],
                ]);
            }

            Payment::create([
                'transaction_id' => $transaction->id,
                'payment_code' => 'PAY-' . strtoupper(Str::random(12)),
                'amount' => $paidAmount,
                'payment_date' => now(),
                'status' => 'paid',
            ]);

                return $transaction->load(['user', 'customer', 'details']);
            });
        } catch (ValidationException $exception) {
            return back()->withErrors($exception->errors())->withInput();
        }

        return redirect()->route('kasir.pos')->with('receipt', $transaction);
    }
}