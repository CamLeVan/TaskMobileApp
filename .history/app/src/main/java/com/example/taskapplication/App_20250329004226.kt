package com.example.taskapplication

import android.app.Application
import com.example.taskapplication.data.api.ApiService
import com.example.taskapplication.data.manager.AuthManager
import com.example.taskapplication.data.repository.AuthRepository
import com.example.taskapplication.data.repository.ChatRepository
import com.example.taskapplication.data.repository.TaskRepository
import com.example.taskapplication.data.repository.TeamRepository
import com.example.taskapplication.ui.viewmodel.AuthViewModel
import com.example.taskapplication.ui.viewmodel.ChatViewModel
import com.example.taskapplication.ui.viewmodel.TaskViewModel
import com.example.taskapplication.ui.viewmodel.TeamViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var authViewModel: AuthViewModel
    lateinit var taskViewModel: TaskViewModel
    lateinit var teamViewModel: TeamViewModel
    lateinit var chatViewModel: ChatViewModel

    override fun onCreate() {
        super.onCreate()
        setupDependencies()
    }

    private fun setupDependencies() {
        val retrofit = Retrofit.Builder()
            .baseUrl("YOUR_API_BASE_URL")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val authManager = AuthManager(this)

        val authRepository = AuthRepository(apiService, authManager)
        val taskRepository = TaskRepository(apiService, authManager)
        val teamRepository = TeamRepository(apiService, authManager)
        val chatRepository = ChatRepository(apiService, authManager)

        authViewModel = AuthViewModel(authRepository)
        taskViewModel = TaskViewModel(taskRepository)
        teamViewModel = TeamViewModel(teamRepository)
        chatViewModel = ChatViewModel(chatRepository)
    }
} 