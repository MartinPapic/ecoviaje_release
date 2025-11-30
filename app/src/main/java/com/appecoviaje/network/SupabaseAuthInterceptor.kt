package com.appecoviaje.network

import okhttp3.Interceptor
import okhttp3.Response

class SupabaseAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
            .header("apikey", SupabaseConfig.SUPABASE_KEY)
            .header("Content-Type", "application/json")

        // If we had a stored session token, we would add it here:
        // .header("Authorization", "Bearer $token")
        // For now, we rely on the apikey for public access and will handle Auth separately or add token logic later if needed for RLS.
        
        return chain.proceed(builder.build())
    }
}
