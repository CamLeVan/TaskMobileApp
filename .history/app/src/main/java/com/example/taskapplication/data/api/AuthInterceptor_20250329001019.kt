package com.example.taskapplication.data.api

import com.example.taskapplication.data.auth.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = authManager.getToken()

        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
} 