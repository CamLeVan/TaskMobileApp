package com.example.taskapplication.data.api

import com.example.taskapplication.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Authentication
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("user")
    suspend fun getUser(): Response<ApiResponse<User>>

    // Personal Tasks
    @GET("personal-tasks")
    suspend fun getPersonalTasks(): Response<ApiResponse<List<PersonalTask>>>

    @POST("personal-tasks")
    suspend fun createPersonalTask(@Body task: PersonalTask): Response<ApiResponse<PersonalTask>>

    @GET("personal-tasks/{taskId}")
    suspend fun getPersonalTask(@Path("taskId") taskId: Long): Response<ApiResponse<PersonalTask>>

    @PUT("personal-tasks/{taskId}")
    suspend fun updatePersonalTask(
        @Path("taskId") taskId: Long,
        @Body task: PersonalTask
    ): Response<ApiResponse<PersonalTask>>

    @DELETE("personal-tasks/{taskId}")
    suspend fun deletePersonalTask(@Path("taskId") taskId: Long): Response<ApiResponse<Unit>>

    // Teams
    @GET("teams")
    suspend fun getTeams(): Response<ApiResponse<List<Team>>>

    @POST("teams")
    suspend fun createTeam(@Body team: Team): Response<ApiResponse<Team>>

    @GET("teams/{teamId}")
    suspend fun getTeam(@Path("teamId") teamId: Long): Response<ApiResponse<Team>>

    @PUT("teams/{teamId}")
    suspend fun updateTeam(
        @Path("teamId") teamId: Long,
        @Body team: Team
    ): Response<ApiResponse<Team>>

    @DELETE("teams/{teamId}")
    suspend fun deleteTeam(@Path("teamId") teamId: Long): Response<ApiResponse<Unit>>

    // Team Members
    @GET("teams/{teamId}/members")
    suspend fun getTeamMembers(@Path("teamId") teamId: Long): Response<ApiResponse<List<TeamMember>>>

    @POST("teams/{teamId}/members")
    suspend fun addTeamMember(
        @Path("teamId") teamId: Long,
        @Body member: TeamMember
    ): Response<ApiResponse<TeamMember>>

    @DELETE("teams/{teamId}/members/{userId}")
    suspend fun removeTeamMember(
        @Path("teamId") teamId: Long,
        @Path("userId") userId: Long
    ): Response<ApiResponse<Unit>>

    // Team Tasks
    @GET("teams/{teamId}/tasks")
    suspend fun getTeamTasks(@Path("teamId") teamId: Long): Response<ApiResponse<List<TeamTask>>>

    @POST("teams/{teamId}/tasks")
    suspend fun createTeamTask(
        @Path("teamId") teamId: Long,
        @Body task: TeamTask
    ): Response<ApiResponse<TeamTask>>

    @GET("teams/{teamId}/tasks/{taskId}")
    suspend fun getTeamTask(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long
    ): Response<ApiResponse<TeamTask>>

    @PUT("teams/{teamId}/tasks/{taskId}")
    suspend fun updateTeamTask(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long,
        @Body task: TeamTask
    ): Response<ApiResponse<TeamTask>>

    @DELETE("teams/{teamId}/tasks/{taskId}")
    suspend fun deleteTeamTask(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long
    ): Response<ApiResponse<Unit>>

    // Team Task Assignments
    @GET("teams/{teamId}/tasks/{taskId}/assignments")
    suspend fun getTaskAssignments(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long
    ): Response<ApiResponse<List<TeamTaskAssignment>>>

    @POST("teams/{teamId}/tasks/{taskId}/assignments")
    suspend fun createTaskAssignment(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long,
        @Body assignment: TeamTaskAssignment
    ): Response<ApiResponse<TeamTaskAssignment>>

    @PUT("teams/{teamId}/tasks/{taskId}/assignments/{assignmentId}")
    suspend fun updateTaskAssignment(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long,
        @Path("assignmentId") assignmentId: Long,
        @Body assignment: TeamTaskAssignment
    ): Response<ApiResponse<TeamTaskAssignment>>

    @DELETE("teams/{teamId}/tasks/{taskId}/assignments/{assignmentId}")
    suspend fun deleteTaskAssignment(
        @Path("teamId") teamId: Long,
        @Path("taskId") taskId: Long,
        @Path("assignmentId") assignmentId: Long
    ): Response<ApiResponse<Unit>>

    // Chat
    @GET("teams/{teamId}/chat")
    suspend fun getChatMessages(@Path("teamId") teamId: Long): Response<ApiResponse<List<ChatMessage>>>

    @POST("teams/{teamId}/chat")
    suspend fun sendChatMessage(
        @Path("teamId") teamId: Long,
        @Body message: ChatMessage
    ): Response<ApiResponse<ChatMessage>>

    @PUT("teams/{teamId}/chat/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("teamId") teamId: Long,
        @Path("messageId") messageId: Long
    ): Response<ApiResponse<Unit>>

    @GET("teams/{teamId}/chat/unread")
    suspend fun getUnreadMessageCount(@Path("teamId") teamId: Long): Response<ApiResponse<UnreadMessageCount>>
} 