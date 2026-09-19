package com.teamflow.monitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_members")
data class TeamMember(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val contact: String = "",
    val schedule: String = "",
    val notes: String = "",
    val active: Boolean = true,
    val checkinCount: Int = 0
)
