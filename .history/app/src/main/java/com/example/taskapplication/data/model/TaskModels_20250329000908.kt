package com.example.taskapplication.data.model

data class PersonalTask(
    val taskId: Long,
    val userId: Long,
    val title: String,
    val description: String?,
    val deadline: String?,
    val priority: Int?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

data class TeamTask(
    val teamTaskId: Long,
    val teamId: Long,
    val createdBy: Long,
    val title: String,
    val description: String?,
    val deadline: String?,
    val priority: Int?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

data class TeamTaskAssignment(
    val assignmentId: Long,
    val teamTaskId: Long,
    val assignedTo: Long,
    val status: String,
    val progress: Int,
    val assignedAt: String,
    val updatedAt: String?
)

data class Team(
    val teamId: Long,
    val name: String,
    val description: String?,
    val createdBy: Long,
    val createdAt: String,
    val updatedAt: String
)

data class TeamMember(
    val id: Long,
    val teamId: Long,
    val userId: Long,
    val role: String,
    val joinedAt: String
) 