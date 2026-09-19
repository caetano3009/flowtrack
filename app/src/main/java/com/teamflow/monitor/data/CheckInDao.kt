package com.teamflow.monitor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM checkins ORDER BY id DESC")
    fun getAll(): Flow<List<CheckIn>>

    @Query("SELECT * FROM checkins WHERE memberName = :name ORDER BY id DESC")
    fun getForMember(name: String): Flow<List<CheckIn>>

    @Query("SELECT COUNT(*) FROM checkins WHERE date = :today")
    fun getTodayCount(today: String): Flow<Int>

    @Insert
    suspend fun insert(checkIn: CheckIn): Long
}
