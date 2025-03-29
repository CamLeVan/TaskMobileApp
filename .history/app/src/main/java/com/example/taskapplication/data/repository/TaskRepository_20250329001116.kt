package com.example.taskapplication.data.repository

import com.example.taskapplication.data.api.ApiService
import com.example.taskapplication.data.model.PersonalTask
import com.example.taskapplication.data.model.TeamTask
import com.example.taskapplication.data.model.TeamTaskAssignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val apiService: ApiService) {
    // Personal Tasks
    suspend fun getPersonalTasks() = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPersonalTasks()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get personal tasks: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPersonalTask(task: PersonalTask) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createPersonalTask(task)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create personal task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePersonalTask(taskId: Long, task: PersonalTask) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updatePersonalTask(taskId, task)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update personal task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePersonalTask(taskId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deletePersonalTask(taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete personal task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Team Tasks
    suspend fun getTeamTasks(teamId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTeamTasks(teamId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get team tasks: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTeamTask(teamId: Long, task: TeamTask) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createTeamTask(teamId, task)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create team task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTeamTask(teamId: Long, taskId: Long, task: TeamTask) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateTeamTask(teamId, taskId, task)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update team task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeamTask(teamId: Long, taskId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteTeamTask(teamId, taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete team task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Team Task Assignments
    suspend fun getTaskAssignments(teamId: Long, taskId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTaskAssignments(teamId, taskId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get task assignments: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTaskAssignment(
        teamId: Long,
        taskId: Long,
        assignment: TeamTaskAssignment
    ) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createTaskAssignment(teamId, taskId, assignment)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create task assignment: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTaskAssignment(
        teamId: Long,
        taskId: Long,
        assignmentId: Long,
        assignment: TeamTaskAssignment
    ) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateTaskAssignment(teamId, taskId, assignmentId, assignment)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update task assignment: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTaskAssignment(teamId: Long, taskId: Long, assignmentId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteTaskAssignment(teamId, taskId, assignmentId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete task assignment: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 