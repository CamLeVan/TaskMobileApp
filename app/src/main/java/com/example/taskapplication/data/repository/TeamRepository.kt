package com.example.taskapplication.data.repository

import com.example.taskapplication.data.api.ApiService
import com.example.taskapplication.data.model.Team
import com.example.taskapplication.data.model.TeamMember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRepository(private val apiService: ApiService) {
    suspend fun getTeams() = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTeams()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get teams: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTeam(team: Team) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createTeam(team)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create team: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTeam(teamId: Long, team: Team) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateTeam(teamId, team)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update team: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeam(teamId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteTeam(teamId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete team: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Team Members
    suspend fun getTeamMembers(teamId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTeamMembers(teamId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get team members: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTeamMember(teamId: Long, member: TeamMember) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.addTeamMember(teamId, member)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to add team member: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeTeamMember(teamId: Long, userId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.removeTeamMember(teamId, userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to remove team member: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 