package com.example.taskapplication.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("personal-tasks")
    suspend fun getPersonalTasks(): Response<List<PersonalTaskResponse>>

    @POST("personal-tasks")
    suspend fun createPersonalTask(@Body request: CreateTaskRequest): Response<PersonalTaskResponse>

    // Thêm các API endpoints khác...
}

object ApiClient {
    private const val BASE_URL = "http://your-api-url/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${SessionManager.getToken()}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}