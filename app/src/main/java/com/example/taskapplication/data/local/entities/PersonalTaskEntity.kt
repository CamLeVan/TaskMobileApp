package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_tasks")
data class PersonalTaskEntity(
    @PrimaryKey
    val taskId: Long,
    val userId: Long,
    val title: String,
    val description: String?,
    val deadline: Long?,
    val priority: Int?,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean = false
)