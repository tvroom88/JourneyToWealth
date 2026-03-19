package com.example.journeytowealth.data.local

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.dao.HabitCheckDao
import com.example.journeytowealth.data.local.entity.HabitCheckEntity
import com.example.journeytowealth.ui.myhabit.HabitStatus
import kotlinx.coroutines.flow.Flow

class HabitCheckLocalDataSource(
    private val habitCheckDao: HabitCheckDao
) {

    fun getChecksByDate(
        date: Long
    ): Flow<List<HabitCheckEntity>> {
        return habitCheckDao.getChecksByDate(date)
    }

    suspend fun insertCheck(
        habitId: Int,
        date: Long
    ): HttpResult<Unit> {
        return try {
            habitCheckDao.insert(
                HabitCheckEntity(
                    habitId = habitId,
                    date = date,
                    status = HabitStatus.DONE
                )
            )
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

    suspend fun deleteCheck(
        habitId: Int,
        date: Long
    ): HttpResult<Unit> {
        return try {
            habitCheckDao.delete(habitId, date)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }
}