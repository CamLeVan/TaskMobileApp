package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_task_assignments")
data class TeamTaskAssignmentEntity(
    @PrimaryKey
    val assignmentId: Long,
    val teamTaskId: Long,
    val assignedTo: Long,
    val status: String,
    val progress: Int,
    val assignedAt: Long,
    val updatedAt: Long?,
    val isSynced: Boolean = false
)