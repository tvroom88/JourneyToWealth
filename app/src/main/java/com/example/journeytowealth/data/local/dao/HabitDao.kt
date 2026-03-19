package com.example.journeytowealth.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.journeytowealth.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(habits: List<HabitEntity>)

    @Update
    fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Query("DELETE FROM habit WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM habit")
    fun deleteAll()
}