package com.example.journeytowealth.data.local

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.dao.HabitDao
import com.example.journeytowealth.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

class HabitLocalDataSource(
    private val habitDao: HabitDao
) {

    fun getHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }

    suspend fun insertHabit(
        habit: HabitEntity
    ): HttpResult<Unit> {
        return try {
            habitDao.insert(habit)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

    suspend fun deleteHabit(
        habit: HabitEntity
    ): HttpResult<Unit> {
        return try {
            habitDao.delete(habit)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }
}