package com.example.taskapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskapplication.data.local.entities.PersonalTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalTaskDao {
    @Query("SELECT * FROM personal_tasks WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllByUserId(userId: Long): Flow<List<PersonalTaskEntity>>

    @Query("SELECT * FROM personal_tasks WHERE isSynced = 0")
    suspend fun getUnsyncedTasks(): List<PersonalTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<PersonalTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: PersonalTaskEntity)

    @Update
    suspend fun update(task: PersonalTaskEntity)

    @Delete
    suspend fun delete(task: PersonalTaskEntity)

    @Query("UPDATE personal_tasks SET isSynced = 1 WHERE taskId = :taskId")
    suspend fun markAsSynced(taskId: Long)
}