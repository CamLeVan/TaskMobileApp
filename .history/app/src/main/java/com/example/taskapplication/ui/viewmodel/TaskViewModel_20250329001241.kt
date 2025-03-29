package com.example.taskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.model.PersonalTask
import com.example.taskapplication.data.model.TeamTask
import com.example.taskapplication.data.model.TeamTaskAssignment
import com.example.taskapplication.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    // Personal Tasks
    private val _personalTasks = MutableStateFlow<List<PersonalTask>>(emptyList())
    val personalTasks: StateFlow<List<PersonalTask>> = _personalTasks.asStateFlow()

    private val _personalTaskState = MutableStateFlow<TaskState>(TaskState.Initial)
    val personalTaskState: StateFlow<TaskState> = _personalTaskState.asStateFlow()

    // Team Tasks
    private val _teamTasks = MutableStateFlow<List<TeamTask>>(emptyList())
    val teamTasks: StateFlow<List<TeamTask>> = _teamTasks.asStateFlow()

    private val _teamTaskState = MutableStateFlow<TaskState>(TaskState.Initial)
    val teamTaskState: StateFlow<TaskState> = _teamTaskState.asStateFlow()

    // Task Assignments
    private val _taskAssignments = MutableStateFlow<List<TeamTaskAssignment>>(emptyList())
    val taskAssignments: StateFlow<List<TeamTaskAssignment>> = _taskAssignments.asStateFlow()

    private val _taskAssignmentState = MutableStateFlow<TaskState>(TaskState.Initial)
    val taskAssignmentState: StateFlow<TaskState> = _taskAssignmentState.asStateFlow()

    // Personal Tasks
    fun getPersonalTasks() {
        viewModelScope.launch {
            _personalTaskState.value = TaskState.Loading
            taskRepository.getPersonalTasks()
                .onSuccess { tasks ->
                    _personalTasks.value = tasks
                    _personalTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _personalTaskState.value = TaskState.Error(error.message ?: "Failed to get personal tasks")
                }
        }
    }

    fun createPersonalTask(task: PersonalTask) {
        viewModelScope.launch {
            _personalTaskState.value = TaskState.Loading
            taskRepository.createPersonalTask(task)
                .onSuccess {
                    getPersonalTasks()
                    _personalTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _personalTaskState.value = TaskState.Error(error.message ?: "Failed to create personal task")
                }
        }
    }

    fun updatePersonalTask(taskId: Long, task: PersonalTask) {
        viewModelScope.launch {
            _personalTaskState.value = TaskState.Loading
            taskRepository.updatePersonalTask(taskId, task)
                .onSuccess {
                    getPersonalTasks()
                    _personalTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _personalTaskState.value = TaskState.Error(error.message ?: "Failed to update personal task")
                }
        }
    }

    fun deletePersonalTask(taskId: Long) {
        viewModelScope.launch {
            _personalTaskState.value = TaskState.Loading
            taskRepository.deletePersonalTask(taskId)
                .onSuccess {
                    getPersonalTasks()
                    _personalTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _personalTaskState.value = TaskState.Error(error.message ?: "Failed to delete personal task")
                }
        }
    }

    // Team Tasks
    fun getTeamTasks(teamId: Long) {
        viewModelScope.launch {
            _teamTaskState.value = TaskState.Loading
            taskRepository.getTeamTasks(teamId)
                .onSuccess { tasks ->
                    _teamTasks.value = tasks
                    _teamTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _teamTaskState.value = TaskState.Error(error.message ?: "Failed to get team tasks")
                }
        }
    }

    fun createTeamTask(teamId: Long, task: TeamTask) {
        viewModelScope.launch {
            _teamTaskState.value = TaskState.Loading
            taskRepository.createTeamTask(teamId, task)
                .onSuccess {
                    getTeamTasks(teamId)
                    _teamTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _teamTaskState.value = TaskState.Error(error.message ?: "Failed to create team task")
                }
        }
    }

    fun updateTeamTask(teamId: Long, taskId: Long, task: TeamTask) {
        viewModelScope.launch {
            _teamTaskState.value = TaskState.Loading
            taskRepository.updateTeamTask(teamId, taskId, task)
                .onSuccess {
                    getTeamTasks(teamId)
                    _teamTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _teamTaskState.value = TaskState.Error(error.message ?: "Failed to update team task")
                }
        }
    }

    fun deleteTeamTask(teamId: Long, taskId: Long) {
        viewModelScope.launch {
            _teamTaskState.value = TaskState.Loading
            taskRepository.deleteTeamTask(teamId, taskId)
                .onSuccess {
                    getTeamTasks(teamId)
                    _teamTaskState.value = TaskState.Success
                }
                .onFailure { error ->
                    _teamTaskState.value = TaskState.Error(error.message ?: "Failed to delete team task")
                }
        }
    }

    // Task Assignments
    fun getTaskAssignments(teamId: Long, taskId: Long) {
        viewModelScope.launch {
            _taskAssignmentState.value = TaskState.Loading
            taskRepository.getTaskAssignments(teamId, taskId)
                .onSuccess { assignments ->
                    _taskAssignments.value = assignments
                    _taskAssignmentState.value = TaskState.Success
                }
                .onFailure { error ->
                    _taskAssignmentState.value = TaskState.Error(error.message ?: "Failed to get task assignments")
                }
        }
    }

    fun createTaskAssignment(teamId: Long, taskId: Long, assignment: TeamTaskAssignment) {
        viewModelScope.launch {
            _taskAssignmentState.value = TaskState.Loading
            taskRepository.createTaskAssignment(teamId, taskId, assignment)
                .onSuccess {
                    getTaskAssignments(teamId, taskId)
                    _taskAssignmentState.value = TaskState.Success
                }
                .onFailure { error ->
                    _taskAssignmentState.value = TaskState.Error(error.message ?: "Failed to create task assignment")
                }
        }
    }

    fun updateTaskAssignment(
        teamId: Long,
        taskId: Long,
        assignmentId: Long,
        assignment: TeamTaskAssignment
    ) {
        viewModelScope.launch {
            _taskAssignmentState.value = TaskState.Loading
            taskRepository.updateTaskAssignment(teamId, taskId, assignmentId, assignment)
                .onSuccess {
                    getTaskAssignments(teamId, taskId)
                    _taskAssignmentState.value = TaskState.Success
                }
                .onFailure { error ->
                    _taskAssignmentState.value = TaskState.Error(error.message ?: "Failed to update task assignment")
                }
        }
    }

    fun deleteTaskAssignment(teamId: Long, taskId: Long, assignmentId: Long) {
        viewModelScope.launch {
            _taskAssignmentState.value = TaskState.Loading
            taskRepository.deleteTaskAssignment(teamId, taskId, assignmentId)
                .onSuccess {
                    getTaskAssignments(teamId, taskId)
                    _taskAssignmentState.value = TaskState.Success
                }
                .onFailure { error ->
                    _taskAssignmentState.value = TaskState.Error(error.message ?: "Failed to delete task assignment")
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