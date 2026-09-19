package com.teamflow.monitor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {
    @Query("SELECT * FROM team_members ORDER BY name ASC")
    fun getAll(): Flow<List<TeamMember>>

    @Query("SELECT * FROM team_members WHERE lower(name) LIKE lower(:pattern) LIMIT 1")
    suspend fun findByName(pattern: String): TeamMember?

    @Insert
    suspend fun insert(member: TeamMember): Long

    @Update
    suspend fun update(member: TeamMember)
}
