package com.example.taskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.model.Task
import com.example.taskapplication.data.model.TeamTask
import com.example.taskapplication.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _teamTasks = MutableStateFlow<List<TeamTask>>(emptyList())
    val teamTasks: StateFlow<List<TeamTask>> = _teamTasks.asStateFlow()

    private val _tasksState = MutableStateFlow<TaskState>(TaskState.Initial)
    val tasksState: StateFlow<TaskState> = _tasksState.asStateFlow()

    private val _teamTasksState = MutableStateFlow<TaskState>(TaskState.Initial)
    val teamTasksState: StateFlow<TaskState> = _teamTasksState.asStateFlow()

    fun getPersonalTasks() {
        viewModelScope.launch {
            _tasksState.value = TaskState.Loading
            taskRepository.getPersonalTasks()
                .onSuccess { tasks ->
                    _tasks.value = tasks
                    _tasksState.value = TaskState.Success
                }
                .onFailure { error ->
                    _tasksState.value = TaskState.Error(error.message ?: "Failed to get tasks")
                }
        }
    }

    fun getTeamTasks(teamId: Long) {
        viewModelScope.launch {
            _teamTasksState.value = TaskState.Loading
            taskRepository.getTeamTasks(teamId)
                .onSuccess { tasks ->
                    _teamTasks.value = tasks
                    _teamTasksState.value = TaskState.Success
                }
                .onFailure { error ->
                    _teamTasksState.value = TaskState.Error(error.message ?: "Failed to get team tasks")
                }
        }
    }

    fun createPersonalTask(
        title: String,
        description: String?,
        deadline: Date,
        priority: String
    ) {
        viewModelScope.launch {
            _tasksState.value = TaskState.Loading
            taskRepository.createPersonalTask(title, description, deadline, priority)
                .onSuccess {
                    getPersonalTasks()
                }
                .onFailure { error ->
                    _tasksState.value = TaskState.Error(error.message ?: "Failed to create task")
                }
        }
    }

    fun createTeamTask(
        teamId: Long,
        title: String,
        description: String?,
        deadline: Date,
        priority: String
    ) {
        viewModelScope.launch {
            _teamTasksState.value = TaskState.Loading
            taskRepository.createTeamTask(teamId, title, description, deadline, priority)
                .onSuccess {
                    getTeamTasks(teamId)
                }
                .onFailure { error ->
                    _teamTasksState.value = TaskState.Error(error.message ?: "Failed to create team task")
                }
        }
    }

    fun updateTask(
        taskId: Long,
        title: String,
        description: String?,
        deadline: Date,
        priority: String,
        status: String
    ) {
        viewModelScope.launch {
            _tasksState.value = TaskState.Loading
            taskRepository.updateTask(taskId, title, description, deadline, priority, status)
                .onSuccess {
                    getPersonalTasks()
                }
                .onFailure { error ->
                    _tasksState.value = TaskState.Error(error.message ?: "Failed to update task")
                }
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            _tasksState.value = TaskState.Loading
            taskRepository.deleteTask(taskId)
                .onSuccess {
                    getPersonalTasks()
                }
                .onFailure { error ->
                    _tasksState.value = TaskState.Error(error.message ?: "Failed to delete task")
                }
        }
    }
}

sealed class TaskState {
    object Initial : TaskState()
    object Loading : TaskState()
    object Success : TaskState()
    data class Error(val message: String) : TaskState()
} 