package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 습관
 */
@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String
)