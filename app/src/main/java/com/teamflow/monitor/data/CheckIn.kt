package com.teamflow.monitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkins")
data class CheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: Long = 0,
    val memberName: String,
    val date: String,
    val shift: String
)
