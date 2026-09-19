package com.teamflow.monitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "production_records")
data class ProductionRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val item: String,
    val quantity: Double,
    val unit: String,
    val date: String,
    val memberName: String,
    val notes: String = ""
)
