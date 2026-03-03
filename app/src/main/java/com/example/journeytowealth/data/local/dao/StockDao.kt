package com.example.journeytowealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.journeytowealth.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<StockEntity>)

    @Query("SELECT * FROM stock")
    fun getAll(): Flow<List<StockEntity>>

    @Query("DELETE FROM stock")
    suspend fun deleteAll()
}