package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import com.example.journeytowealth.ui.myhabit.HabitStatus

@Entity(
    tableName = "habit_check",
    primaryKeys = ["habitId", "date"]
)
data class HabitCheckEntity(
    val habitId: Int,
    val date: Long,
    val status: HabitStatus
)