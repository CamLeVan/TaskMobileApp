package com.example.taskapplication.data.model

data class ApiResponse<T>(
    val data: T,
    val message: String,
    val status: Int
)

data class ApiError(
    val message: String,
    val errors: Map<String, List<String>>,
    val status: Int
) 