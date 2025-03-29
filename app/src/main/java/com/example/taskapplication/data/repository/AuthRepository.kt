package com.example.taskapplication.data.repository

import com.example.taskapplication.data.api.ApiService
import com.example.taskapplication.data.auth.AuthManager
import com.example.taskapplication.data.model.LoginRequest
import com.example.taskapplication.data.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiService,
    private val authManager: AuthManager
) {
    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    authManager.saveToken(apiResponse.data.token)
                    authManager.saveUser(apiResponse.data.user)
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(RegisterRequest(name, email, password, password))
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    authManager.saveToken(apiResponse.data.token)
                    authManager.saveUser(apiResponse.data.user)
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                authManager.logout()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUser()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 