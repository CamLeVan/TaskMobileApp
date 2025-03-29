package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey
    val teamId: Long,
    val name: String,
    val description: String?,
    val createdBy: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean = false
)