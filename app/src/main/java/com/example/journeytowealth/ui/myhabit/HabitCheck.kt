package com.example.journeytowealth.ui.myhabit

data class HabitCheck(
    val habitId: Int,
    val date: Long,
    val status: HabitStatus
)

enum class HabitStatus {
    SUCCESS,
    FAIL,
    NONE,
    DONE
}