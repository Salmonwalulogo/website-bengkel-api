<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class RoleMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     * @param  string  ...$roles
     */
    public function handle(Request $request, Closure $next, string ...$roles): Response
    {
        // Cek apakah user sudah login
        if (!$request->user()) {
            return redirect()->route('login');
        }

        $allowedRoles = array_filter(array_map('trim', explode(',', implode(',', $roles))));

        if (! in_array($request->user()->role, $allowedRoles, true)) {
            abort(403, 'Unauthorized action. Anda tidak memiliki akses ke halaman ini.');
        }

        return $next($request);
    }
}