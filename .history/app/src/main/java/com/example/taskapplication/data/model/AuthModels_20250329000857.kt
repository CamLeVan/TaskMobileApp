package com.example.taskapplication.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val email_verified_at: String?
)

data class AuthResponse(
    val user: User,
    val token: String
) 