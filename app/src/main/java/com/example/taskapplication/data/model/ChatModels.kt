package com.example.taskapplication.data.model

data class ChatMessage(
    val messageId: Long,
    val teamId: Long,
    val senderId: Long,
    val message: String?,
    val fileUrl: String?,
    val timestamp: String,
    val isRead: Boolean = false
)

data class UnreadMessageCount(
    val count: Int
) 