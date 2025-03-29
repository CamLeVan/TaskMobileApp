package com.example.taskapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey
    val id: Long,
    val teamId: Long,
    val userId: Long,
    val role: String,
    val joinedAt: Long,
    val isSynced: Boolean = false
)