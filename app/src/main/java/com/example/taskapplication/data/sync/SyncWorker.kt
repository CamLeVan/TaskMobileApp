package com.example.taskapplication.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskapplication.data.repository.PersonalTaskRepository

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var personalTaskRepository: PersonalTaskRepository

    @Inject
    lateinit var teamRepository: TeamRepository

    override suspend fun doWork(): Result {
        return try {
            // Sync personal tasks
            val unsyncedTasks = personalTaskRepository.getUnsyncedTasks()
            personalTaskRepository.syncTasks(unsyncedTasks)

            // Sync teams
            val unsyncedTeams = teamRepository.getUnsyncedTeams()
            teamRepository.syncTeams(unsyncedTeams)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}