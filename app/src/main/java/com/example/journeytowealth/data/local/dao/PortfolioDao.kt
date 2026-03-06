package com.example.journeytowealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PortfolioEntity>)

    @Query("SELECT * FROM portfolio")
    fun getAll(): Flow<List<PortfolioEntity>>

    @Query("DELETE FROM portfolio")
    suspend fun deleteAll()
}