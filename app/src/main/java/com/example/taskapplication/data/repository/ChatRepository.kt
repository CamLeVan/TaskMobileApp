package com.example.taskapplication.data.repository

import com.example.taskapplication.data.api.ApiService
import com.example.taskapplication.data.model.ChatMessage
import com.example.taskapplication.data.model.UnreadMessageCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val apiService: ApiService) {
    suspend fun getChatMessages(teamId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getChatMessages(teamId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get chat messages: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendChatMessage(teamId: Long, message: ChatMessage) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendChatMessage(teamId, message)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to send chat message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markMessageAsRead(teamId: Long, messageId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.markMessageAsRead(teamId, messageId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark message as read: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnreadMessageCount(teamId: Long) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUnreadMessageCount(teamId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Result.success(apiResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get unread message count: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 