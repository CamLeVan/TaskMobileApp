package com.example.taskapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskapplication.data.local.entities.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams WHERE teamId IN (SELECT teamId FROM team_members WHERE userId = :userId)")
    fun getAllByUserId(userId: Long): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE isSynced = 0")
    suspend fun getUnsyncedTeams(): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: TeamEntity)

    @Update
    suspend fun update(team: TeamEntity)

    @Delete
    suspend fun delete(team: TeamEntity)

    @Query("UPDATE teams SET isSynced = 1 WHERE teamId = :teamId")
    suspend fun markAsSynced(teamId: Long)
}