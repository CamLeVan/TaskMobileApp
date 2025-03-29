package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey
    val messageId: Long,
    val teamId: Long,
    val senderId: Long,
    val message: String?,
    val fileUrl: String?,
    val timestamp: Long,
    val isSynced: Boolean = false
)