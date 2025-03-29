package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_tasks")
data class TeamTaskEntity(
    @PrimaryKey
    val teamTaskId: Long,
    val teamId: Long,
    val createdBy: Long,
    val title: String,
    val description: String?,
    val deadline: Long?,
    val priority: Int?,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean = false
)