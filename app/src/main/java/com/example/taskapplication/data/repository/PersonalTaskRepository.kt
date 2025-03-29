package com.example.taskapplication.data.repository
// app/src/main/java/com/example/taskmanager/data/repository/PersonalTaskRepository.kt
class PersonalTaskRepository @Inject constructor(
    private val apiService: ApiService,
    private val personalTaskDao: PersonalTaskDao,
    private val workManager: WorkManager
) {
    fun getPersonalTasks(userId: Long): Flow<List<PersonalTask>> = flow {
        // Emit local data first
        emit(personalTaskDao.getAllByUserId(userId).map { it.toDomain() })

        try {
            // Try to sync with server
            val response = apiService.getPersonalTasks()
            if (response.isSuccessful) {
                response.body()?.let { tasks ->
                    personalTaskDao.insertAll(tasks.map { it.toEntity() })
                    emit(tasks.map { it.toDomain() })
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun createTask(task: CreateTaskRequest): Result<PersonalTask> {
        return try {
            // Save locally first
            val localTask = task.toEntity()
            personalTaskDao.insert(localTask)

            // Try to sync with server
            val response = apiService.createPersonalTask(task)
            if (response.isSuccessful) {
                response.body()?.let { taskResponse ->
                    personalTaskDao.insert(taskResponse.toEntity())
                    personalTaskDao.markAsSynced(taskResponse.taskId)
                    Result.success(taskResponse.toDomain())
                } ?: Result.failure(Exception("Empty response"))
            } else {
                // Schedule sync
                scheduleSync()
                Result.success(localTask.toDomain())
            }
        } catch (e: Exception) {
            // Schedule sync
            scheduleSync()
            Result.success(localTask.toDomain())
        }
    }

    private fun scheduleSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(syncRequest)
    }

    suspend fun syncTasks(tasks: List<PersonalTaskEntity>) {
        tasks.forEach { task ->
            try {
                when {
                    task.createdAt > task.updatedAt -> {
                        // New task
                        val response = apiService.createPersonalTask(task.toRequest())
                        if (response.isSuccessful) {
                            personalTaskDao.markAsSynced(task.taskId)
                        }
                    }
                    task.updatedAt > task.createdAt -> {
                        // Updated task
                        val response = apiService.updatePersonalTask(task.taskId, task.toRequest())
                        if (response.isSuccessful) {
                            personalTaskDao.markAsSynced(task.taskId)
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}