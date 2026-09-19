package com.teamflow.monitor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductionRecordDao {
    @Query("SELECT * FROM production_records ORDER BY id DESC")
    fun getAll(): Flow<List<ProductionRecord>>

    @Query("SELECT * FROM production_records WHERE memberName = :name ORDER BY id DESC")
    fun getForMember(name: String): Flow<List<ProductionRecord>>

    @Insert
    suspend fun insert(record: ProductionRecord): Long
}
