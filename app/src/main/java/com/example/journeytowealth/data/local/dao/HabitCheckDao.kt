package com.example.journeytowealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.journeytowealth.data.local.entity.HabitCheckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCheckDao {

    @Query("SELECT * FROM habit_check WHERE date = :date")
    fun getChecksByDate(date: Long): Flow<List<HabitCheckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(check: HabitCheckEntity)

    @Query("DELETE FROM habit_check WHERE habitId = :habitId AND date = :date")
    suspend fun delete(habitId: Int, date: Long)
}